package com.feregui00124221.qrguard.domain.dtos.AdminDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AddTerminalDTO {
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^(VEHICLE|PEDESTRIAN)_ACCESS$", message = "Tipo de acceso no válido")
    private String terminalAccess;
}
