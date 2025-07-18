package com.feregui00124221.qrguard.controllers;

import com.feregui00124221.qrguard.domain.dtos.EntryDTO.HouseEntriesDTO;
import com.feregui00124221.qrguard.domain.dtos.EntryDTO.RequestEntryDTO;
import com.feregui00124221.qrguard.domain.dtos.GeneralResponse;
import com.feregui00124221.qrguard.domain.dtos.PermitDTO.GetPermitsDTO;
import com.feregui00124221.qrguard.domain.entities.Entries.Entries;
import com.feregui00124221.qrguard.domain.entities.Home;
import com.feregui00124221.qrguard.domain.entities.Permit.EntryType;
import com.feregui00124221.qrguard.domain.entities.Permit.Permit;
import com.feregui00124221.qrguard.domain.entities.Permit.PermitGenerationType;
import com.feregui00124221.qrguard.domain.entities.Roles.RoleCategory;
import com.feregui00124221.qrguard.domain.entities.Terminal.Terminal;
import com.feregui00124221.qrguard.domain.entities.User;
import com.feregui00124221.qrguard.services.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/entries")
public class EntriesController {
    private final EntriesService entriesService;
    private final HomeService homeService;
    private final UserService userService;
    private final PermitService permitService;
    private final AdminController adminController;
    private final AdminService adminService;

    public EntriesController(EntriesService entriesService, HomeService homeService, UserService userService, PermitService permitService, AdminController adminController, AdminService adminService) {
        this.entriesService = entriesService;
        this.homeService = homeService;
        this.userService = userService;
        this.permitService = permitService;
        this.adminController = adminController;
        this.adminService = adminService;
    }

    @PostMapping("/request-entry")
    public ResponseEntity<GeneralResponse> requestEntry(@RequestBody @Valid RequestEntryDTO info) {
        User vigilant = userService.findUserAuthenticated();

        if (vigilant.getRoles()
                .stream()
                .noneMatch(role -> role.getRoleName().equals(RoleCategory.VIGILANT))) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You are not allowed to perform this action");
        }

        if (vigilant.getTerminal() == null) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "You must be linked to a terminal to perform this action");
        }

        Terminal usedTerminal = vigilant.getTerminal();
        Home homeToAccess = homeService.findHomeByHouseNumber(info.getHomeName());
        User enteringUser = userService.findUserByEmail(info.getUserEmail());

        if (homeToAccess == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Home does not exist");
        }

        if (enteringUser == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "User requesting entry does not exist in system");
        }

        if (!userService.isQrValid(enteringUser, info.getQrTimestamp())) {
            return GeneralResponse.getResponse(HttpStatus.UNAUTHORIZED, "QR code is no longer valid");
        }

        GetPermitsDTO permitData = permitService.getPermitsByGuestToday(enteringUser)
                .stream()
                .filter(p -> p.getHouseNumber().equals(homeToAccess.getHouseNumber()))
                .findFirst()
                .orElse(null);

        if (permitData == null) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "Guest is not authorized to enter");
        }

        Permit permitUsed = permitService.findPermitById(permitData.getPermitId());

        if (permitUsed == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "Permit not found");
        }

        String access = usedTerminal.getAccessCategory().toString();

        String gate = "hola";

        if (access.equals("VEHICLE_ACCESS")) {
            gate = "pluma";
        }
        else if (access.equals("PEDESTRIAN_ACCESS")) {
            gate = "puerta";
        }

        String respuesta = ejecutarAccionEnArduino("open", gate);

        if (respuesta == null || respuesta.startsWith("Error")) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "Error al conectarse con servo!"); // Si toda la validacion es correcta pero falla la conexion en lugar de registrar la entrada
        }

        entriesService.registerEntry(homeToAccess, enteringUser, usedTerminal, permitUsed, Instant.now());

        if (permitUsed.getEntryType().equals(EntryType.UNIQUE) && permitUsed.getPermitGenerationType().equals(PermitGenerationType.ONE_DAY)) {
            permitService.expirePermit(permitUsed, "1");
        }

        if (permitUsed.getEntryType().equals(EntryType.UNIQUE) && permitUsed.getPermitGenerationType().equals(PermitGenerationType.PERIODIC)) {
            permitService.expirePermit(permitUsed, "2");
        }

        return GeneralResponse.getResponse(HttpStatus.OK, "Entry registered successfully");
    }

    @GetMapping("/get-my-house-entries")
    public ResponseEntity<GeneralResponse> getEntries() {
        User incharge = userService.findUserAuthenticated();

        if (incharge.getRoles()
                .stream()
                .noneMatch(role -> role.getRoleName().equals(RoleCategory.IN_CHARGE_RESIDENT))) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You are not allowed to perform this action");
        }

        Home home = homeService.findHomeByHouseNumber(incharge.getLinkedHouses().getFirst().getHouseNumber());

        List<HouseEntriesDTO> entries = entriesService.getMyHouseEntries(home);

        if (entries.isEmpty()) {
            return GeneralResponse.getResponse(HttpStatus.NO_CONTENT, "No entries found");
        }

        return GeneralResponse.getResponse(HttpStatus.OK, entries);
    }

    @GetMapping("/get-all-entries")
    public ResponseEntity<GeneralResponse> getAllEntries() {
        User admin = userService.findUserAuthenticated();

        if (admin.getRoles()
                .stream()
                .noneMatch(role -> role.getRoleName().equals(RoleCategory.ADMIN))) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You are not allowed to perform this action");
        }

        return GeneralResponse.getResponse(HttpStatus.OK, "Residential entries retrieved successfully", entriesService.getResidentialEntries());
    }

    @GetMapping("/especial-format-entries")
    public ResponseEntity<GeneralResponse> getEspecialFormatEntries() {
        User user = userService.findUserAuthenticated();

        Boolean isAdmin = adminService.isAdmin(user);

        List<Integer> data = new ArrayList<>();

        if (!isAdmin) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You are not allowed to perform this action");
        }
        List<HouseEntriesDTO> entries = entriesService.getResidentialEntries();
        int allEntries = entries.size();
        data.add(allEntries);

        List<HouseEntriesDTO> lastEntries = entriesService.getMyHouseEntriesBefore10days();
        int lastEntriesInt = lastEntries.size();
        data.add(lastEntriesInt);

        return GeneralResponse.getResponse(HttpStatus.OK, data);
    }

    public String ejecutarAccionEnArduino(String command, String gate) {
        String url = adminController.arduinoIP + "/accionArduino";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String cuerpoSolicitud = String.format("{\"command\": \"%s\", \"deviceType\": \"%s\"}", command, gate);

        HttpEntity<String> entity = new HttpEntity<>(cuerpoSolicitud, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> respuesta;
        try {
            respuesta = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            return respuesta.getBody();
        } catch (Exception e) {
            return "Error al enviar solicitud al Arduino: " + e.getMessage();
        }
    }
}