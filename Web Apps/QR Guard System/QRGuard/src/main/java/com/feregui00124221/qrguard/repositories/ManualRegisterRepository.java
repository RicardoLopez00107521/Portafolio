package com.feregui00124221.qrguard.repositories;

import com.feregui00124221.qrguard.domain.entities.Entries.ManualRegisters;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ManualRegisterRepository extends JpaRepository<ManualRegisters, UUID> {
}
