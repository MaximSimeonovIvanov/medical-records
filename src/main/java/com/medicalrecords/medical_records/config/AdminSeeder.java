package com.medicalrecords.medical_records.config;

import com.medicalrecords.medical_records.entity.Role;
import com.medicalrecords.medical_records.entity.User;
import com.medicalrecords.medical_records.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {
//commandlinerunner работи автоматично след пускатнето на приложението което е добре за сийдването на initial данните

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("admin").isEmpty()) {
            //създ админ само ако вече не същ.
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println("Admin account created: admin / admin123");
        }
    }
}