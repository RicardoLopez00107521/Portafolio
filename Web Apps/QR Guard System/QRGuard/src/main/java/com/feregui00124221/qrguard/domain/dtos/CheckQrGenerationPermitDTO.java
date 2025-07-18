package com.feregui00124221.qrguard.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CheckQrGenerationPermitDTO {
    @NotEmpty
    @Email
    private String email;
}
