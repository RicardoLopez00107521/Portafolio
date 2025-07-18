package com.feregui00124221.qrguard.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID tokenId;

    private String content;

    @Column(name = "timestamp", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date generatedTimestamp;

    private Boolean isTokenActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User tokenUser;

    public Token(String content, User tokenUser) { // Asignara al token su contenido, duracion y lo activara
        super();
        this.content = content;
        this.tokenUser = tokenUser;
        this.generatedTimestamp = Date.from(Instant.now());
        this.isTokenActive = true;
    }
}
