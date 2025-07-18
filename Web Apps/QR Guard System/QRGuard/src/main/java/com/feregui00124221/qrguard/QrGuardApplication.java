package com.feregui00124221.qrguard;

import com.feregui00124221.qrguard.domain.entities.Roles.Role;
import com.feregui00124221.qrguard.domain.entities.Roles.RoleCategory;
import com.feregui00124221.qrguard.domain.entities.User;
import com.feregui00124221.qrguard.repositories.RoleRepository;
import com.feregui00124221.qrguard.repositories.UserRepository;
import com.feregui00124221.qrguard.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class QrGuardApplication {

	public static void main(String[] args) {
		SpringApplication.run(QrGuardApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunnerRol(
			UserRepository userRepository,
			RoleRepository roleRepository,
			UserService userService
	){
		return args -> {

			for (RoleCategory roleOption : RoleCategory.values()) {
				Role role = new Role();
				role.setRoleId(roleOption.toString().substring(0, 3) + UUID.randomUUID().toString().substring(0, 3));
				role.setRoleName(roleOption);

				if (roleRepository.findRoleByRoleName(role.getRoleName()).isEmpty()) {
					roleRepository.save(role);
				}
			}

			User admin = userService.findUserByEmail("admin@admin.com");

			if(admin == null) {
				admin = new User();
				admin.setName("admin");
				admin.setEmail("admin@admin.com");

				List<Role> roles = new ArrayList<>();
				roles.add(roleRepository.findRoleByRoleName(RoleCategory.ADMIN).orElse(null));
				admin.setRoles(roles);

				userRepository.save(admin);
			}

			User residentInCharge = userService.findUserByEmail("incharge@mail.com");

			if(residentInCharge == null){
				residentInCharge = new User();
				residentInCharge.setName("incharge");
				residentInCharge.setEmail("incharge@mail.com");

				List<Role> roles = new ArrayList<>();
				roles.add(roleRepository.findRoleByRoleName(RoleCategory.GUEST).orElse(null));
				residentInCharge.setRoles(roles);

				userRepository.save(residentInCharge);
			}

			User resident = userService.findUserByEmail("resident@mail.com");

			if(resident == null) {
				resident = new User();
				resident.setName("resident");
				resident.setEmail("resident@mail.com");

				List<Role> roles = new ArrayList<>();
				roles.add(roleRepository.findRoleByRoleName(RoleCategory.GUEST).orElse(null));
				resident.setRoles(roles);

				userRepository.save(resident);
			}

			User visitor = userService.findUserByEmail("visit@mail.com");

			if(visitor == null) {
				visitor = new User();
				visitor.setName("visitor");
				visitor.setEmail("visit@mail.com");

				List<Role> roles = new ArrayList<>();
				roles.add(roleRepository.findRoleByRoleName(RoleCategory.GUEST).orElse(null));
				visitor.setRoles(roles);

				userRepository.save(visitor);
			}
		};
	}
}
