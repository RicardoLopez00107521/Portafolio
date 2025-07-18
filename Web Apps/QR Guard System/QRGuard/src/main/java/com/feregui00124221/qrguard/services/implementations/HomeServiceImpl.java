package com.feregui00124221.qrguard.services.implementations;

import com.feregui00124221.qrguard.domain.dtos.HomeDTO.HomeDetailsDTO;
import com.feregui00124221.qrguard.domain.dtos.HomeDTO.NewHomeDTO;
import com.feregui00124221.qrguard.domain.dtos.UserDTO.SimpleUserInfoDTO;
import com.feregui00124221.qrguard.domain.entities.Home;
import com.feregui00124221.qrguard.domain.entities.Roles.Role;
import com.feregui00124221.qrguard.domain.entities.Roles.RoleCategory;
import com.feregui00124221.qrguard.domain.entities.User;
import com.feregui00124221.qrguard.repositories.HomeRepository;
import com.feregui00124221.qrguard.repositories.RoleRepository;
import com.feregui00124221.qrguard.repositories.UserRepository;
import com.feregui00124221.qrguard.services.HomeService;
import com.feregui00124221.qrguard.services.RoleService;
import com.feregui00124221.qrguard.services.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class HomeServiceImpl implements HomeService {
    private final HomeRepository homeRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public HomeServiceImpl(HomeRepository homeRepository, RoleRepository roleRepository, UserRepository userRepository) {
        this.homeRepository = homeRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Home findHomeById(String id) {
        Home home = homeRepository.findHomeByHomeId(UUID.fromString(id)).orElse(null);

        if (home == null) {
            Hibernate.initialize(home.getPermits());
        }

        return home;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Home findHomeByHouseNumber(String houseNumber) {
        Home home = homeRepository.findHomeByHouseNumber(houseNumber).orElse(null);

        if (home != null) {
            Hibernate.initialize(home.getResidents());
            home.getResidents().forEach(user -> Hibernate.initialize(user.getRoles()));
        }

        return home;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createHome(NewHomeDTO info) {
        Home newHome = new Home();
        newHome.setHouseNumber(info.getHouseNumber());
        newHome.setHouseCapacity(info.getHouseCapacity());
        homeRepository.save(newHome);
        homeRepository.flush();
    }

    @Override
    public HomeDetailsDTO getHomeDetails(Home homeToGetDetails) {
        List<SimpleUserInfoDTO> residents = new ArrayList<>();

        for (User resident : homeToGetDetails.getResidents()) {
            residents.add(
                    SimpleUserInfoDTO.builder()
                            .userEmail(resident.getEmail())
                            .userName(resident.getName())
                            .userRoles(
                                    resident.getRoles()
                                            .stream()
                                            .map(Role::getRoleName)
                                            .filter(roleName ->
                                                    (resident.equals(homeToGetDetails.getInChargeResident()))
                                                            ?
                                                            roleName.equals(RoleCategory.IN_CHARGE_RESIDENT)
                                                            :
                                                            roleName.equals(RoleCategory.NORMAL_RESIDENT))
                                            .collect(toList())
                            )
                            .build()
            );
        }

        return HomeDetailsDTO.builder()
                .houseNumber(homeToGetDetails.getHouseNumber())
                .inChargeResident(
                        (homeToGetDetails.getInChargeResident() == null)
                                ?
                                null
                                :
                                SimpleUserInfoDTO.builder()
                                        .userEmail(homeToGetDetails.getInChargeResident().getEmail())
                                        .userName(homeToGetDetails.getInChargeResident().getName())
                                        .userRoles(homeToGetDetails.getInChargeResident()
                                                .getRoles()
                                                        .stream()
                                                        .map(Role::getRoleName)
                                                .filter(roleName -> roleName.equals(RoleCategory.IN_CHARGE_RESIDENT))
                                                        .collect(toList())
                                        )
                                        .build()
                )
                .houseCapacity(homeToGetDetails.getHouseCapacity())
                .currentOccupancy(homeToGetDetails.getResidents().size())
                .residents(residents)
                .build();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public List<HomeDetailsDTO> getAllHomesDetails() {
        List<Home> homes = homeRepository.findAll();
        List<HomeDetailsDTO> homesDetails = new ArrayList<>();

        for (Home home : homes) {
            Hibernate.initialize(home.getResidents());
            home.getResidents().forEach(user -> Hibernate.initialize(user.getRoles()));
            homesDetails.add(getHomeDetails(home));
        }

        return homesDetails;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateHomeCapacity(Home home, int newHouseCapacity) {
        home.setHouseCapacity(newHouseCapacity);
        homeRepository.save(home);
        homeRepository.flush();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void linkNewResident(
            Home homeToLink,
            User newResident
    ) {
        homeToLink.getResidents().add(newResident);
        homeRepository.save(homeToLink);
        homeRepository.flush();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void unlinkResident(Home homeToUnlink, User residentToUnlink) {
        homeToUnlink.getResidents().remove(residentToUnlink);
        homeRepository.save(homeToUnlink);
        homeRepository.flush();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void assignInChargeResident(
            Home homeToAssign,
            User inChargeResident
    ) {
        List<Role> userRoles = inChargeResident.getRoles();
        List<Role> deleteRoles = new ArrayList<>();

        userRoles.forEach(role -> {
            if (role.getRoleName().equals(RoleCategory.NORMAL_RESIDENT)) {
                deleteRoles.add(role);
            }
        });

        if (!deleteRoles.isEmpty()) { // Si elimina guardia quita guardia y asigna User
            userRoles.remove(deleteRoles.getFirst());
            inChargeResident.setRoles(userRoles);
            userRepository.save(inChargeResident);

            inChargeResident.setRoles(userRoles);
            userRepository.save(inChargeResident);
        }
        homeToAssign.setInChargeResident(inChargeResident);
        homeRepository.save(homeToAssign);
        homeRepository.flush();
    }
}