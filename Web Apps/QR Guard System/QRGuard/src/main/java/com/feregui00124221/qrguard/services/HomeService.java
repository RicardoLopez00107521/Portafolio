package com.feregui00124221.qrguard.services;

import com.feregui00124221.qrguard.domain.dtos.HomeDTO.HomeDetailsDTO;
import com.feregui00124221.qrguard.domain.dtos.HomeDTO.NewHomeDTO;
import com.feregui00124221.qrguard.domain.entities.Home;
import com.feregui00124221.qrguard.domain.entities.User;

import java.util.List;

public interface HomeService {
    Home findHomeById(String id);
    Home findHomeByHouseNumber(String houseNumber);
    void createHome(NewHomeDTO info);
    void linkNewResident(Home homeToLink, User newResident);
    void unlinkResident(Home homeToUnlink, User residentToUnlink);
    void assignInChargeResident(Home homeToAssign, User inChargeResident);
    HomeDetailsDTO getHomeDetails(Home homeToGetDetails);
    List<HomeDetailsDTO> getAllHomesDetails();
    void updateHomeCapacity(Home home, int newHouseCapacity);
}
