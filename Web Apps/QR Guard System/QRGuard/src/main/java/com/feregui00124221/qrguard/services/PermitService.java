package com.feregui00124221.qrguard.services;

import com.feregui00124221.qrguard.domain.dtos.PermitDTO.GetPermitsDTO;
import com.feregui00124221.qrguard.domain.dtos.PermitDTO.RequestPermitDTO;
import com.feregui00124221.qrguard.domain.entities.Home;
import com.feregui00124221.qrguard.domain.entities.Permit.Permit;
import com.feregui00124221.qrguard.domain.entities.User;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface PermitService {
    void expirePermit(Permit permit, String type);

    Permit findPermitById(UUID id);
    GetPermitsDTO findPermitByIdResponse(UUID id);

    /*****************************Time management: Time and Date converters, and calculateDates in base a date range*****************************/
    LocalDate convertDate (String date);
    LocalTime convertTime (String time);
    List<String> getPermitDates(LocalDate beginDate, LocalDate endDate, List<DayOfWeek> selectedWeekdays);
    /***************************Time management: Time and Date converters, and calculateDates in base a date range*****************************/

    void createPermit(RequestPermitDTO info, User guess, User resident, Home selectedHome);
    List<GetPermitsDTO> getPermits(List<String> homesByUser, String state);
    List<GetPermitsDTO> getPermitsByState(String state);

    void rejectPermit(Permit permit);
    void acceptPermit(Permit permit);

    List<Boolean> conflictedDate(Home targetHouse, LocalDate beginDates, User guest);
    List<Boolean> conflictedTime(Home targetHouse, RequestPermitDTO info);

    List<Boolean> conflictedDates(Home targetHouse, RequestPermitDTO info, User guest);

    List<GetPermitsDTO> getPermitsByGuestToday(User guest);

    boolean canGuestEnter(User user);
    List<Permit> getPermitsByUser(User user);
    List<Permit> getTodayActivePermitsByUser(User user);
}
