package com.medicalrecords.medical_records.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String role;
    private String username;
    // klienta pazi tokena i go prashta s vseki badesht request
}