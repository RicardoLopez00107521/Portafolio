package com.feregui00124221.qrguard.repositories;

import com.feregui00124221.qrguard.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>{
    Optional<User> findUserByUserIdAndActiveUserIsTrue(UUID userId);
    Optional<User> findUserByEmailAndActiveUserIsTrue(String email);
    Optional<User> findUserByActiveUserIsTrueAndEmailOrUserId(String email, UUID userId);
}
