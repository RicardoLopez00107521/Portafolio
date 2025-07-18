package com.feregui00124221.qrguard.controllers;

import com.feregui00124221.qrguard.domain.dtos.GeneralResponse;
import com.feregui00124221.qrguard.domain.dtos.HomeDTO.HomeDetailsDTO;
import com.feregui00124221.qrguard.domain.dtos.HomeDTO.NewHomeDTO;
import com.feregui00124221.qrguard.domain.dtos.HomeDTO.ResidentHomeDTO;
import com.feregui00124221.qrguard.domain.dtos.HomeDTO.UpdateHomeCapacityDTO;
import com.feregui00124221.qrguard.domain.entities.Home;
import com.feregui00124221.qrguard.domain.entities.Roles.Role;
import com.feregui00124221.qrguard.domain.entities.Roles.RoleCategory;
import com.feregui00124221.qrguard.domain.entities.User;
import com.feregui00124221.qrguard.services.AdminService;
import com.feregui00124221.qrguard.services.HomeService;
import com.feregui00124221.qrguard.services.RoleService;
import com.feregui00124221.qrguard.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    private final HomeService homeService;
    private final UserService userService;
    private final RoleService roleService;
    private final AdminService adminService;

    public HomeController(HomeService homeService, UserService userService, RoleService roleService, AdminService adminService) {
        this.homeService = homeService;
        this.userService = userService;
        this.roleService = roleService;
        this.adminService = adminService;
    }

    @PostMapping("/create-home")
    public ResponseEntity<GeneralResponse> createHome(@RequestBody @Valid NewHomeDTO info) {
        User admin = userService.findUserAuthenticated();
        if (admin.getRoles()
                .stream()
                .noneMatch(role ->
                        role.getRoleName().equals(RoleCategory.ADMIN))
        ) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You don't have permission to perform this action");
        }

        if (homeService.findHomeByHouseNumber(info.getHouseNumber()) != null) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "Home already exists");
        }

        if (info.getHouseCapacity() <= 0) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "House capacity must be greater than 0");
        }

        homeService.createHome(info);

        return GeneralResponse.getResponse(HttpStatus.OK, "Home created successfully. Remember to assign an in charge resident as soon as possible.");
    }

    @GetMapping("/my-home-details")
    public ResponseEntity<GeneralResponse> homeDetails() {
        User user = userService.findUserAuthenticated();
        if (user.getRoles()
                .stream()
                .noneMatch(role ->
                        role.getRoleName().equals(RoleCategory.IN_CHARGE_RESIDENT)
                                ||
                                role.getRoleName().equals(RoleCategory.NORMAL_RESIDENT)
                                ||
                                role.getRoleName().equals(RoleCategory.ADMIN))
        ) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You don't have permission to perform this action");
        }

        if (user.getLinkedHouses().isEmpty()) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "User is not linked to any home");
        }

        Home home = homeService.findHomeByHouseNumber(user.getLinkedHouses().getFirst().getHouseNumber());
        HomeDetailsDTO homeDetails = homeService.getHomeDetails(home);

        return GeneralResponse.getResponse(HttpStatus.OK, homeDetails);
    }

    @GetMapping("/my-home-details-by-number")
    public ResponseEntity<GeneralResponse> homeDetails(@RequestParam String number) {
        User user = userService.findUserAuthenticated();

        Boolean isAdmin = adminService.isAdmin(user);
        System.out.println(isAdmin);
        if (!isAdmin) {
            System.out.println("Entro el if");
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You don't have permission to perform this action");
        }

        Home home = homeService.findHomeByHouseNumber(number);
        HomeDetailsDTO homeDetails = homeService.getHomeDetails(home);

        return GeneralResponse.getResponse(HttpStatus.OK, homeDetails);
    }

    @PatchMapping("/link-new-resident")
    public ResponseEntity<GeneralResponse> linkNewResident(@RequestBody @Valid ResidentHomeDTO info) {
        User admin = userService.findUserAuthenticated();
        if (admin.getRoles()
                .stream()
                .noneMatch(role ->
                        role.getRoleName().equals(RoleCategory.ADMIN)
                                ||
                                role.getRoleName().equals(RoleCategory.IN_CHARGE_RESIDENT))
        ) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You don't have permission to perform this action");
        }

        User newResidentToLink = userService.findUserByEmail(info.getUserEmail());
        Home homeToBeLinked = homeService.findHomeByHouseNumber(info.getHouseNumber());

        if (homeToBeLinked == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Home does not exist");
        }

        if (newResidentToLink == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "User has not yet been registered in the system");
        }

        if(newResidentToLink.equals(admin)){
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "You cannot link yourself to a home");
        }

        if (homeToBeLinked.getResidents().contains(newResidentToLink)) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "User is already linked to this home");
        }

        if (homeToBeLinked.getResidents().size() >= homeToBeLinked.getHouseCapacity()) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "Home is full. Cannot add more residents.");
        }

        if (!newResidentToLink.getLinkedHouses().isEmpty()) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "User is already linked to another home");
        }

        homeService.linkNewResident(homeToBeLinked, newResidentToLink);

        if (userService.doesUserHaveRole(newResidentToLink, RoleCategory.GUEST)) {
            Role guestRole = roleService.findRoleByRoleName(RoleCategory.GUEST.toString());
            userService.deleteRoleFromUser(newResidentToLink, guestRole);
        }

        if (!userService.doesUserHaveRole(newResidentToLink, RoleCategory.NORMAL_RESIDENT)) {
            Role normalResidentRole = roleService.findRoleByRoleName(RoleCategory.NORMAL_RESIDENT.toString());
            userService.addRoleToUser(newResidentToLink, normalResidentRole);
        }

        return GeneralResponse.getResponse(HttpStatus.OK, "User linked successfully");
    }

    @PatchMapping("/assign-in-charge-resident")
    public ResponseEntity<GeneralResponse> assignNewInChargeResident(@RequestBody @Valid ResidentHomeDTO info) {
        User admin = userService.findUserAuthenticated();
        if (admin.getRoles()
                .stream()
                .noneMatch(role ->
                        role.getRoleName().equals(RoleCategory.ADMIN))
        ) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You don't have permission to perform this action");
        }

        Home home = homeService.findHomeByHouseNumber(info.getHouseNumber());
        User user = userService.findUserByEmail(info.getUserEmail());

        if (home == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Home does not exist");
        }

        if (user == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "User has not yet been registered in the system");
        }

        if (!home.getResidents().contains(user)) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "If you want to assign a new in charge resident, first link the user to the home");
        }

        if (user.equals(home.getInChargeResident())) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "User is already in charge of this home");
        }

        User oldInCharge = (home.getInChargeResident() == null)
                ?
                null
                :
                userService.findUserByEmail(home.getInChargeResident().getEmail());

        if (oldInCharge != null && userService.doesUserHaveRole(oldInCharge, RoleCategory.IN_CHARGE_RESIDENT)) {
            Role inChargeRole = roleService.findRoleByRoleName(RoleCategory.IN_CHARGE_RESIDENT.toString());
            userService.deleteRoleFromUser(oldInCharge, inChargeRole);
            adminService.addAnyRoles(oldInCharge);
        }

        if (userService.doesUserHaveRole(user, RoleCategory.GUEST)) {
            Role guestRole = roleService.findRoleByRoleName(RoleCategory.GUEST.toString());
            userService.deleteRoleFromUser(user, guestRole);
        }

        if (!userService.doesUserHaveRole(user, RoleCategory.NORMAL_RESIDENT)) {
            Role normalResidentRole = roleService.findRoleByRoleName(RoleCategory.NORMAL_RESIDENT.toString());
            userService.addRoleToUser(user, normalResidentRole);
        }

        if (!userService.doesUserHaveRole(user, RoleCategory.IN_CHARGE_RESIDENT)) {
            Role inChargeResidentRole = roleService.findRoleByRoleName(RoleCategory.IN_CHARGE_RESIDENT.toString());
            userService.addRoleToUser(user, inChargeResidentRole);
        }

        homeService.assignInChargeResident(home, user);

        return GeneralResponse.getResponse(HttpStatus.OK, "In charge resident assigned successfully");
    }

    @PatchMapping("/unbind-user")
    public ResponseEntity<GeneralResponse> unbindUser(@RequestBody @Valid ResidentHomeDTO info) {
        User admin = userService.findUserAuthenticated();
        if (admin.getRoles()
                .stream()
                .noneMatch(role ->
                        role.getRoleName().equals(RoleCategory.ADMIN)
                                ||
                                role.getRoleName().equals(RoleCategory.IN_CHARGE_RESIDENT)
                )
        ) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You don't have permission to perform this action");
        }

        User residentToUnlink = userService.findUserByEmail(info.getUserEmail());
        Home homeToUnlink = homeService.findHomeByHouseNumber(info.getHouseNumber());

        if (homeToUnlink == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Home does not exist");
        }

        if (residentToUnlink == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "User has not yet been registered in the system");
        }

        if(residentToUnlink.equals(admin)){
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "You cannot unlink yourself from a home");
        }

        if (!homeToUnlink.getResidents().contains(residentToUnlink)) {
            return GeneralResponse.getResponse(HttpStatus.OK, "User was not linked to this home already");
        }

        if (residentToUnlink.equals(homeToUnlink.getInChargeResident())) {
            if (homeToUnlink.getResidents().size() > 1) {
                return GeneralResponse.getResponse(HttpStatus.CONFLICT, "Set a new in charge resident before unlinking the current one");
            }

            Role inChargeRole = roleService.findRoleByRoleName(RoleCategory.IN_CHARGE_RESIDENT.toString());
            userService.deleteRoleFromUser(residentToUnlink, inChargeRole);
            homeService.assignInChargeResident(homeToUnlink, null);
        }

        Role normalResidentRole = roleService.findRoleByRoleName(RoleCategory.NORMAL_RESIDENT.toString());
        userService.deleteRoleFromUser(residentToUnlink, normalResidentRole);
        homeService.unlinkResident(homeToUnlink, residentToUnlink);

        Role guestRole = roleService.findRoleByRoleName(RoleCategory.GUEST.toString());
        userService.addRoleToUser(residentToUnlink, guestRole);

        return GeneralResponse.getResponse(HttpStatus.OK, "User unlinked successfully");
    }

    @GetMapping("/get-all-homes")
    public ResponseEntity<GeneralResponse> getAllHomes(){
        User admin = userService.findUserAuthenticated();
        if (admin.getRoles()
                .stream()
                .noneMatch(role ->
                        role.getRoleName().equals(RoleCategory.ADMIN))
        ) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You don't have permission to perform this action");
        }

        List<HomeDetailsDTO> homes = homeService.getAllHomesDetails();

        return GeneralResponse.getResponse(HttpStatus.OK, homes);
    }

    @PatchMapping("/update-home-capacity")
    public ResponseEntity<GeneralResponse> updateHome(@RequestBody @Valid UpdateHomeCapacityDTO info) {
        User admin = userService.findUserAuthenticated();
        if (admin.getRoles()
                .stream()
                .noneMatch(role ->
                        role.getRoleName().equals(RoleCategory.ADMIN))
        ) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You don't have permission to perform this action");
        }

        Home home = homeService.findHomeByHouseNumber(info.getHouseNumber());

        if (home == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Home does not exist");
        }

        if (info.getNewHouseCapacity() <= 0) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "House capacity must be greater than 0");
        }

        if (home.getResidents().size() > info.getNewHouseCapacity()) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "New house capacity is less than the current number of residents");
        }

        if (home.getHouseCapacity() == info.getNewHouseCapacity()) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "House capacity is already set to this value");
        }

        homeService.updateHomeCapacity(home, info.getNewHouseCapacity());

        return GeneralResponse.getResponse(HttpStatus.OK, "House capacity updated successfully");
    }
}
