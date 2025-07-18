package com.feregui00124221.qrguard.services.implementations;

import com.feregui00124221.qrguard.domain.entities.Roles.Role;
import com.feregui00124221.qrguard.domain.entities.Roles.RoleCategory;
import com.feregui00124221.qrguard.domain.entities.User;
import com.feregui00124221.qrguard.repositories.RoleRepository;
import com.feregui00124221.qrguard.repositories.UserRepository;
import com.feregui00124221.qrguard.services.RoleService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleServiceImpl(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        RoleCategory roleCategory = RoleCategory.valueOf(roleName);
        return roleRepository.findRoleByRoleName(roleCategory).orElse(null);
    }

    @Override
    public List<Role> findRolesByRoleCategory(List<RoleCategory> roleCategories) {
        List<Role> roles = new ArrayList<>();

        roleCategories.forEach(roleCategory -> {
            roles.add(roleRepository.findRoleByRoleName(roleCategory).orElse(null));
        });

        return roles;
    }
}
