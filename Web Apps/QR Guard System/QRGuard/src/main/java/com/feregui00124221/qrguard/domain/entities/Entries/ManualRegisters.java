package com.feregui00124221.qrguard.domain.entities.Entries;

import com.feregui00124221.qrguard.domain.entities.Terminal.Terminal;
import com.feregui00124221.qrguard.domain.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_manual_registers")
public class ManualRegisters {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID manualRegisterId;

    private String name;

    private String motive;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_guard")
    private User user;
}
