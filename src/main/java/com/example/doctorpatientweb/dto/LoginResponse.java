package com.example.doctorpatientweb.dto;

public record LoginResponse(
        String access_token,
        long expiry_time,
        String token_type
) {}
