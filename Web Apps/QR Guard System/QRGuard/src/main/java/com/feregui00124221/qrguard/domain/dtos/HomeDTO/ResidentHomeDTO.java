package com.feregui00124221.qrguard.domain.dtos.HomeDTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResidentHomeDTO {
    @NotEmpty
    private String houseNumber;

    @NotEmpty
    private String userEmail;
}
