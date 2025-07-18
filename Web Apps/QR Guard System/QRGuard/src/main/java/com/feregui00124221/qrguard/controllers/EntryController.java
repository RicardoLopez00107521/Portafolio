package com.feregui00124221.qrguard.controllers;

import com.feregui00124221.qrguard.domain.dtos.GeneralResponse;
import com.feregui00124221.qrguard.domain.dtos.GuardDTO.ManualRegisterDTO;
import com.feregui00124221.qrguard.services.GuardService;
import com.feregui00124221.qrguard.services.PermitService;
import com.feregui00124221.qrguard.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/entry")
public class EntryController {

    private final PermitService permitService;
    private final UserService userService;
    private final GuardService guardService;

    public EntryController(PermitService permitService, GuardService guardService, UserService userService) {
        this.permitService = permitService;
        this.guardService = guardService;
        this.userService = userService;
    }

    /*@PostMapping("/requestPermit")
    public ResponseEntity<GeneralResponse> requestPermit(@RequestBody @Valid RequestPermitDTO info) {
        reLocalDate beginDate = permitService.convertDate(info.getBeginDate());
        LocalTime beginTime = permitService.convertTime(info.getBeginTime());

        if (!beginDate.isAfter(LocalDate.now())) {
            if (beginDate.equals(LocalDate.now())) {
                if (!beginTime.isAfter(LocalTime.now())) {
                    return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The begin time is in the past");
                }
            } else {
                return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The begin date is in the past");
            }
        }

        if(info.getEndDate() != null) {
            LocalDate endDate = permitService.convertDate(info.getEndDate());
            if (beginDate.isAfter(endDate)) {
                return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The end date is in the past");
            }
        }

        if(info.getEndTime() != null) {
            LocalTime endTime = permitService.convertTime(info.getEndTime());
            if (beginTime.equals(endTime)) {
                return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The begin time is the same end time");
            }
            if (beginTime.isAfter(endTime)) {
                return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The end time is in the past");
            }
        }

        User user = userService.findUser(info.getGuessEmail());

        if (user == null) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "The user does not exist");
        }
        //TODO: Verificar que el usuario no sea residente

        permitService.createPermit(info, user);

        return GeneralResponse.getResponse(HttpStatus.OK, "Permit Created Successfully");
    }*/

    @GetMapping("/getDates") //TODO: Eliminar es para apoyo
    public ResponseEntity<GeneralResponse> getDates() {
        LocalDate beginDate = permitService.convertDate("2024-06-01");
        LocalDate endDate = permitService.convertDate("2024-06-08");

        List<DayOfWeek> dates = new ArrayList<>();
        dates.add(DayOfWeek.valueOf("MONDAY"));
        dates.add(DayOfWeek.valueOf("WEDNESDAY"));
        dates.add(DayOfWeek.valueOf("SUNDAY"));

        LocalTime startTime = permitService.convertTime("20:30:01");
        System.out.println(startTime);

        List<String> permitsDates = permitService.getPermitDates(beginDate, endDate, dates);

        return GeneralResponse.getResponse(HttpStatus.OK, permitsDates);
    }
}
