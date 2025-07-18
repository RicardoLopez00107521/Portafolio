package com.feregui00124221.qrguard.services.implementations;

import com.feregui00124221.qrguard.domain.dtos.EntryDTO.HouseEntriesDTO;
import com.feregui00124221.qrguard.domain.dtos.UserDTO.SimpleUserInfoDTO;
import com.feregui00124221.qrguard.domain.entities.Entries.Entries;
import com.feregui00124221.qrguard.domain.entities.Home;
import com.feregui00124221.qrguard.domain.entities.Permit.Permit;
import com.feregui00124221.qrguard.domain.entities.Roles.Role;
import com.feregui00124221.qrguard.domain.entities.Terminal.Terminal;
import com.feregui00124221.qrguard.domain.entities.User;
import com.feregui00124221.qrguard.repositories.EntriesRepository;
import com.feregui00124221.qrguard.repositories.HomeRepository;
import com.feregui00124221.qrguard.services.EntriesService;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class EntriesServiceImpl implements EntriesService {

    private final EntriesRepository entriesRepository;
    private final HomeRepository homeRepository;

    public EntriesServiceImpl(EntriesRepository entriesRepository, HomeRepository homeRepository) {
        this.entriesRepository = entriesRepository;
        this.homeRepository = homeRepository;
    }

    @Override
    public List<HouseEntriesDTO> getMyHouseEntries(Home home) {
        List<Entries> entries = entriesRepository.findAllByHomeToAccess(home).orElse(null);

        if (entries == null) {
            return null;
        }

        return entries
                .stream()
                .map(entry -> {
                    HouseEntriesDTO myHouseEntry = new HouseEntriesDTO();
                    myHouseEntry.setHomeName(entry.getHomeToAccess().getHouseNumber());
                    myHouseEntry.setEntryTimestamp(entry.getEntryTimestamp());
                    myHouseEntry.setEntryType(
                            (entry.getPermitUsed() == null)
                                    ?
                                    "Non defined entry type"
                                    :
                                    String.valueOf(entry.getPermitUsed().getEntryType())
                    );
                    myHouseEntry.setEnteredBy(
                            SimpleUserInfoDTO
                                    .builder()
                                    .userEmail(entry.getEnteringUser().getEmail())
                                    .userName(entry.getEnteringUser().getName())
                                    .userRoles(null)
                                    .build()
                    );
                    return myHouseEntry;
                })
                .toList();
    }

    @Override
    public List<HouseEntriesDTO> getResidentialEntries() {
        return entriesRepository
                .findAll()
                .stream()
                .map(entry -> {
                    HouseEntriesDTO myHouseEntry = new HouseEntriesDTO();
                    myHouseEntry.setHomeName(entry.getHomeToAccess().getHouseNumber());
                    myHouseEntry.setEntryTimestamp(entry.getEntryTimestamp());
                    myHouseEntry.setEnteredBy(
                            SimpleUserInfoDTO
                                    .builder()
                                    .userEmail(entry.getEnteringUser().getEmail())
                                    .userName(entry.getEnteringUser().getName())
                                    .userRoles(
                                            entry.getEnteringUser()
                                                    .getRoles()
                                                    .stream()
                                                    .map(Role::getRoleName)
                                                    .toList()
                                    )
                                    .build()
                    );
                    return myHouseEntry;
                })
                .sorted(Comparator.comparing(HouseEntriesDTO::getEntryTimestamp))
                .toList();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void registerEntry(Home home, User user, Terminal terminal, Permit permit, Instant entryTimestamp) {
        Entries entry = Entries
                .builder()
                .homeToAccess(home)
                .enteringUser(user)
                .terminal(terminal)
                .permitUsed(permit)
                .entryTimestamp(entryTimestamp)
                .build();

        entriesRepository.save(entry);
        entriesRepository.flush();
    }

    @Override
    public List<HouseEntriesDTO> getMyHouseEntriesBefore10days() {
        List<Entries> response = new ArrayList<>();
        List<Entries> allEntries = entriesRepository.findAll();

        Instant thisInstant = Instant.now();
        Instant tenDaysPast = thisInstant.minus(10, ChronoUnit.DAYS);

        for (Entries entry : allEntries) {
            if (entry.getEntryTimestamp().equals(tenDaysPast) || entry.getEntryTimestamp().equals(thisInstant)) {
                response.add(entry);
            }
            if (entry.getEntryTimestamp().isAfter(tenDaysPast) && entry.getEntryTimestamp().isBefore(thisInstant)) {
                response.add(entry);
            }
        }

        if (response.isEmpty()) {
            return null;
        }

        return response
                .stream()
                .map(entry -> {
                    HouseEntriesDTO myHouseEntry = new HouseEntriesDTO();
                    myHouseEntry.setHomeName(entry.getHomeToAccess().getHouseNumber());
                    myHouseEntry.setEntryTimestamp(entry.getEntryTimestamp());
                    myHouseEntry.setEntryType(
                            (entry.getPermitUsed() == null)
                                    ?
                                    "Non defined entry type"
                                    :
                                    String.valueOf(entry.getPermitUsed().getEntryType())
                    );
                    myHouseEntry.setEnteredBy(
                            SimpleUserInfoDTO
                                    .builder()
                                    .userEmail(entry.getEnteringUser().getEmail())
                                    .userName(entry.getEnteringUser().getName())
                                    .userRoles(null)
                                    .build()
                    );
                    return myHouseEntry;
                })
                .toList();
    }
}
