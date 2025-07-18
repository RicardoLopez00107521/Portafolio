package com.feregui00124221.qrguard.domain.dtos.GuardDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ManualRegisterDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String motive;
}
