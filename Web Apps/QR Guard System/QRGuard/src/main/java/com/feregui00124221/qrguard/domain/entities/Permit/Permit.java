package com.feregui00124221.qrguard.domain.entities.Permit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.feregui00124221.qrguard.domain.entities.Entries.Entries;
import com.feregui00124221.qrguard.domain.entities.Home;
import com.feregui00124221.qrguard.domain.entities.User;
import com.feregui00124221.qrguard.domain.entities.Weekday;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_permit")
public class Permit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID permitId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTimestamp;

    @Enumerated(EnumType.STRING)
    private PermitState permitState;

    // Permit time configuration

    /*@Temporal(TemporalType.DATE)
    private LocalDate startDate;

    @Temporal(TemporalType.DATE)
    private LocalDate endDate;

    private LocalTime startTime;

    private LocalTime endTime;*/

    @Enumerated(EnumType.STRING)
    private EntryType entryType;

    @Enumerated(EnumType.STRING)
    private PermitGenerationType permitGenerationType;

    // Permit relationships

    // User relationships
    @ManyToOne
    @JoinColumn(name = "issuingUser")
    private User issuingUser;

    @ManyToOne
    @JoinColumn(name = "targetUser")
    private User targetUser;

    // Home relationships
    @ManyToOne
    @JoinColumn(name = "permit_of_home")
    private Home homeOfThePermit;

    @OneToMany(mappedBy = "permit_date")
    @JsonIgnore
    private List<Weekday> dates;

    @OneToMany(mappedBy = "permitUsed")
    @JsonIgnore
    private List<Entries> entries;

    // Weekday relationships
    /*@ManyToMany
    @JoinTable(
            name = "tbl_permit_x_weekday",
            joinColumns = @JoinColumn(name = "permitId"),
            inverseJoinColumns = @JoinColumn(name = "dayId")
    )
    private List<Weekday> weekdays;*/
}
