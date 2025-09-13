package com.example.doctorpatientweb.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Long age;

    private String gender;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;
    private String problem;
}
