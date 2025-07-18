package com.feregui00124221.qrguard.controllers;

import com.feregui00124221.qrguard.domain.dtos.GeneralResponse;
import com.feregui00124221.qrguard.domain.dtos.LoginDTO;
import com.feregui00124221.qrguard.domain.dtos.TokenDTO;
import com.feregui00124221.qrguard.domain.dtos.UserDTO.SimpleUserInfoDTO;
import com.feregui00124221.qrguard.domain.entities.Token;
import com.feregui00124221.qrguard.domain.entities.User;
import com.feregui00124221.qrguard.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    final
    UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<GeneralResponse> login(@RequestBody @Valid LoginDTO info) {
        User user = userService.findUserByEmail(info.getEmail());

        if (user == null) {
            userService.registerUser(info);
            User recentUser = userService.findUserByEmail(info.getEmail());
            Token token = userService.registerToken(recentUser);
            return GeneralResponse.getResponse(HttpStatus.OK, "Login successful", new TokenDTO(token));
        }

        Token token = userService.registerToken(user);
        return GeneralResponse.getResponse(HttpStatus.OK, "Login successful", new TokenDTO(token));
    }

    @GetMapping("/whoami")
    public ResponseEntity<GeneralResponse> whoami() {

        User user = userService.findUserAuthenticated();

        if (user == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "User not found");
        }

        SimpleUserInfoDTO userInfo = userService.userInfo(user);

        return GeneralResponse.getResponse(HttpStatus.OK, userInfo);
    }
}
