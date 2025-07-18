package com.feregui00124221.qrguard.repositories;

import com.feregui00124221.qrguard.domain.entities.Terminal.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TerminalRepository extends JpaRepository<Terminal, UUID> {
    Optional<Terminal> findByTerminalId(UUID terminalId);
}
