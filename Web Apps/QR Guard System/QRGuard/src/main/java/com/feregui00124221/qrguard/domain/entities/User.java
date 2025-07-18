package com.feregui00124221.qrguard.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.feregui00124221.qrguard.domain.entities.Entries.Entries;
import com.feregui00124221.qrguard.domain.entities.Entries.ManualRegisters;
import com.feregui00124221.qrguard.domain.entities.Permit.Permit;
import com.feregui00124221.qrguard.domain.entities.Roles.Role;
import com.feregui00124221.qrguard.domain.entities.Terminal.Terminal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    private String name;

    private String email;

    private Duration qrValidTime = Duration.ofMinutes(10);

    private boolean activeUser = true;

    // RELATIONSHIPS

    // Permit relationships
    @OneToMany(mappedBy = "issuingUser")
    @JsonIgnore
    private List<Permit> permitsIssued;

    @OneToMany(mappedBy = "targetUser")
    @JsonIgnore
    private List<Permit> permitsReceived;

    // Role relationships
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tbl_user_x_role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private List<Role> roles;

    // Entries relationships
    @OneToMany(mappedBy = "enteringUser")
    @JsonIgnore
    private List<Entries> entries;

    // Home relationships
    @ManyToMany(mappedBy = "residents", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Home> linkedHouses;

    @OneToMany(mappedBy = "inChargeResident", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Home> inChargeHouses;

    // ManualRegisters relationships
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<ManualRegisters> manualRegisters;

    @OneToOne(mappedBy = "userTerminal")
    @JsonIgnore
    private Terminal terminal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @OneToMany(mappedBy = "tokenUser", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
