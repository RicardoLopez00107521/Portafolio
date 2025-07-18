package com.feregui00124221.qrguard.domain.dtos.HomeDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateHomeCapacityDTO {
    @NotNull
    private int newHouseCapacity;

    @NotEmpty
    private String houseNumber;
}
