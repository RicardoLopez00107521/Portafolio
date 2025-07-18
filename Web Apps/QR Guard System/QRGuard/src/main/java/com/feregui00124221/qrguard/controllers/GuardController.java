package com.feregui00124221.qrguard.controllers;

import com.feregui00124221.qrguard.domain.dtos.GeneralResponse;
import com.feregui00124221.qrguard.domain.dtos.GuardDTO.ManualRegisterDTO;
import com.feregui00124221.qrguard.domain.entities.Entries.ManualRegisters;
import com.feregui00124221.qrguard.domain.entities.User;
import com.feregui00124221.qrguard.services.AdminService;
import com.feregui00124221.qrguard.services.EntriesService;
import com.feregui00124221.qrguard.services.GuardService;
import com.feregui00124221.qrguard.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.apache.coyote.http11.Constants.a;

//TODO: Eliminar esta seccion
@RestController
@RequestMapping("/api/guard")
public class GuardController {

    private final AdminController adminController;

    private final GuardService guardService;

    private final UserService userService;

    private final AdminService adminService;

    public GuardController(GuardService guardService, EntriesController entriesController, AdminController adminController, UserService userService, AdminService adminService) {
        this.guardService = guardService;
        this.adminController = adminController;
        this.userService = userService;
        this.adminService = adminService;
    }

    @PostMapping("/manualEntryRegister")
    public ResponseEntity<GeneralResponse> manualRegister (@RequestBody @Valid ManualRegisterDTO info) {

        User user = userService.findUserAuthenticated();

        Boolean isGuard = adminService.isGuard(user);

        if (!isGuard) {
            return GeneralResponse.getResponse(HttpStatus.FORBIDDEN, "You are not a guard!");
        }

        String response = ejecutarAccionEnArduino("open", "pluma");

        if (response == null || response.startsWith("Error")) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "Error al conectarse con servo!"); // Si toda la validacion es correcta pero falla la conexion en lugar de registrar la entrada
        }

        guardService.manualRegister(info, user);

        return GeneralResponse.getResponse(HttpStatus.OK, "Entry registered successfully");
    }

    @GetMapping("/getManualEntryRegisters")
    public ResponseEntity<GeneralResponse> getManualEntryRegisters() {
        List<ManualRegisters> manualRegisters = guardService.getManualRegisters();

        if (manualRegisters.isEmpty()) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "No manual registers found");
        }

        return GeneralResponse.getResponse(HttpStatus.OK, manualRegisters);
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
