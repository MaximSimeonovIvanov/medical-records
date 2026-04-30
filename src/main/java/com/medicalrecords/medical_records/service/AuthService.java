package com.medicalrecords.medical_records.service;

import com.medicalrecords.medical_records.dto.request.LoginRequest;
import com.medicalrecords.medical_records.dto.request.RegisterRequest;
import com.medicalrecords.medical_records.dto.response.AuthResponse;
import com.medicalrecords.medical_records.entity.Patient;
import com.medicalrecords.medical_records.entity.Role;
import com.medicalrecords.medical_records.entity.User;
import com.medicalrecords.medical_records.exception.DuplicateResourceException;
import com.medicalrecords.medical_records.exception.ResourceNotFoundException;
import com.medicalrecords.medical_records.repository.PatientRepository;
import com.medicalrecords.medical_records.repository.UserRepository;
import com.medicalrecords.medical_records.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        //proverka za zaet username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException(
                    "Username " + request.getUsername() + " is already taken");
        }

        //proverka egn
        if (patientRepository.existsByEgn(request.getEgn())) {
            throw new DuplicateResourceException(
                    "Patient with EGN " + request.getEgn()
                            + " is already registered");
        }

        //sazdavam patient entity
        Patient patient = Patient.builder()
                .name(request.getName())
                .egn(request.getEgn())
                .healthInsured(false)
                .build();
        Patient savedPatient = patientRepository.save(patient);

        //sazdavam akaunt
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PATIENT)
                .patient(savedPatient)
                .build();
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, "ROLE_" + Role.PATIENT.name(),
                request.getUsername());
    }

    public AuthResponse login(LoginRequest request) {
        //ako credentials sa greshni hvarlq greshka
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        //ako stigne do tuk credentials sa verni
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, "ROLE_" + user.getRole().name(),
                user.getUsername());
    }
}