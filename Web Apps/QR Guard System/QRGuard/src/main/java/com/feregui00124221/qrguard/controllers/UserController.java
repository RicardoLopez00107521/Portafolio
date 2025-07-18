package com.feregui00124221.qrguard.controllers;

import com.feregui00124221.qrguard.domain.dtos.CheckPermitDTO;
import com.feregui00124221.qrguard.domain.dtos.GeneralResponse;
import com.feregui00124221.qrguard.domain.dtos.LoginDTO;
import com.feregui00124221.qrguard.domain.dtos.CheckQrGenerationPermitDTO;
import com.feregui00124221.qrguard.domain.entities.User;
import com.feregui00124221.qrguard.services.UserService;
//import com.oracle.svm.core.annotate.Delete;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/check-qr-gen-permit")
    public ResponseEntity<GeneralResponse> checkQRPermit(@RequestBody @Valid CheckQrGenerationPermitDTO info) {
        return null;
    }

    @PostMapping("/change-qr-time")
    public ResponseEntity<GeneralResponse> changeQrTime(){
        return null;
    }

    @GetMapping("/get-vigilantes")
    public ResponseEntity<GeneralResponse> getVigilantes(){
        return null;
    }

    @PostMapping("/add-vigilante")
    public ResponseEntity<GeneralResponse> addVigilante(){
        return null;
    }

    @DeleteMapping("/remove-vigilante")
    public ResponseEntity<GeneralResponse> removeVigilante(){
        return null;
    }

    @GetMapping("/get-terminals")
    public ResponseEntity<GeneralResponse> getTerminals(){
        return null;
    }

    @PostMapping("/add-terminal")
    public ResponseEntity<GeneralResponse> addTerminal(){
        return null;
    }

    @DeleteMapping("/remove-terminal")
    public ResponseEntity<GeneralResponse> removeTerminal(){
        return null;
    }


}
