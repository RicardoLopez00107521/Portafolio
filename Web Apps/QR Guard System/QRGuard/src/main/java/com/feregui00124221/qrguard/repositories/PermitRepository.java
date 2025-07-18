package com.feregui00124221.qrguard.repositories;

import com.feregui00124221.qrguard.domain.entities.Permit.Permit;
import com.feregui00124221.qrguard.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermitRepository extends JpaRepository<Permit, UUID>{
    boolean existsPermitByTargetUserEqualsAndPermitStateIsTrue(User targetUser);

    Optional<List<Permit>> findPermitsByTargetUser(User targetUser);
}
