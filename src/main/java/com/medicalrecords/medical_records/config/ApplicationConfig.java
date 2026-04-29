package com.medicalrecords.medical_records.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
//казва "този клас съдържа bean дефиниции"

public class ApplicationConfig {

    @Bean
    //извикай метода и пази резултата за да го инжектирам където е нужно
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}