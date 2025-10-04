package com.example.doctorpatientweb.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users") // avoid conflict with SQL reserved word "user"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email; // new field for email

    @Column(nullable = false)
    private String password;  // stored as hashed

    @Enumerated(EnumType.STRING)  // stores role as text
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean enabled = false; // track email verification
}
