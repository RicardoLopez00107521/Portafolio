package com.feregui00124221.qrguard.services;

import com.feregui00124221.qrguard.domain.dtos.EntryDTO.HouseEntriesDTO;
import com.feregui00124221.qrguard.domain.entities.Home;
import com.feregui00124221.qrguard.domain.entities.Permit.Permit;
import com.feregui00124221.qrguard.domain.entities.Terminal.Terminal;
import com.feregui00124221.qrguard.domain.entities.User;

import java.time.Instant;
import java.util.List;

public interface EntriesService {
    List<HouseEntriesDTO> getMyHouseEntries(Home house);
    List<HouseEntriesDTO> getResidentialEntries();
    void registerEntry(Home home, User user, Terminal terminal, Permit permit, Instant entryTimestamp);
    List<HouseEntriesDTO> getMyHouseEntriesBefore10days();
}
