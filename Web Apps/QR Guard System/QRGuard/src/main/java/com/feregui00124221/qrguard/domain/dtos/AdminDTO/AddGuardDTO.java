package com.feregui00124221.qrguard.domain.dtos.AdminDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddGuardDTO {
    @NotBlank
    @Email
    private String guardEmail;
}
