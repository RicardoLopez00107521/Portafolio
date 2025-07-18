package com.feregui00124221.qrguard.services;

import com.feregui00124221.qrguard.domain.entities.Roles.Role;
import com.feregui00124221.qrguard.domain.entities.Roles.RoleCategory;
import com.feregui00124221.qrguard.domain.entities.User;

import java.util.List;

public interface RoleService {
    Role findRoleByRoleName(String roleName);
    List<Role> findRolesByRoleCategory(List<RoleCategory> roleCategories);
}
