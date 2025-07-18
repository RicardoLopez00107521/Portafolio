package com.feregui00124221.qrguard.domain.dtos.EntryDTO;

import com.feregui00124221.qrguard.domain.dtos.UserDTO.SimpleUserInfoDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class HouseEntriesDTO {
    @NotEmpty
    private String homeName;

    @NotNull
    private Instant entryTimestamp;

    @NotNull
    private String entryType;

    @NotNull
    private SimpleUserInfoDTO enteredBy;
}
