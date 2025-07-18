package com.feregui00124221.qrguard.domain.entities;

import com.feregui00124221.qrguard.domain.entities.Permit.Permit;
import com.feregui00124221.qrguard.domain.entities.Permit.PermitState;
import jakarta.persistence.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_weekday")
public class Weekday {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String dayId;

    private LocalDate beginDate;

    private LocalDate endDate;

    private LocalTime beginTime;

    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private PermitState permitState;

    @ManyToOne
    @JoinColumn(name = "permit_date")
    private Permit permit_date;
}
