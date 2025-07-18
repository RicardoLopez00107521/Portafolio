package com.feregui00124221.qrguard.domain.dtos.AdminDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ServeDTO {
    @NotBlank
    @NotEmpty
    private String arduinoIP;
}
