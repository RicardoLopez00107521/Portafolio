package com.feregui00124221.qrguard.domain.dtos.UserDTO;

import com.feregui00124221.qrguard.domain.entities.Roles.RoleCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SimpleUserInfoDTO {
    @NotEmpty
    private String userEmail;

    private String userName;

    @NotEmpty
    @Enumerated(EnumType.STRING)
    private List<RoleCategory> userRoles;
}
