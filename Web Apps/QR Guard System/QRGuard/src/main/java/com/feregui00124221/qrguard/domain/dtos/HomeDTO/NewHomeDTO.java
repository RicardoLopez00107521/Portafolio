package com.feregui00124221.qrguard.domain.dtos.HomeDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewHomeDTO {
    @NotEmpty
    private String houseNumber;

    @NotNull
    private int houseCapacity;
}
