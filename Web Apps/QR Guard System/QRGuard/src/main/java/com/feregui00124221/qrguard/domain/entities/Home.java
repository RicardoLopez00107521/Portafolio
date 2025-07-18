package com.feregui00124221.qrguard.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.feregui00124221.qrguard.domain.entities.Entries.Entries;
import com.feregui00124221.qrguard.domain.entities.Permit.Permit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_home")
public class Home {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID homeId;

    private String houseNumber;

    private int houseCapacity;

    // Home relationships

    // Permit relationships
    @OneToMany(mappedBy = "homeOfThePermit")
    @JsonIgnore
    private List<Permit> permits;

    // Entries relationships
    @OneToMany(mappedBy = "homeToAccess")
    @JsonIgnore
    private List<Entries> entries;

    // User relationships
    @ManyToMany
    @JoinTable(
            name = "tbl_home_x_user",
            joinColumns = @JoinColumn(name = "homeId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private List<User> residents;

    @ManyToOne
    @JoinColumn(name = "inChargeResident")
    private User inChargeResident;
}
