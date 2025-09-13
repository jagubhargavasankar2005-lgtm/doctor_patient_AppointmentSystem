package com.example.doctorpatientweb.service;

import com.example.doctorpatientweb.entity.Doctor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DoctorService {
    Doctor addDoctor(Doctor doctor);

    List<Doctor> getAllDoctors();
   

    Doctor getDoctorByName(String name);

    Doctor updateDoctor(Long id,Doctor doctor);

    Doctor getDoctorById(Long id);

    Doctor patchDoctor(Long id, Doctor doctor);

    void deleteDoctor(Long id);
}
