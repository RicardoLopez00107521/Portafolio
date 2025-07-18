package com.feregui00124221.qrguard.services.implementations;

import com.feregui00124221.qrguard.domain.dtos.GuardDTO.ManualRegisterDTO;
import com.feregui00124221.qrguard.domain.entities.Entries.ManualRegisters;
import com.feregui00124221.qrguard.domain.entities.User;
import com.feregui00124221.qrguard.repositories.ManualRegisterRepository;
import com.feregui00124221.qrguard.services.GuardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ManualRegisterImpl implements GuardService {

    private final ManualRegisterRepository manualRegisterRepository;

    public ManualRegisterImpl(ManualRegisterRepository manualRegisterRepository) {
        this.manualRegisterRepository = manualRegisterRepository;
    }

    @Override
    public void manualRegister(ManualRegisterDTO info, User guard) {

        ManualRegisters manualRegisters = new ManualRegisters();
        manualRegisters.setName(info.getName());
        manualRegisters.setMotive(info.getMotive());
        manualRegisters.setDate(Date.from(Instant.now()));
        manualRegisters.setUser(guard);

        manualRegisterRepository.save(manualRegisters);
    }

    @Override
    public List<ManualRegisters> getManualRegisters() {
        return manualRegisterRepository.findAll();
    }
}
