package com.feregui00124221.qrguard.services;

import com.feregui00124221.qrguard.domain.dtos.LoginDTO;
import com.feregui00124221.qrguard.domain.dtos.UserDTO.SimpleUserInfoDTO;
import com.feregui00124221.qrguard.domain.entities.Home;
import com.feregui00124221.qrguard.domain.entities.Permit.Permit;
import com.feregui00124221.qrguard.domain.entities.Token;
import com.feregui00124221.qrguard.domain.entities.Roles.Role;
import com.feregui00124221.qrguard.domain.entities.Roles.RoleCategory;
import com.feregui00124221.qrguard.domain.entities.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import java.time.Instant;

public interface UserService {
    User findUserByIdentifier(String identifier);
    User findUserByEmail(String email);
    void registerUser(LoginDTO user);
    boolean checkQrGenerationPermit(String email);

    // Token Management
    void cleanTokens(User user);
    Token registerToken(User user);
    Boolean validateToken(User user, String token);
    void cleanPreviousTokens(User user);
    User findUserAuthenticated();

    SimpleUserInfoDTO userInfo(User user);

    boolean isUserInChargeAnywhere(User user);
    void deleteRoleFromUser(User user, Role role);
    void addRoleToUser(User user, Role role);
    boolean doesUserHaveRole(User user, RoleCategory role);
    boolean isQrValid(User user, Instant qrTimestamp);

    Boolean isResident(User user);
    Boolean userAreInHome (Home home, User user);
    List<String> homesByUser(User user);


}
