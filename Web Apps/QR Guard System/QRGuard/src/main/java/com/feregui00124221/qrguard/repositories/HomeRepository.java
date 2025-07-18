package com.feregui00124221.qrguard.repositories;

import com.feregui00124221.qrguard.domain.entities.Home;
import com.feregui00124221.qrguard.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HomeRepository extends JpaRepository<Home, UUID> {
    Optional<Home> findHomeByHomeId(UUID homeId);
    Optional<Home> findHomeByHouseNumber(String houseNumber);
    Optional<List<Home>> findHomesByInChargeResident(User inChargeResident);
}
