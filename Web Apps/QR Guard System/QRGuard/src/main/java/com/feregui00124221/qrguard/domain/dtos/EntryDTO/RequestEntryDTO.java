package com.feregui00124221.qrguard.domain.dtos.EntryDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class RequestEntryDTO {
    @NotEmpty
    private String homeName;

    @NotNull
    private Instant qrTimestamp;

    @NotEmpty
    private String userEmail;
}
