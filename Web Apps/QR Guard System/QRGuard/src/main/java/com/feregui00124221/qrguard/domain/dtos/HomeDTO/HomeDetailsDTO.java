package com.feregui00124221.qrguard.domain.dtos.HomeDTO;

import com.feregui00124221.qrguard.domain.dtos.UserDTO.SimpleUserInfoDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HomeDetailsDTO {
    @NotEmpty
    private String houseNumber;

    @NotEmpty
    private SimpleUserInfoDTO inChargeResident;

    @NotEmpty
    private int houseCapacity;

    @NotEmpty
    private int currentOccupancy;

    @NotEmpty
    private List<SimpleUserInfoDTO> residents;
}
