package com.feregui00124221.qrguard.services.implementations;

import com.feregui00124221.qrguard.domain.dtos.LoginDTO;
import com.feregui00124221.qrguard.domain.dtos.UserDTO.SimpleUserInfoDTO;
import com.feregui00124221.qrguard.domain.entities.Home;
import com.feregui00124221.qrguard.domain.entities.Permit.Permit;
import com.feregui00124221.qrguard.domain.entities.Roles.Role;
import com.feregui00124221.qrguard.domain.entities.Token;
import com.feregui00124221.qrguard.domain.entities.Roles.RoleCategory;
import com.feregui00124221.qrguard.domain.entities.User;
import com.feregui00124221.qrguard.repositories.HomeRepository;
import com.feregui00124221.qrguard.repositories.RoleRepository;
import com.feregui00124221.qrguard.repositories.TokenRepository;
import com.feregui00124221.qrguard.repositories.UserRepository;
import com.feregui00124221.qrguard.security.jwt.JWTTools;
import com.feregui00124221.qrguard.services.RoleService;
import com.feregui00124221.qrguard.services.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class UserServiceImplementation implements UserService {

    private final JWTTools jwtTools;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;
    private final HomeRepository homeRepository;
    private final RoleService roleService;

    public UserServiceImplementation(JWTTools jwtTools, PasswordEncoder passwordEncoder, UserRepository userRepository, TokenRepository tokenRepository, RoleRepository roleRepository, HomeRepository homeRepository, RoleService roleService) {
        this.jwtTools = jwtTools;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
        this.homeRepository = homeRepository;
        this.roleService = roleService;
    }

    @Override
    public User findUserByIdentifier(String identifier) {
        UUID uuid = UUID.fromString(identifier);
        return userRepository.findUserByUserIdAndActiveUserIsTrue(uuid)
                .orElse(null);
    }

    /**
     * @param email String that contains the email to be checked
     * @return User that contains the email. If the email is not found, it returns null, meaning that there is no User registered with said email.
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmailAndActiveUserIsTrue(email).orElse(null);

        if(user != null){
            Hibernate.initialize(user.getRoles());
            Hibernate.initialize(user.getInChargeHouses());
            Hibernate.initialize(user.getLinkedHouses());
        }
        return user;
    }

    /**
     * @param info  LoginDTO object that contains the information of the enteringUser to be registered
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void registerUser(LoginDTO info) {
        User newUser = new User();

        newUser.setEmail(info.getEmail());
        newUser.setName(info.getUsername());
        newUser.setActiveUser(true);
        List<Role> roles = findRolesByNameList(List.of(RoleCategory.GUEST));
        System.out.println(roles);
        newUser.setRoles(roles);
        newUser.setQrValidTime(Duration.ofMinutes(10));

        userRepository.save(newUser);
    }

    /**
     * @param email     String containing the email of the enteringUser requesting to generate a QR code
     * @return          boolean that indicates if the enteringUser has the permit to generate a QR code
     */
    @Override
    public boolean checkQrGenerationPermit(String email) {
        User user = findUserByEmail(email);
        if (user == null) {
            return false;
        }
        return user.isActiveUser();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean isUserInChargeAnywhere(User user) {
        Hibernate.initialize(user.getInChargeHouses());
        return !user.getInChargeHouses().isEmpty();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteRoleFromUser(User user, Role role) {
        user.getRoles().removeIf(r -> r.getRoleId().equals(role.getRoleId()));
        userRepository.save(user);
        userRepository.flush();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void addRoleToUser(User user, Role role) {
        user.getRoles().add(role);
        userRepository.save(user);
        userRepository.flush(); // Explicitly flush changes to ensure they are persisted immediately
//        User managedUser = userRepository.findById(user.getUserId()).orElse(null);
//        Role managedRole = roleRepository.findById(role.getRoleId()).orElse(null);
//
//        if (managedUser != null && managedRole != null && !managedUser.getRoles().contains(managedRole)) {
//            managedUser.getRoles().add(managedRole);
//            userRepository.save(managedUser);
//            userRepository.flush(); // Explicitly flush changes to ensure they are persisted immediately
//        }
    }

    @Override
    public boolean doesUserHaveRole(User user, RoleCategory role) {
        return (user.getRoles().stream().anyMatch(r -> r.getRoleName().equals(role)));
    }

    @Override
    public Boolean isResident(User user) {
        List<Role> roles = user.getRoles();

        for (Role role : roles) {
            if (role.getRoleName().equals(RoleCategory.IN_CHARGE_RESIDENT) || role.getRoleName().equals(RoleCategory.NORMAL_RESIDENT))
                return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean userAreInHome(Home home, User user) {
        System.out.println(home.getHomeId().toString());
        System.out.println(user.getEmail());

        Home homeToCheck = homeRepository.findHomeByHomeId(home.getHomeId()).orElse(null);

        Hibernate.initialize(homeToCheck.getResidents());

        List<User> liveHere = homeToCheck.getResidents();

        for (User u : liveHere) {
            System.out.println(u.getEmail());
            if (u.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> homesByUser(User user) {
        List<Home> homes = user.getLinkedHouses();
        List<String> homesByUser = new ArrayList<>();

        for (Home home : homes) {
            homesByUser.add(home.getHouseNumber());
        }
        System.out.println(user.getEmail() + " " + homesByUser);
        return homesByUser;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void cleanTokens(User user) {
        List<Token> tokens = tokenRepository.findAllByTokenUserAndAndIsTokenActive(user, true).orElse(null);

        assert tokens != null;
        tokens.forEach(token -> {
            if (!jwtTools.verifyToken(token.getContent())) {
                token.setIsTokenActive(false);
                tokenRepository.save(token);
            }
        });
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Token registerToken(User user) {
        cleanPreviousTokens(user);

        new Token();
        Token token = Token.builder()
                .content(jwtTools.generateToken(user))
                .tokenUser(user)
                .isTokenActive(true)
                .build();

        tokenRepository.save(token);

        return token;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean validateToken(User user, String token) {
    cleanTokens(user);
    List<Token> tokens = tokenRepository.findAllByTokenUserAndAndIsTokenActive(user, true).orElse(null);

    tokens.stream()
            .filter(tk -> tk.getContent().equals(token))
            .findAny();

    return tokens.stream().anyMatch(tk -> tk.getContent().equals(token)); // Si un token cumple con la condicion dada devolvera true, sino devolver false
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void cleanPreviousTokens(User user) {
      List<Token> tokens = tokenRepository.findAllByTokenUserAndAndIsTokenActive(user, true).orElse(null);

      if (tokens == null) {
          return;
      }

      tokens.forEach(token -> {
          token.setIsTokenActive(false);
          tokenRepository.save(token);
      });
    }

    @Override
    public User findUserAuthenticated() {

      String email = SecurityContextHolder
              .getContext()
              .getAuthentication()
              .getName();

      return userRepository.findUserByEmailAndActiveUserIsTrue(email)
              .orElse(null);
    }

    @Override
    public SimpleUserInfoDTO userInfo(User user) {
      return SimpleUserInfoDTO.builder().userEmail(user.getEmail())
              .userName(user.getName())
              .userRoles(user.getRoles().stream().map(Role::getRoleName).collect(toList()))
              .build();
    }

    private List<Role> findRolesByNameList(List<RoleCategory> roles) {
        return roles.stream()
                .map(role -> roleRepository.findRoleByRoleName(role).orElse(null))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isQrValid(User user, Instant qrTimestamp) {
        // I will manage the QR validity here. The verification must make sure that the QR code is still valid.
        // It will do so by comparing the instant the QR was generated with the current instant and make sure that the difference is less than the validity time.
        return qrTimestamp.plus(user.getQrValidTime()).isAfter(Instant.now());
    }
}
