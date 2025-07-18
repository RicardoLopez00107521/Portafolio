package com.feregui00124221.qrguard.services;

import com.feregui00124221.qrguard.domain.dtos.GuardDTO.ManualRegisterDTO;
import com.feregui00124221.qrguard.domain.entities.Entries.ManualRegisters;
import com.feregui00124221.qrguard.domain.entities.User;

import java.util.List;

public interface GuardService {

    void manualRegister (ManualRegisterDTO info, User guard);
    List<ManualRegisters> getManualRegisters();
}
