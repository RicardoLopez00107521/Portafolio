package com.feregui00124221.qrguard.repositories;

import com.feregui00124221.qrguard.domain.entities.Token;
import com.feregui00124221.qrguard.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    Optional<List<Token>> findAllByTokenUserAndAndIsTokenActive(User user, boolean isTokenActive);
}
