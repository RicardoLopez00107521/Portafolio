package com.feregui00124221.qrguard.services.implementations;

import com.feregui00124221.qrguard.domain.dtos.PermitDTO.GetPermitsDTO;
import com.feregui00124221.qrguard.domain.dtos.PermitDTO.RequestPermitDTO;
import com.feregui00124221.qrguard.domain.entities.Home;
import com.feregui00124221.qrguard.domain.entities.Permit.EntryType;
import com.feregui00124221.qrguard.domain.entities.Permit.Permit;
import com.feregui00124221.qrguard.domain.entities.Permit.PermitGenerationType;
import com.feregui00124221.qrguard.domain.entities.Permit.PermitState;
import com.feregui00124221.qrguard.domain.entities.Roles.Role;
import com.feregui00124221.qrguard.domain.entities.Roles.RoleCategory;
import com.feregui00124221.qrguard.domain.entities.User;
import com.feregui00124221.qrguard.domain.entities.Weekday;
import com.feregui00124221.qrguard.repositories.PermitRepository;
import com.feregui00124221.qrguard.repositories.WeekDayRepository;
import com.feregui00124221.qrguard.services.PermitService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.util.pattern.PatternParseException;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.feregui00124221.qrguard.domain.entities.Permit.PermitState.APPROVED;
import static com.feregui00124221.qrguard.domain.entities.Permit.PermitState.PENDING;

@Service
public class PermitServiceImpl implements PermitService {

    private final PermitRepository permitRepository;

    private final WeekDayRepository weekDayRepository;
    private final UserServiceImplementation userServiceImplementation;

