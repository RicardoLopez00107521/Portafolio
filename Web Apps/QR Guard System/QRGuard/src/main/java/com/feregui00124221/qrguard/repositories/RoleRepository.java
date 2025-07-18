package com.feregui00124221.qrguard.repositories;

import com.feregui00124221.qrguard.domain.entities.Roles.Role;
import com.feregui00124221.qrguard.domain.entities.Roles.RoleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String>{
    List<Role> findRolesByRoleName(String roleName);

    Optional<Role> findRoleByRoleName(RoleCategory roleName);
}
