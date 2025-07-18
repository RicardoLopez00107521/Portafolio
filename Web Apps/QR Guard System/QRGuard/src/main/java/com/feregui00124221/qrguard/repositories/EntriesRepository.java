package com.feregui00124221.qrguard.repositories;

import com.feregui00124221.qrguard.domain.entities.Entries.Entries;
import com.feregui00124221.qrguard.domain.entities.Home;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntriesRepository extends JpaRepository<Entries, UUID> {
    Optional<List<Entries>> findAllByHomeToAccess(Home homeToAccess);
}
