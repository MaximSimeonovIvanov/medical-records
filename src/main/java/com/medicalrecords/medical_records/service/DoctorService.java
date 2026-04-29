package com.medicalrecords.medical_records.service;

import com.medicalrecords.medical_records.dto.request.CreateDoctorRequest;
import com.medicalrecords.medical_records.dto.response.DoctorResponse;
import com.medicalrecords.medical_records.entity.Doctor;
import com.medicalrecords.medical_records.entity.Role;
import com.medicalrecords.medical_records.entity.User;
import com.medicalrecords.medical_records.exception.DuplicateResourceException;
import com.medicalrecords.medical_records.exception.ResourceNotFoundException;
import com.medicalrecords.medical_records.mapper.EntityMapper;
import com.medicalrecords.medical_records.repository.DoctorRepository;
import com.medicalrecords.medical_records.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

@RequiredArgsConstructor
//lombok generira const s final poleta. taka injectiram dependencies
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final EntityMapper mapper;
    private final PasswordEncoder passwordEncoder;
    //constructor injection

    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(mapper::toDoctorResponse)
                .toList();
                // .stream() converts it to a stream for processing
    }

    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor with id " + id + " not found"));
        return mapper.toDoctorResponse(doctor);
    }

    public List<DoctorResponse> getAllGPs() {
        return doctorRepository.findAll()
                .stream()
                .filter(Doctor::isGp)
                .map(mapper::toDoctorResponse)
                .toList();
    }

    @Transactional
    public DoctorResponse createDoctor(CreateDoctorRequest request) {
        if (doctorRepository.existsByUin(request.getUin())) {
            throw new DuplicateResourceException(
                    "Doctor with UIN " + request.getUin() + " already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException(
                    "Username " + request.getUsername() + " is already taken");
        }

        Doctor doctor = Doctor.builder()
                .uin(request.getUin())
                .name(request.getName())
                .specialty(request.getSpecialty())
                .gp(request.isGp())
                .build();
        Doctor savedDoctor = doctorRepository.save(doctor);

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.DOCTOR)
                .doctor(savedDoctor)
                .build();
        userRepository.save(user);

        return mapper.toDoctorResponse(savedDoctor);
    }

    @Transactional
    public DoctorResponse updateDoctor(Long id, CreateDoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor with id " + id + " not found"));

        doctor.setName(request.getName());
        doctor.setSpecialty(request.getSpecialty());
        doctor.setGp(request.isGp());

        return mapper.toDoctorResponse(doctorRepository.save(doctor));
    }

    @Transactional
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Doctor with id " + id + " not found");
        }
        doctorRepository.deleteById(id);
    }
}