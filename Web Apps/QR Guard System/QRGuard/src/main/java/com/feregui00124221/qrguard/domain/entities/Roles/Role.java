package com.feregui00124221.qrguard.domain.entities.Roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.feregui00124221.qrguard.domain.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_role")
public class Role {
    @Id
    private String roleId;

    @Enumerated(EnumType.STRING)
    private RoleCategory roleName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tbl_user_role",
            joinColumns = @JoinColumn(name = "roleCode"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    @JsonIgnore
    private List<User> roleUsers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(roleId, role.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId);
    }
}