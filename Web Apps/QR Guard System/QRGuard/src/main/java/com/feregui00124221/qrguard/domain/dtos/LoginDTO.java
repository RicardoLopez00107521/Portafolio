package com.feregui00124221.qrguard.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;
}