package com.feregui00124221.qrguard.domain.dtos.GuardDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllGuardsDTO {
    private String vigilantId;
    private String vigilantName;
    private String vigilantEmail;
}
