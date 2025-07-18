package com.feregui00124221.qrguard.controllers;

import com.feregui00124221.qrguard.domain.dtos.CheckPermitDTO;
import com.feregui00124221.qrguard.domain.dtos.CheckQrGenerationPermitDTO;
import com.feregui00124221.qrguard.domain.dtos.GeneralResponse;
import com.feregui00124221.qrguard.domain.dtos.PermitDTO.GetPermitsDTO;
import com.feregui00124221.qrguard.domain.dtos.PermitDTO.RequestPermitDTO;
import com.feregui00124221.qrguard.domain.entities.Entries.Entries;
import com.feregui00124221.qrguard.domain.entities.Home;
import com.feregui00124221.qrguard.domain.entities.Permit.EntryType;
import com.feregui00124221.qrguard.domain.entities.Permit.Permit;
import com.feregui00124221.qrguard.domain.entities.Permit.PermitGenerationType;
import com.feregui00124221.qrguard.domain.entities.Permit.PermitState;
import com.feregui00124221.qrguard.domain.entities.User;
import com.feregui00124221.qrguard.repositories.PermitRepository;
import com.feregui00124221.qrguard.services.GuardService;
import com.feregui00124221.qrguard.services.HomeService;
import com.feregui00124221.qrguard.services.PermitService;
import com.feregui00124221.qrguard.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/permit")
public class PermitController {

    private final PermitService permitService;

    private final HomeService homeService;

    private final UserService userService;
    private final PermitRepository permitRepository;

    public PermitController(PermitService permitService, HomeService homeService, UserService userService, PermitRepository permitRepository) {
        this.permitService = permitService;
        this.homeService = homeService;
        this.userService = userService;
        this.permitRepository = permitRepository;
    }

    @GetMapping("/check-permit")
    public ResponseEntity<GeneralResponse> checkPermit(@RequestBody @Valid CheckPermitDTO info) {
        return null;
    }

    /*@GetMapping("/get-my-house-permits-requests")
    public ResponseEntity<GeneralResponse> getPermitsRequests() {
        List<GetPermitsDTO> permits = permitService.getPermitsByState("PENDING");

        if (permits.isEmpty()) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "No permits found");
        }

        return GeneralResponse.getResponse(HttpStatus.OK, permits);
    }

    //TODO: Hay que agregar la casa buzo Eguizabal
    @GetMapping("/get-my-house-permits")
    public ResponseEntity<GeneralResponse> getPermits(@RequestParam String state) {

        if (!state.equals("PENDING") && !state.equals("APPROVED") && !state.equals("REJECTED")) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Invalid state");
        }

        User user = userService.findUserAuthenticated();

        List<String> homesByUser = userService.homesByUser(user);

        if (homesByUser.isEmpty()) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "No houses found!");
        }

        List<GetPermitsDTO> permits = permitService.getPermitsByState("PENDING");

        if (permits.isEmpty()) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "No permits found");
        }

        return GeneralResponse.getResponse(HttpStatus.OK, permits);
    }*/

    @PatchMapping("/accept-permit")
    public ResponseEntity<GeneralResponse> acceptPermit(@RequestParam(required = false) String permitId) {
        if (permitId == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Permit is empty!");
        }

        try {
            UUID isValid = UUID.fromString(permitId);
        } catch (Exception e) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Invalid permit ID!");
        }

        UUID permitUUID = UUID.fromString(permitId);

        Permit searchPermit = permitService.findPermitById(permitUUID);

        if (searchPermit == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Permit not found!");
        }

        User resident = userService.findUserAuthenticated();

        Boolean isResident = userService.isResident(resident);

