package com.feregui00124221.qrguard.domain.dtos.PermitDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPermitsDTO {

    private UUID permitId;
    private Date creationDate;
    private String entryType;
    private String permitGenerationType;
    private String state;
    private String resident;
    private String guess;
    private String houseNumber;

    private List<String> dates;
    private String beginHour;
    private String endHour;
}
