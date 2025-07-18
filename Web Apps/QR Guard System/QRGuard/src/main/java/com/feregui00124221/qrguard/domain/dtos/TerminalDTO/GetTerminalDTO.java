package com.feregui00124221.qrguard.domain.dtos.TerminalDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTerminalDTO {
    private String terminalName;
    private String terminalId;
    private String accessGate;
}
