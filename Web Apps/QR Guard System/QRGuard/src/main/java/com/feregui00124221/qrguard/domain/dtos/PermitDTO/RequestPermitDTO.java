package com.feregui00124221.qrguard.domain.dtos.PermitDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.DayOfWeek;
import java.util.List;

@Data
public class RequestPermitDTO {
    @NotBlank
    private String beginDate;

    // Can be null for one-day permit
    private String endDate;

    @NotBlank
    private String beginTime;

    // Can be null for unique permit, but the system will assign an endTime in base of the valid qr duration
    private String endTime;

    // Can be null for one-day permit
    private List<String> days;

    @NotBlank
    @Pattern(regexp = "UNIQUE|MULTIPLE", message = "El tipo de entrada debe ser UNIQUE o MULTIPLE")
    private String entryType;

    @NotBlank
    @Pattern(regexp = "PERIODIC|ONE_DAY", message = "El tipo de entrada debe ser PERIODIC o ONE_DAY")
    private String permitGenerationType;

    @NotBlank
    @Email
    private String guessEmail;

    @NotBlank
    private String targetHouse;
}
