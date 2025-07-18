package com.feregui00124221.qrguard.domain.entities.Terminal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.feregui00124221.qrguard.domain.entities.Entries.Entries;
import com.feregui00124221.qrguard.domain.entities.Entries.ManualRegisters;
import com.feregui00124221.qrguard.domain.entities.User;
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
@Table(name = "tbl_terminal")
public class Terminal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID terminalId;

    //private String deviceName;

    @Enumerated(EnumType.STRING)
    private AccessCategory accessCategory;

    private Boolean isTerminalActive;

    // RELATIONSHIPS

    // Entries relationships
    @OneToMany(mappedBy = "terminal")
    @JsonIgnore
    private List<Entries> entries;

    @OneToOne
    @JoinColumn(name = "userId")
    private User userTerminal;
}
