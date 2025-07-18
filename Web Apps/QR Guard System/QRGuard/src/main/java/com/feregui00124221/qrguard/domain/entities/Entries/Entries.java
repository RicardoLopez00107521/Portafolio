package com.feregui00124221.qrguard.domain.entities.Entries;

import com.feregui00124221.qrguard.domain.entities.Home;
import com.feregui00124221.qrguard.domain.entities.Permit.Permit;
import com.feregui00124221.qrguard.domain.entities.Terminal.Terminal;
import com.feregui00124221.qrguard.domain.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_entries")
public class Entries {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID EntryId;

    private Instant entryTimestamp;

    // RELATIONSHIPS

    // Terminal relationships
    @ManyToOne
    @JoinColumn(name = "terminal_used")
    private Terminal terminal;

    // Home relationships
    @ManyToOne
    @JoinColumn(name = "home_to_access")
    private Home homeToAccess;

    // User relationships
    @ManyToOne
    @JoinColumn(name = "user_accessing")
    private User enteringUser;

    // Permit relationships
    @ManyToOne
    @JoinColumn(name = "permit_used")
    private Permit permitUsed;
}
