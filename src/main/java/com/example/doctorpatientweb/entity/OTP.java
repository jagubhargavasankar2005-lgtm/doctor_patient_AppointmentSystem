
package com.example.doctorpatientweb.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "otp_codes")
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username; // link OTP to a user

    @Column(nullable = false)
    private String code; // 6-digit OTP

    @Column(nullable = false)
    private LocalDateTime expiration; // OTP expiry time
}
