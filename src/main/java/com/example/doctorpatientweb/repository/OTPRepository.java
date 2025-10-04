package com.example.doctorpatientweb.repository;

import com.example.doctorpatientweb.entity.OTP; // must match entity class name
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPRepository extends JpaRepository<OTP, Long> {
    OTP findByUsername(String username);
}