    public PermitServiceImpl(PermitRepository permitRepository, WeekDayRepository weekDayRepository, UserServiceImplementation userServiceImplementation) {
        this.permitRepository = permitRepository;
        this.weekDayRepository = weekDayRepository;
        this.userServiceImplementation = userServiceImplementation;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void expirePermit(Permit permit, String type) {
        if (type.equals("1")) {
            permit.setPermitState(PermitState.EXPIRED);
            permitRepository.save(permit);
            permitRepository.flush();

            List<Weekday> weekdays = weekDayRepository.findAll().stream()
                    .filter(weekday -> weekday.getPermit_date().getPermitId().equals(permit.getPermitId()))
                    .toList();

            for (Weekday weekday : weekdays) {
                weekday.setPermitState(PermitState.EXPIRED);
                weekDayRepository.save(weekday);
            }
        } else if (type.equals("2")) {
            List<Weekday> weekdays = weekDayRepository.findAll().stream()
                    .filter(weekday -> weekday.getPermit_date().getPermitId().equals(permit.getPermitId()))
                    .toList();

            for (Weekday weekday : weekdays) {
                if (weekday.getBeginDate().equals(LocalDate.now())) {
                    weekday.setPermitState(PermitState.EXPIRED);
                    weekDayRepository.save(weekday);
                }
            }
        }

    }

    @Override
    public Permit findPermitById(UUID id) {
        return permitRepository.findById(id).orElse(null);
    }

    @Override
    public GetPermitsDTO findPermitByIdResponse(UUID id) {
        GetPermitsDTO info = new GetPermitsDTO();
        permitRepository.findAll()
                .stream()
                .filter(permit -> permit.getPermitId().equals(id))
                .forEach(permit -> {
                    info.setPermitId(permit.getPermitId());
                    info.setCreationDate(permit.getCreationTimestamp());
                    info.setEntryType(permit.getEntryType().toString());
                    info.setPermitGenerationType(permit.getPermitGenerationType().toString());
                    info.setState(permit.getPermitState().toString());
                    //TODO: Obtener solicitante
                    info.setGuess(permit.getTargetUser().getEmail());
                    List<String> dates = new ArrayList<>();

                    weekDayRepository.findAll().stream().filter(weekday -> weekday.getPermit_date().getPermitId().equals(permit.getPermitId()))
                            .forEach(weekday -> {
                                dates.add(weekday.getBeginDate().toString());
                                info.setDates(dates);
                                info.setBeginHour(weekday.getBeginTime().toString());
                                info.setEndHour( weekday.getEndTime().toString());
                            });
                });
        return info;
    }

    @Override
    public LocalDate convertDate(String date) {
        //TODO: Que no devuelva error 500
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (PatternParseException e) {
            System.out.println("Error converting date " + date);
            return null;
        }
    }

    @Override
    public LocalTime convertTime(String time) {
        //TODO: Que no devuelva error 500
        try {
            return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
        } catch (PatternParseException e) {
            System.out.println("Error converting time " + time);
            return null;
        }
    }

    @Override
    public List<String> getPermitDates(LocalDate beginDate, LocalDate endDate, List<DayOfWeek> selectedWeekdays) {
        List<String> permitDates = new ArrayList<>();

        while (!beginDate.isAfter(endDate)) {

            if (selectedWeekdays.contains(beginDate.getDayOfWeek())) {
                permitDates.add(beginDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            beginDate = beginDate.plusDays(1);
        }

        return permitDates;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createPermit(RequestPermitDTO info, User guess, User resident, Home selectedHome) {
        LocalDate beginDate = convertDate(info.getBeginDate());

        LocalTime toNextDay = convertTime("23:29:59");
        LocalTime beginTime = convertTime(info.getBeginTime());

        Boolean isResident = userServiceImplementation.isResident(guess);

        if (isResident) {
            Permit permit = new Permit();
            permit.setCreationTimestamp(Date.from(Instant.now()));
            permit.setPermitState(APPROVED);
            permit.setEntryType(EntryType.UNIQUE);
            permit.setPermitGenerationType(PermitGenerationType.ONE_DAY);
            permit.setIssuingUser(resident);
            permit.setTargetUser(guess);
            permit.setHomeOfThePermit(selectedHome);
            permitRepository.save(permit);

            Weekday weekday = new Weekday();

            if (beginTime.isAfter(toNextDay)) {
                weekday.setBeginDate(beginDate);
                weekday.setEndDate(beginDate.plusDays(1));
                weekday.setBeginTime(beginTime.minusMinutes(30));
                weekday.setEndTime(beginTime.plusMinutes(30));
                weekday.setPermit_date(permit);
                weekday.setPermitState(APPROVED);
                weekDayRepository.save(weekday);
            } else {
                weekday.setBeginDate(beginDate);
                weekday.setEndDate(beginDate);
                weekday.setBeginTime(beginTime.minusMinutes(30));
                weekday.setEndTime(beginTime.plusMinutes(30));
                weekday.setPermit_date(permit);
                weekday.setPermitState(APPROVED);
                weekDayRepository.save(weekday);
            }
        }

        if (info.getEntryType().equals("UNIQUE") && info.getPermitGenerationType().equals("ONE_DAY")) {
            Permit permit = new Permit();
            permit.setCreationTimestamp(Date.from(Instant.now()));
            permit.setPermitState(PENDING);
            permit.setEntryType(EntryType.UNIQUE);
            permit.setPermitGenerationType(PermitGenerationType.ONE_DAY);
            permit.setIssuingUser(resident);
            permit.setTargetUser(guess);
            permit.setHomeOfThePermit(selectedHome);
            permitRepository.save(permit);

            Weekday weekday = new Weekday();

            if (beginTime.isAfter(toNextDay)) {
                weekday.setBeginDate(beginDate);
                weekday.setEndDate(beginDate.plusDays(1));
                weekday.setBeginTime(beginTime.minusMinutes(30));
                weekday.setEndTime(beginTime.plusMinutes(30));
                weekday.setPermit_date(permit);
                weekDayRepository.save(weekday);
            } else {
                weekday.setBeginDate(beginDate);
                weekday.setEndDate(beginDate);
                weekday.setBeginTime(beginTime.minusMinutes(30));
                weekday.setEndTime(beginTime.plusMinutes(30));
                weekday.setPermit_date(permit);
                weekDayRepository.save(weekday);
            }


        } else if (info.getEntryType().equals("MULTIPLE") && info.getPermitGenerationType().equals("ONE_DAY")) {
            LocalTime endTime = convertTime(info.getEndTime());

            Permit permit = new Permit();
            permit.setCreationTimestamp(Date.from(Instant.now()));
            permit.setPermitState(PENDING);
            permit.setEntryType(EntryType.MULTIPLE);
            permit.setPermitGenerationType(PermitGenerationType.ONE_DAY);
            permit.setIssuingUser(resident);
            permit.setTargetUser(guess);
            permit.setHomeOfThePermit(selectedHome);
            permitRepository.save(permit);

            Weekday weekday = new Weekday();
            weekday.setBeginDate(beginDate);
            weekday.setEndDate(beginDate);
            weekday.setBeginTime(beginTime);
            weekday.setEndTime(endTime);
            weekday.setPermit_date(permit);
            weekDayRepository.save(weekday);

        } else if (info.getEntryType().equals("UNIQUE") && info.getPermitGenerationType().equals("PERIODIC")) {

            Permit permit = new Permit();
            permit.setCreationTimestamp(Date.from(Instant.now()));
            permit.setPermitState(PENDING);
            permit.setEntryType(EntryType.UNIQUE);
            permit.setPermitGenerationType(PermitGenerationType.PERIODIC);
            permit.setIssuingUser(resident);
            permit.setTargetUser(guess);
            permit.setHomeOfThePermit(selectedHome);
            permitRepository.save(permit);

            LocalDate endDate = convertDate(info.getEndDate());

            List<DayOfWeek> days = info.getDays().stream()
                    .map(day -> DayOfWeek.valueOf(day.toUpperCase()))
                    .toList();
            List<String> dates = getPermitDates(beginDate, endDate, days);
            System.out.println(dates);

            if (!dates.isEmpty()) {
                for (String date : dates) {
                    Weekday weekday = new Weekday();

                    if (beginTime.isAfter(toNextDay)) {
                        weekday.setBeginDate(beginDate);
                        weekday.setEndDate(beginDate.plusDays(1));
                        weekday.setBeginTime(beginTime.minusMinutes(30));
                        weekday.setEndTime(beginTime.plusMinutes(30));
                        weekday.setPermit_date(permit);
                        weekDayRepository.save(weekday);
                    } else {
                        weekday.setBeginDate(convertDate(date));
                        weekday.setEndDate(convertDate(date));
                        weekday.setBeginTime(beginTime.minusMinutes(30));
                        weekday.setEndTime(beginTime.plusMinutes(30));
                        weekday.setPermit_date(permit);
                        weekDayRepository.save(weekday);
                    }
                }
            }

        } else if (info.getEntryType().equals("MULTIPLE") && info.getPermitGenerationType().equals("PERIODIC")) {
            System.out.println("Entro");
            LocalDate endDate = convertDate(info.getEndDate());
            LocalTime endTime = convertTime(info.getEndTime());

            List<DayOfWeek> days = info.getDays().stream()
                    .map(day -> DayOfWeek.valueOf(day.toUpperCase()))
                    .toList();
            List<String> dates = getPermitDates(beginDate, endDate, days);

            Permit permit = new Permit();
            permit.setCreationTimestamp(Date.from(Instant.now()));
            permit.setPermitState(PENDING);
            permit.setEntryType(EntryType.MULTIPLE);
            permit.setPermitGenerationType(PermitGenerationType.PERIODIC);
            permit.setIssuingUser(resident);
            permit.setTargetUser(guess);
            permit.setHomeOfThePermit(selectedHome);
            permitRepository.save(permit);

            for (String date : dates) {
                Weekday weekday = new Weekday();
                weekday.setBeginDate(convertDate(date));
                weekday.setEndDate(convertDate(date));
                weekday.setBeginTime(beginTime);
                weekday.setEndTime(endTime);
                weekday.setPermit_date(permit);
                weekDayRepository.save(weekday);
            }
        }
    }

    @Override
    public List<GetPermitsDTO> getPermits(List<String> homesByUser, String state) {
        List<GetPermitsDTO> response = new ArrayList<>(); // Lista acumulativa para almacenar todos los permisos

        homesByUser.forEach(homesByUserName -> {
            List<GetPermitsDTO> permits = new ArrayList<>();

            permitRepository.findAll()
                    .stream()
                    .filter(permit -> permit.getPermitState().toString().equals(state))
                    .filter(permit -> permit.getHomeOfThePermit().getHouseNumber().equals(homesByUserName))
                    .forEach(permit -> {

                        List<Role> roles = permit.getTargetUser().getRoles();
                        boolean isResident = false;

                        for (Role role : roles) {
                            if (role.getRoleName().equals(RoleCategory.IN_CHARGE_RESIDENT) || role.getRoleName().equals(RoleCategory.NORMAL_RESIDENT)) {
                                isResident = true;
                            }
                        }

                        if (!isResident) {
                            GetPermitsDTO info = new GetPermitsDTO();
                            info.setPermitId(permit.getPermitId());
                            info.setCreationDate(permit.getCreationTimestamp());
                            info.setEntryType(permit.getEntryType().toString());
                            info.setPermitGenerationType(permit.getPermitGenerationType().toString());
                            info.setState(permit.getPermitState().toString());
                            info.setResident(permit.getIssuingUser().getName());
                            info.setGuess(permit.getTargetUser().getName());
                            info.setHouseNumber(permit.getHomeOfThePermit().getHouseNumber());
                            List<String> dates = new ArrayList<>();

                            weekDayRepository.findAll().stream().filter(weekday -> weekday.getPermit_date().getPermitId().equals(permit.getPermitId()))
                                    .forEach(weekday -> {
                                        dates.add(weekday.getBeginDate().toString());
                                        info.setDates(dates);
                                        info.setBeginHour(weekday.getBeginTime().toString());
                                        info.setEndHour(weekday.getEndTime().toString());
                                    });
                            permits.add(info);
                        }
                    });
            response.addAll(permits); // Agregar los permisos encontrados a la lista acumulativa
        });
        System.out.println(response);
        return response;
    }

    @Override
    public List<GetPermitsDTO> getPermitsByState(String state) {
        List<GetPermitsDTO> permits = new ArrayList<>();
        permitRepository.findAll()
                .stream()
                .filter(permit -> permit.getPermitState().toString().equals(state))
                .forEach(permit -> {
                    GetPermitsDTO info = new GetPermitsDTO();
                    info.setPermitId(permit.getPermitId());
                    info.setCreationDate(permit.getCreationTimestamp());
                    info.setEntryType(permit.getEntryType().toString());
                    info.setPermitGenerationType(permit.getPermitGenerationType().toString());
                    info.setState(permit.getPermitState().toString());
                    info.setResident(permit.getIssuingUser().getEmail());
                    info.setGuess(permit.getTargetUser().getEmail());
                    info.setHouseNumber(permit.getHomeOfThePermit().getHouseNumber());
                    List<String> dates = new ArrayList<>();

                    weekDayRepository.findAll().stream().filter(weekday -> weekday.getPermit_date().getPermitId().equals(permit.getPermitId()))
                            .forEach(weekday -> {
                                dates.add(weekday.getBeginDate().toString());
                                info.setDates(dates);
                                info.setBeginHour(weekday.getBeginTime().toString());
                                info.setEndHour(weekday.getEndTime().toString());
                            });
                    permits.add(info);
                });
        return permits;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void rejectPermit(Permit permit) {
        permit.setPermitState(PermitState.REJECTED);
        permitRepository.save(permit);

        List<Weekday> weekdays = weekDayRepository.findAll().stream()
                .filter(weekday -> weekday.getPermit_date().getPermitId().equals(permit.getPermitId()))
                .toList();

        for (Weekday weekday : weekdays) {
            weekday.setPermitState(PermitState.REJECTED);
            weekDayRepository.save(weekday);
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void acceptPermit(Permit permit) {
        permit.setPermitState(PermitState.APPROVED);
        permitRepository.save(permit);

        List<Weekday> weekdays = weekDayRepository.findAll().stream()
                .filter(weekday -> weekday.getPermit_date().getPermitId().equals(permit.getPermitId()))
                .toList();

        for (Weekday weekday : weekdays) {
            weekday.setPermitState(PermitState.APPROVED);
            weekDayRepository.save(weekday);
        }
    }

    @Override
    public List<Boolean> conflictedDate(Home targetHouse, LocalDate beginDate, User guest) {
        List<Boolean> conflictedDates = new ArrayList<>();

        List<Permit> permits = permitRepository.findAll()
                .stream().filter(permit -> permit.getHomeOfThePermit().getHomeId().equals(targetHouse.getHomeId()))
                .filter(permit -> permit.getTargetUser().getEmail().equals(guest.getEmail()))
                .toList();

        for (Permit permit : permits) {
            weekDayRepository.findAll().stream()
                    .filter(weekday -> weekday.getPermit_date().getPermitId().equals(permit.getPermitId()))
                    .forEach(weekday -> {
                        if (weekday.getBeginDate().equals(beginDate)) {
                            conflictedDates.add(true);
                        }
                    });
        }

        return conflictedDates;
    }

    @Override
    public List<Boolean> conflictedTime(Home targetHouse, RequestPermitDTO info) {
        List<Boolean> conflictedDates = new ArrayList<>();

        LocalTime beginTime = convertTime(info.getBeginTime());

        List<Permit> permits = permitRepository.findAll()
                .stream().filter(permit -> permit.getHomeOfThePermit().getHomeId().equals(targetHouse.getHomeId()))
                .filter(permit -> permit.getTargetUser().getEmail().equals(info.getGuessEmail()))
                .toList();

        if (info.getEntryType().equals("UNIQUE")) {

            for (Permit permit : permits) {
                weekDayRepository.findAll().stream()
                        .filter(weekday -> weekday.getPermit_date().getPermitId().equals(permit.getPermitId()))
                        .forEach(weekday -> {
                            /*System.out.println(weekday.getBeginTime());
                            System.out.println(beginTime.minusMinutes(30));
                            System.out.println(beginTime.plusMinutes(30));
                            System.out.println(weekday.getEndTime());*/
                            if (weekday.getBeginTime().equals(beginTime.minusMinutes(30)) &&
                                weekday.getEndTime().equals(beginTime.plusMinutes(30))) {
                                System.out.println("Inicio y fin igual");
                                conflictedDates.add(true);
                            } else if (beginTime.plusMinutes(30).equals(weekday.getBeginTime()) ||
                                    beginTime.minusMinutes(30).equals(weekday.getEndTime())) {
                                System.out.println("Fin igual a inicio de un permiso o inicio igual a fin de un permiso");
                                conflictedDates.add(true);
                            } else if (beginTime.minusMinutes(30).isAfter(weekday.getBeginTime())) {
                                if (beginTime.minusMinutes(30).isBefore(weekday.getEndTime())) {
                                    System.out.println("Inicio mayor a inicio de un permiso y en el intervalo de un rango"); // Revisar
                                    conflictedDates.add(true);
                                }
                            } else if (beginTime.minusMinutes(30).isBefore(weekday.getBeginTime())) {
                                if (beginTime.plusMinutes(30).isAfter(weekday.getBeginTime()) &&
                                    beginTime.plusMinutes(30).isBefore(weekday.getEndTime())) {
                                    System.out.println("Inicio antes de inicio, pero en el intervalo de otro permiso");
                                    conflictedDates.add(true);
                                }
                            } else
                                conflictedDates.add(false);
                        });
            }

        } else if (info.getEntryType().equals("MULTIPLE")) {
            LocalTime endTime = convertTime(info.getEndTime());

            for (Permit permit : permits) {
                weekDayRepository.findAll().stream()
                        .filter(weekday -> weekday.getPermit_date().getPermitId().equals(permit.getPermitId()))
                        .forEach(weekday -> {
                            System.out.println(weekday.getBeginTime());
                            System.out.println(beginTime);
                            System.out.println(endTime);
                            System.out.println(weekday.getEndTime());

                            if (weekday.getBeginTime().equals(beginTime) ||
                                    weekday.getEndTime().equals(endTime)) {
                                System.out.println("Inicio o fin igual");
                                conflictedDates.add(true);
                            } else if (endTime.equals(weekday.getBeginTime()) ||
                                    beginTime.equals(weekday.getEndTime())) {
                                System.out.println("Fin igual a inicio de un permiso o inicio igual a fin de un permiso");
                                conflictedDates.add(true);
                            } else if (beginTime.isAfter(weekday.getBeginTime())) {
                                if (beginTime.isBefore(weekday.getEndTime())) {
                                    System.out.println("Inicio mayor a inicio de un permiso y en el intervalo de un rango"); // Revisar
                                    conflictedDates.add(true);
                                }
                            } else if (beginTime.isBefore(weekday.getBeginTime())) {
                                System.out.println("Entro");
                                if (endTime.isAfter(weekday.getEndTime())) {
                                    System.out.println("El permiso abarca otro permiso");
                                    conflictedDates.add(true);
                                }
                                if (endTime.isAfter(weekday.getBeginTime()) &&
                                        endTime.isBefore(weekday.getEndTime())) {
                                    System.out.println("Inicio antes de inicio, pero en el intervalo de otro permiso");
                                    conflictedDates.add(true);
                                }
                            } else
                                conflictedDates.add(false);
                        });
            }
        }

        return conflictedDates;
    }

    @Override
    public List<Boolean> conflictedDates(Home targetHouse, RequestPermitDTO info, User guest) {
        List<Boolean> conflictedDates = new ArrayList<>();
        LocalDate beginDate = convertDate(info.getBeginDate());
        LocalDate endDate = convertDate(info.getEndDate());

        List<DayOfWeek> days = info.getDays().stream()
                .map(day -> DayOfWeek.valueOf(day.toUpperCase()))
                .toList();
        List<String> stringDates = getPermitDates(beginDate, endDate, days);
        List<LocalDate> dates = new ArrayList<>();

        for (String stringDate : stringDates) {
            dates.add(convertDate(stringDate));
        }

        List<Permit> permits = permitRepository.findAll()
                .stream().filter(permit -> permit.getHomeOfThePermit().getHomeId().equals(targetHouse.getHomeId()))
                .filter(permit -> permit.getTargetUser().getEmail().equals(guest.getEmail()))
                .toList();

        for (Permit permit : permits) {
            weekDayRepository.findAll().stream()
                    .filter(weekday -> weekday.getPermit_date().getPermitId().equals(permit.getPermitId()))
                    .forEach(weekday -> {
                        if (dates.contains(weekday.getBeginDate())) {
                            conflictedDates.add(true);
                        }
                    });
        }
        return conflictedDates;
    }

    @Override
    public List<GetPermitsDTO> getPermitsByGuestToday(User guest) {
        List<GetPermitsDTO> permits = new ArrayList<>();

        permitRepository.findAll().stream()
                .filter(permit -> permit.getTargetUser().getEmail().equals(guest.getEmail()))
                .filter(permit -> permit.getPermitState().equals(PermitState.APPROVED))
                .forEach(permit -> {
                    GetPermitsDTO info = new GetPermitsDTO();
                    info.setPermitId(permit.getPermitId());
                    info.setCreationDate(permit.getCreationTimestamp());
                    info.setEntryType(permit.getEntryType().toString());
                    info.setPermitGenerationType(permit.getPermitGenerationType().toString());
                    info.setState(permit.getPermitState().toString());
                    info.setResident(permit.getIssuingUser().getEmail());
                    info.setGuess(permit.getTargetUser().getEmail());
                    info.setHouseNumber(permit.getHomeOfThePermit().getHouseNumber());

                    List<String> dates = new ArrayList<>();
                    List<Weekday> relevantWeekdays = weekDayRepository.findAll().stream()
                            .filter(weekday -> weekday.getPermit_date().getPermitId().equals(permit.getPermitId()))
                            .filter(weekday -> weekday.getBeginDate().equals(LocalDate.now()))
                            .filter(weekday -> weekday.getPermitState().equals(PermitState.APPROVED))
                            .toList();

                    relevantWeekdays.forEach(weekday -> {
                        if (weekday.getBeginTime().equals(LocalTime.now()) ||
                                (weekday.getBeginTime().isBefore(LocalTime.now()) && LocalTime.now().isBefore(weekday.getEndTime()))) {

                            dates.add(weekday.getBeginDate().toString());
                            info.setDates(dates);
                            info.setBeginHour(weekday.getBeginTime().toString());
                            info.setEndHour(weekday.getEndTime().toString());
                        }
                    });

                    if (!dates.isEmpty()) {
                        permits.add(info);
                    }
                });

        System.out.println(permits);
        return permits;
    }

    @Override
    public boolean canGuestEnter(User user) {
        return !(getPermitsByGuestToday(user).isEmpty());
    }

    @Override
    public List<Permit> getPermitsByUser(User user) {
        List<Permit> userPermits = permitRepository.findPermitsByTargetUser(user).orElse(null);
        return null;
    }

    @Override
    public List<Permit> getTodayActivePermitsByUser(User user) {
        return null;
    }
}
