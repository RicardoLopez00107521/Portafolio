package com.feregui00124221.qrguard.services;

import com.feregui00124221.qrguard.domain.dtos.AdminDTO.AddTerminalDTO;
import com.feregui00124221.qrguard.domain.dtos.GuardDTO.AllGuardsDTO;
import com.feregui00124221.qrguard.domain.dtos.TerminalDTO.GetTerminalDTO;
import com.feregui00124221.qrguard.domain.entities.Terminal.Terminal;
import com.feregui00124221.qrguard.domain.entities.User;

import java.util.List;

public interface AdminService {
    Boolean isGuard(User guard);
    Boolean isAdmin(User admin);
    void addGuard (User guard);
    void deleteGuard (User guard);
    List<AllGuardsDTO> getGuards();
    void addAnyRoles (User user);

    Boolean isTerminal(User terminal);
    Terminal getTerminalByUser(User terminal);
    Terminal getTerminalByTerminalId(String terminalId);
    void addTerminal(User terminal, AddTerminalDTO info);
    void changeState(Terminal Terminal);
    void deleteTerminal (Terminal terminal);
    List<GetTerminalDTO> getTerminals();
}
