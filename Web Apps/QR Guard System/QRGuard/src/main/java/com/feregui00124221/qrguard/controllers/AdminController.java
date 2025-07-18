package com.feregui00124221.qrguard.controllers;

import com.feregui00124221.qrguard.domain.dtos.AdminDTO.AddGuardDTO;
import com.feregui00124221.qrguard.domain.dtos.AdminDTO.AddTerminalDTO;
import com.feregui00124221.qrguard.domain.dtos.AdminDTO.ServeDTO;
import com.feregui00124221.qrguard.domain.dtos.GeneralResponse;
import com.feregui00124221.qrguard.domain.dtos.GuardDTO.AllGuardsDTO;
import com.feregui00124221.qrguard.domain.dtos.TerminalDTO.GetTerminalDTO;
import com.feregui00124221.qrguard.domain.entities.Roles.Role;
import com.feregui00124221.qrguard.domain.entities.Roles.RoleCategory;
import com.feregui00124221.qrguard.domain.entities.Terminal.Terminal;
import com.feregui00124221.qrguard.domain.entities.User;
import com.feregui00124221.qrguard.services.AdminService;
import com.feregui00124221.qrguard.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    private final AdminService adminService;

    public String arduinoIP;

    public AdminController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @PostMapping("/addGuard")
    public ResponseEntity<GeneralResponse> addGuard(@RequestBody @Valid AddGuardDTO info) {

        User admin = userService.findUserAuthenticated();

        Boolean isAdmin = adminService.isAdmin(admin);

        if (!isAdmin) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You are not allowed to perform this action");
        }

        User user = userService.findUserByEmail(info.getGuardEmail());

        if (user == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "User not found!");
        }

        Boolean isGuard = adminService.isGuard(user);

        if (isGuard) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "This user is a guard!");
        }

        adminService.addGuard(user);

        return GeneralResponse.getResponse(HttpStatus.OK, "Guard added successfully!");
    }

    @PatchMapping("/deleteGuard")
    public ResponseEntity<GeneralResponse> deleteGuard(@RequestParam(required = false) String vigilantId) {
        User admin = userService.findUserAuthenticated();

        Boolean isAdmin = adminService.isAdmin(admin);

        if (!isAdmin) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You are not allowed to perform this action");
        }

        if (vigilantId == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Vigilant id is required!");
        }

        try {
            UUID isValid = UUID.fromString(vigilantId);
        } catch (Exception e) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Invalid guard ID!");
        }

        User guard = userService.findUserByIdentifier(vigilantId);

        if (guard == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "User not found!");
        }

        Boolean isGuard = adminService.isGuard(guard);

        if (!isGuard) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "This user is not a guard!");
        }

        adminService.deleteGuard(guard);

        return GeneralResponse.getResponse(HttpStatus.OK, "Guard deleted successfully!");
    }

    @GetMapping("/getGuards")
    public ResponseEntity<GeneralResponse> getGuards() {
        User admin = userService.findUserAuthenticated();

        Boolean isAdmin = adminService.isAdmin(admin);

        if (!isAdmin) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You are not allowed to perform this action");
        }

        List<AllGuardsDTO> guards = adminService.getGuards();

        if (guards.isEmpty()) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "No guards found!");
        }

        return GeneralResponse.getResponse(HttpStatus.OK, guards);
    }

    @PostMapping("/addTerminal")
    public ResponseEntity<GeneralResponse> addTerminal (@RequestBody @Valid AddTerminalDTO info) {
        User admin = userService.findUserAuthenticated();

        Boolean isAdmin = adminService.isAdmin(admin);

        if (!isAdmin) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You are not allowed to perform this action");
        }

        User isUser = userService.findUserByEmail(info.getEmail());

        if (isUser == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "User not found!");
        }

        Boolean isGuard = adminService.isGuard(isUser);

        if (!isGuard) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "This user not is a guard!");
        }

        Boolean isTerminal = adminService.isTerminal(isUser);

        if (isTerminal) {
            Terminal isActive = adminService.getTerminalByUser(isUser);
            if (isActive.getIsTerminalActive()) {
                return GeneralResponse.getResponse(HttpStatus.CONFLICT, "This terminal is already active!");
            } else {
                adminService.changeState(isActive);
                return GeneralResponse.getResponse(HttpStatus.OK, "Terminal added successfully!");
            }
        }

        adminService.addGuard(isUser);
        adminService.addTerminal(isUser, info);

        return GeneralResponse.getResponse(HttpStatus.OK, "Terminal added successfully!");
    }

    @PatchMapping("/deleteTerminal")
    public ResponseEntity<GeneralResponse> deleteTerminal(@RequestParam(required = false) String terminalId) {
        User admin = userService.findUserAuthenticated();

        Boolean isAdmin = adminService.isAdmin(admin);

        if (!isAdmin) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You are not allowed to perform this action");
        }

        if (terminalId == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Terminal id is required!");
        }

        try {
            UUID isValid = UUID.fromString(terminalId);
        } catch (Exception e) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Invalid terminal ID!");
        }

        Terminal terminal = adminService.getTerminalByTerminalId(terminalId);
        if (terminal == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Terminal not found!");
        }

        Terminal isTerminal = adminService.getTerminalByUser(terminal.getUserTerminal());
        if (isTerminal == null) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "This user is not a terminal!");
        }
        
        adminService.deleteTerminal(isTerminal);

        return GeneralResponse.getResponse(HttpStatus.OK, "Terminal deleted successfully!");
    }

    @GetMapping("/getTerminals")
    public ResponseEntity<GeneralResponse> getTerminals() {
        User admin = userService.findUserAuthenticated();

        Boolean isAdmin = adminService.isAdmin(admin);

        if (!isAdmin) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You are not allowed to perform this action");
        }

        List<GetTerminalDTO> terminals = adminService.getTerminals();

        if (terminals.isEmpty()) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "No terminals found!");
        }

        return GeneralResponse.getResponse(HttpStatus.OK, terminals);
    }

    @PostMapping("addIP")
    public ResponseEntity<GeneralResponse> test(@RequestBody @Valid ServeDTO info) {
        User admin = userService.findUserAuthenticated();

        Boolean isAdmin = adminService.isAdmin(admin);

        if (!isAdmin) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You are not allowed to perform this action");
        }

        arduinoIP = info.getArduinoIP();

        return GeneralResponse.getResponse(HttpStatus.OK, "IP agregada satisfactoriamente");
    }
}
