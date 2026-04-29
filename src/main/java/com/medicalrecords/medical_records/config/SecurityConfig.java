package com.medicalrecords.medical_records.config;

import com.medicalrecords.medical_records.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
//EnableMethodSecurity ми дава да ползвам PreAuthorize
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
                                .requestMatchers("/api/auth/**").permitAll()

                                .requestMatchers("/api/doctors/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                                .requestMatchers("/api/patients/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                                .requestMatchers("/api/diagnoses/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                                .requestMatchers("/api/visits/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                                .requestMatchers("/api/sick-leaves/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")

                                .anyRequest().authenticated()
                        //вс останало изисква автентикация
                )

                .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        //сървърът не помни нищо = РЕСТ
                )

                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class);
        //първо добавям jwt filter преди дефолтния логин филтър на спринг
        //за да проверя първо jwt на всеки рекуест

        return http.build();
    }
}