        if (!isResident) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "You are not resident!");
        }

        User activeResident = userService.findUserByEmail(resident.getEmail());

        if (activeResident == null)
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Resident not found");

        Home selectedHome = homeService.findHomeById(searchPermit.getHomeOfThePermit().getHomeId().toString());

        Boolean targetHome = userService.userAreInHome(selectedHome, resident);

        if (!targetHome)
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "You not leave in this home");

        permitService.acceptPermit(searchPermit);

        return GeneralResponse.getResponse(HttpStatus.OK, "Permit approved!");
    }

    @PatchMapping("/reject-permit")
    public ResponseEntity<GeneralResponse> rejectPermit(@RequestParam(required = false) String permitId) {
        if (permitId == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Permit is empty!");
        }

        try {
            UUID isValid = UUID.fromString(permitId);
        } catch (Exception e) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Invalid permit ID!");
        }

        UUID permitUUID = UUID.fromString(permitId);

        Permit searchPermit = permitService.findPermitById(permitUUID);

        if (searchPermit == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Permit not found!");
        }

        User resident = userService.findUserAuthenticated();

        Boolean isResident = userService.isResident(resident);

        if (!isResident) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "You are not resident!");
        }

        User activeResident = userService.findUserByEmail(resident.getEmail());

        if (activeResident == null)
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Resident not found");

        Home selectedHome = homeService.findHomeById(searchPermit.getHomeOfThePermit().getHomeId().toString());

        Boolean targetHome = userService.userAreInHome(selectedHome, resident);

        if (!targetHome)
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "You not leave in this home");

        permitService.rejectPermit(searchPermit);

        return GeneralResponse.getResponse(HttpStatus.OK, "Permit rejected!");
    }

    @PostMapping("/generate-permit")
    public ResponseEntity<GeneralResponse> generatePermit(@RequestBody @Valid RequestPermitDTO info) {
        User resident = userService.findUserAuthenticated();

        Boolean isResident = userService.isResident(resident);

        if (!isResident)
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "You are not a resident");

        User activeResident = userService.findUserByEmail(resident.getEmail());

        if (activeResident == null)
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Resident not found");

        LocalDate beginDate = permitService.convertDate(info.getBeginDate());
        LocalTime beginTime = permitService.convertTime(info.getBeginTime());

        if (!beginDate.isAfter(LocalDate.now())) {
            if (beginDate.equals(LocalDate.now())) {
                if (!beginTime.isAfter(LocalTime.now())) {
                    return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The begin time is in the past");
                }
            } else {
                return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The begin date is in the past");
            }
        }

        if(info.getEndDate() != null) {
            LocalDate endDate = permitService.convertDate(info.getEndDate());
            if (beginDate.isAfter(endDate)) {
                return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The end date is in the past");
            }
        }

        if(info.getEndTime() != null) {
            LocalTime endTime = permitService.convertTime(info.getEndTime());
            if (beginTime.equals(endTime)) {
                return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The end time is the same end time");
            }
            if (beginTime.isAfter(endTime)) {
                return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The end time is in the past");
            }
        }

        User user = userService.findUserByEmail(info.getGuessEmail());

        if (user == null) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The user does not exist");
        }

        Boolean isGuest = userService.isResident(user);

        if (isGuest) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The target user is a resident!");
        }

        Home selectedHome = homeService.findHomeByHouseNumber(info.getTargetHouse());

        if (selectedHome == null) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The target home does not exist");
        }

        Boolean liveHere = userService.userAreInHome(selectedHome, resident);

        if (!liveHere) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The user doesn't live in selected home");
        }

        if (info.getEntryType().equals("MULTIPLE")) {
            if (info.getEndTime() == null) {
                return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The end time is empty");
            }
        }

        if (info.getPermitGenerationType().equals("ONE_DAY")) {
            List<Boolean> conflictDateDetected = permitService.conflictedDate(selectedHome, beginDate, user);

            if (conflictDateDetected.contains(true)) {
                List<Boolean> conflictTimeDetected = permitService.conflictedTime(selectedHome, info);
                if (conflictTimeDetected.contains(true)) {
                    return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The permit of this user already exists in this date an hour");
                }
            }
            permitService.createPermit(info, user, resident, selectedHome);
        }

        if (info.getPermitGenerationType().equals("PERIODIC")) {
            if (info.getEndDate() == null) {
                return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The end date is empty");
            }

            List<DayOfWeek> days = info.getDays().stream()
                    .map(day -> DayOfWeek.valueOf(day.toUpperCase()))
                    .toList();

            List<String> haveDates = permitService.getPermitDates(permitService.convertDate(info.getBeginDate()), permitService.convertDate(info.getEndDate()), days);

            if (haveDates.isEmpty()) {
                return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "No dates in selected days on the range date");
            }

            List<Boolean> conflictDateDetected = permitService.conflictedDates(selectedHome, info, user); // Aqui empiezan las validaciones

            if (conflictDateDetected.contains(true)) {

                List<Boolean> conflictTimeDetected = permitService.conflictedTime(selectedHome, info);
                if (conflictTimeDetected.contains(true)) {
                    System.out.println(conflictTimeDetected);
                    return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The permit of this user already exists in this date an hour");
                }
            }
            permitService.createPermit(info, user, resident, selectedHome);
        }

        return GeneralResponse.getResponse(HttpStatus.OK, "Permit Created Successfully");
    }

    @GetMapping("/allPermits")
    public ResponseEntity<GeneralResponse> getAllPermitsOfMyHouses(@RequestParam(required = false) String state) {
        if (state == null) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The state is empty");
        }
        if (!state.equals("PENDING") && !state.equals("APPROVED") && !state.equals("REJECTED")) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Invalid state, the states are PENDING, APPROVED or REJECTED");
        }

        User user = userService.findUserAuthenticated();

        List<String> homesByUser = userService.homesByUser(user);

        List<GetPermitsDTO> permits = permitService.getPermits(homesByUser, state);

        if (homesByUser.isEmpty()) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "No houses found!");
        }

        if (permits.isEmpty()) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "No permits found");
        }

        return GeneralResponse.getResponse(HttpStatus.OK, permits);
    }

    @GetMapping("/permitById")
    public ResponseEntity<GeneralResponse> permitById(@RequestParam(required = false) String permitId) {
        if (permitId == null) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Permit id is required");
        }

        try {
            UUID isValid = UUID.fromString(permitId);
        } catch (Exception e) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Invalid permit ID!");
        }

        User resident = userService.findUserAuthenticated();

        Boolean isResident = userService.isResident(resident);

        if (!isResident)
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "You are not a resident");

        User activeResident = userService.findUserByEmail(resident.getEmail());

        if (activeResident == null)
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Resident not found");

        UUID permitUUID = UUID.fromString(permitId);

        Permit searchPermit = permitService.findPermitById(permitUUID);

        if (searchPermit == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Permit not found");
        }
        Home selectedHome = homeService.findHomeById(searchPermit.getHomeOfThePermit().getHomeId().toString());

        Boolean targetHome = userService.userAreInHome(selectedHome, resident);

        if (!targetHome)
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "You not leave in this home");

        GetPermitsDTO permitsDTO = permitService.findPermitByIdResponse(permitUUID);

        if (permitsDTO == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Permit not found!");
        }

        return GeneralResponse.getResponse(HttpStatus.OK, permitsDTO);
    }

    @GetMapping("/requestedPermits")
    public ResponseEntity<GeneralResponse> getAllPermitsOfMyHouses() {

        String state = "PENDING";

        User user = userService.findUserAuthenticated();

        List<String> homesByUser = userService.homesByUser(user);

        List<GetPermitsDTO> permits = permitService.getPermits(homesByUser, state);

        if (homesByUser.isEmpty()) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "No houses found!");
        }

        if (permits.isEmpty()) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "No permits found");
        }

        return GeneralResponse.getResponse(HttpStatus.OK, permits);
    }

    @GetMapping("/myPermits")
    public ResponseEntity<GeneralResponse> getMyPermitsOfMyHouses() {

        User user = userService.findUserAuthenticated();

        if (user == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "User not found");
        }

        Boolean isResident = userService.isResident(user);

        if (isResident) {
            Home home = user.getLinkedHouses().getFirst();
            RequestPermitDTO info = new RequestPermitDTO();
            System.out.println(LocalTime.now());
            info.setBeginDate(LocalDate.now().toString());

            LocalTime ahora = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String horaFormateada = ahora.format(formatter);

            info.setBeginTime(horaFormateada);
            info.setEntryType(EntryType.UNIQUE.toString());
            info.setPermitGenerationType(PermitGenerationType.ONE_DAY.toString());
            info.setGuessEmail(user.getEmail());
            info.setTargetHouse(home.getHouseNumber());
            permitService.createPermit(info, user, user, home);
        }

        List<GetPermitsDTO> permits = permitService.getPermitsByGuestToday(user);

        if (permits.isEmpty()) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "No permits found in this day");
        }

        return GeneralResponse.getResponse(HttpStatus.OK, permits);
    }
}
