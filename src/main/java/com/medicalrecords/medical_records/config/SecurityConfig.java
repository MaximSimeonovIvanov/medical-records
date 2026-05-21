package com.medicalrecords.medical_records.config;

import com.medicalrecords.medical_records.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain; //това си е Bean към Spring
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
//позволява ми да ползвам PreAutherize

public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        //публични страници
                        .requestMatchers("/", "/login", "/register",
                                "/css/**", "/js/**", "/images/**").permitAll()

                        //рест апи ендпойнти-jwt auth
                        .requestMatchers("/api/auth/**").permitAll() //логин и реистрация са публик
                        .requestMatchers("/api/**").authenticated()  //вс други иска автентикация

                        //web/thymleaf endpoints-session auth and cookies
                        .requestMatchers("/doctors/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers("/patients/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers("/visits/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers("/diagnoses/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers("/sick-leaves/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                        .requestMatchers("/statistics/**").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        //custom login page
                        .loginProcessingUrl("/login")
                        //спринг обработва формата от POST тук
                        .defaultSuccessUrl("/dashboard", true)
                        //след логин към дашборд
                        .failureUrl("/login?error=true")
                        //при грешни данни обратно към логин грешка
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true) //iztrivam sesiqta ot pametta, cookie becomes worthless
                        .clearAuthentication(true) //изчиствам security context
                        .permitAll()
                )

                //регистрира daoauthenticationprovider
                //спринг секюрити го ползва при автентикация
                .authenticationProvider(authenticationProvider)
                //pazq jwt filter za rest api
                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class);
                //jwt filter се изпълнява преди логин филтъра по подразбиране на Спринг

        return http.build();
    }
}