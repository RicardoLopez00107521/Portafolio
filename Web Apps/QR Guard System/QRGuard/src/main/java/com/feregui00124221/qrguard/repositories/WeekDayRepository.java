package com.feregui00124221.qrguard.repositories;

import com.feregui00124221.qrguard.domain.entities.Weekday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WeekDayRepository extends JpaRepository<Weekday, UUID> {
    //Optional<Weekday> findWeekdayByPermit_date(String date);
}
