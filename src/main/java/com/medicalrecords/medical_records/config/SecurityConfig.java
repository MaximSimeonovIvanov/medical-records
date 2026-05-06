package com.medicalrecords.medical_records.config;

import com.medicalrecords.medical_records.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
//позволява ми да ползвам PreAutherize

public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        // Public pages — no login needed
                        .requestMatchers("/", "/login", "/register",
                                "/css/**", "/js/**", "/images/**").permitAll()

                        // REST API endpoints — JWT auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/**").authenticated()

                        // Thymeleaf pages — session auth
                        .requestMatchers("/doctors/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers("/patients/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers("/visits/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers("/diagnoses/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers("/sick-leaves/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers("/statistics/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        // Our custom login page
                        .loginProcessingUrl("/login")
                        // Spring processes the form POST here
                        .defaultSuccessUrl("/dashboard", true)
                        // After login → go to dashboard
                        .failureUrl("/login?error=true")
                        // Wrong credentials → back to login with error
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )

                // Keep JWT filter for REST API
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}