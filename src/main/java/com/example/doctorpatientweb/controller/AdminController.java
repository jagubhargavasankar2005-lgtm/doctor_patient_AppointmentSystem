package com.example.doctorpatientweb.controller;
import com.example.doctorpatientweb.entity.Role;
import com.example.doctorpatientweb.entity.User;
import com.example.doctorpatientweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Admin adds doctor
    @PostMapping("/addDoctor")
    public ResponseEntity<String> addDoctor(@RequestBody User doctor) {
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctor.setRole(Role.DOCTOR);
        userRepository.save(doctor);
        return ResponseEntity.ok("Doctor added successfully!");
    }
}
