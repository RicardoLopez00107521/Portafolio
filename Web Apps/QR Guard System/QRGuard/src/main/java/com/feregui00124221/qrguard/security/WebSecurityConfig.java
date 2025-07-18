package com.feregui00124221.qrguard.security;

import com.feregui00124221.qrguard.domain.entities.User;
import com.feregui00124221.qrguard.security.jwt.JWTTokenFilter;
import com.feregui00124221.qrguard.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserService userService;
    private final JWTTokenFilter jwtTokenFilter;
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(UserService userService, JWTTokenFilter jwtTokenFilter, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenFilter = jwtTokenFilter;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder managerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);

        managerBuilder
                .userDetailsService(email -> {
                    User user = userService.findUserByIdentifier(email);

                    if (user == null) {
                        throw new UsernameNotFoundException("User " + email + " not found");
                    }
                    return user;
                })
                .passwordEncoder(passwordEncoder);

        return managerBuilder.build();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.httpBasic(withDefaults()).csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/entry/**").hasRole("VIGILANT")
                .requestMatchers("/api/guard/**").hasRole("VIGILANT")

                .requestMatchers("/api/permit/accept-permit").hasRole("IN_CHARGE_RESIDENT")
                .requestMatchers("/api/permit/reject-permit").hasRole("IN_CHARGE_RESIDENT")
                .requestMatchers("/api/permit/generate-permit").hasAnyRole("IN_CHARGE_RESIDENT", "NORMAL_RESIDENT")
                .requestMatchers("/api/permit/allPermits").hasAnyRole("IN_CHARGE_RESIDENT", "NORMAL_RESIDENT")
                .requestMatchers("/api/permit/permitById").hasAnyRole("IN_CHARGE_RESIDENT", "NORMAL_RESIDENT")
                .requestMatchers("/api/permit/requestedPermits").hasRole("NORMAL_RESIDENT")

        );

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**")
                .permitAll()
                .anyRequest()
                .authenticated()
        );

        http.sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(handling -> handling.authenticationEntryPoint((req, res, ex) -> {
            res.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Auth fail!"
            );
        }));

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
