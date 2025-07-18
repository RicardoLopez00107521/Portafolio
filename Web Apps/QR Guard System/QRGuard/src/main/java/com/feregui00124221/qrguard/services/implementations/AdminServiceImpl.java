package com.feregui00124221.qrguard.services.implementations;

import com.feregui00124221.qrguard.domain.dtos.AdminDTO.AddTerminalDTO;
import com.feregui00124221.qrguard.domain.dtos.GuardDTO.AllGuardsDTO;
import com.feregui00124221.qrguard.domain.dtos.TerminalDTO.GetTerminalDTO;
import com.feregui00124221.qrguard.domain.entities.Roles.RoleCategory;
import com.feregui00124221.qrguard.domain.entities.Terminal.AccessCategory;
import com.feregui00124221.qrguard.domain.entities.Terminal.Terminal;
import com.feregui00124221.qrguard.domain.entities.User;
import com.feregui00124221.qrguard.repositories.RoleRepository;
import com.feregui00124221.qrguard.repositories.TerminalRepository;
import com.feregui00124221.qrguard.repositories.UserRepository;
import com.feregui00124221.qrguard.services.AdminService;
import com.feregui00124221.qrguard.domain.entities.Roles.Role;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TerminalRepository terminalRepository;

    public AdminServiceImpl(RoleRepository roleRepository, UserRepository userRepository, TerminalRepository terminalRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.terminalRepository = terminalRepository;
    }

    @Override
    public Boolean isGuard(User guard) {
        List<Role> userRoles = guard.getRoles();

        for (Role role : userRoles) {
            if (role.getRoleName().equals(RoleCategory.VIGILANT)) {
                Role newRole = roleRepository.findAll().stream()
                        .filter(r -> r.getRoleName().equals(role.getRoleName()))
                        .findFirst()
                        .orElse(null);
                if (newRole != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Boolean isAdmin(User admin) {
        List<Role> userRoles = admin.getRoles();

        for (Role role : userRoles) {
            if (role.getRoleName().equals(RoleCategory.ADMIN)) {
                Role newRole = roleRepository.findAll().stream()
                        .filter(r -> r.getRoleName().equals(role.getRoleName()))
                        .findFirst()
                        .orElse(null);
                if (newRole != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void addGuard(User guard) {
        List<Role> userRoles = guard.getRoles();
        List<Role> deleteRoles = new ArrayList<>();

        userRoles.forEach(role -> {
            if (role.getRoleName().equals(RoleCategory.GUEST)) {
                deleteRoles.add(role);
            }
        });

        if (!deleteRoles.isEmpty()) { // Si elimina guest quita guest y asigna guard
            userRoles.remove(deleteRoles.getFirst());
            guard.setRoles(userRoles);
            userRepository.save(guard);

            Role newRole = roleRepository.findAll().stream().filter(role -> role.getRoleName().equals(RoleCategory.VIGILANT)).findFirst().orElse(null);
            userRoles.add(newRole);
            guard.setRoles(userRoles);
            userRepository.save(guard);
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteGuard(User guard) {
        List<Role> userRoles = guard.getRoles();
        List<Role> deleteRoles = new ArrayList<>();

        userRoles.forEach(role -> {
            if (role.getRoleName().equals(RoleCategory.VIGILANT)) {
                deleteRoles.add(role);
            }
        });

        if (!deleteRoles.isEmpty()) { // Si elimina guardia quita guardia y asigna User
            userRoles.remove(deleteRoles.getFirst());
            guard.setRoles(userRoles);
            userRepository.save(guard);

            Role newRole = roleRepository.findAll().stream().filter(role -> role.getRoleName().equals(RoleCategory.GUEST)).findFirst().orElse(null);
            userRoles.add(newRole);
            guard.setRoles(userRoles);
            userRepository.save(guard);
        }
    }

    @Override
    public List<AllGuardsDTO> getGuards() {
        List<AllGuardsDTO> allGuards = new ArrayList<>();

        userRepository.findAll()
                .forEach(user -> {
                    List<Role> roles = user.getRoles();
                    roles.forEach(role -> {
                        if (role.getRoleName().equals(RoleCategory.VIGILANT)) {
                            AllGuardsDTO info = new AllGuardsDTO();
                            info.setVigilantId(user.getUserId().toString());
                            info.setVigilantName(user.getName());
                            info.setVigilantEmail(user.getEmail());
                            allGuards.add(info);
                        }
                    });
                });

        return allGuards;
    }

    @Override
    public void addAnyRoles(User user) {
        List<Role> roles = user.getRoles();

        Role newRole = roleRepository.findAll().stream().filter(role -> role.getRoleName().equals(RoleCategory.NORMAL_RESIDENT)).findFirst().orElse(null);
        roles.add(newRole);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public Boolean isTerminal(User terminal) {
        List<Terminal> terminals = terminalRepository.findAll();

        for (Terminal terminal1 : terminals) {
            if (terminal1.getUserTerminal().getUserId().equals(terminal.getUserId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Terminal getTerminalByUser(User terminal) {
        List<Terminal> terminals = terminalRepository.findAll();

        for (Terminal terminal1 : terminals) {
            if (terminal1.getUserTerminal().getUserId().equals(terminal.getUserId())) {
                return terminal1;
            }
        }
        return null;
    }

    @Override
    public Terminal getTerminalByTerminalId(String terminalId) {
        UUID terminalUUID = UUID.fromString(terminalId);
        return terminalRepository.findByTerminalId(terminalUUID).orElse(null);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void addTerminal(User terminal, AddTerminalDTO info) {
        Terminal newTerminal = new Terminal();
        newTerminal.setAccessCategory(AccessCategory.valueOf(info.getTerminalAccess().toUpperCase()));
        newTerminal.setIsTerminalActive(true);
        newTerminal.setUserTerminal(terminal);
        terminalRepository.save(newTerminal);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void changeState(Terminal terminal) {
        terminal.setIsTerminalActive(true);
        terminalRepository.save(terminal);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteTerminal(Terminal terminal) {
        terminal.setIsTerminalActive(false);
        terminalRepository.save(terminal);
    }

    @Override
    public List<GetTerminalDTO> getTerminals() {
        return terminalRepository.findAll()
                .stream().filter(terminal -> terminal.getIsTerminalActive().equals(true))
                .map(terminal -> new GetTerminalDTO(terminal.getUserTerminal().getEmail(), terminal.getTerminalId().toString(), terminal.getAccessCategory().toString()))
                .collect(Collectors.toList());
    }
}
