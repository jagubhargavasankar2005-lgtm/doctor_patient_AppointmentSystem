package com.example.doctorpatientweb.service.impl;

import com.example.doctorpatientweb.entity.Doctor;
import com.example.doctorpatientweb.repository.DoctorRepository;
import com.example.doctorpatientweb.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    DoctorRepository doctorRepository;
    @Override
    public Doctor addDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).get();
    }

    @Override
    public Doctor patchDoctor(Long id, Doctor doctor) {
        Doctor doctor1 = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id " + id));

        if (doctor.getName() != null) doctor1.setName(doctor.getName());
        if (doctor.getSpecialization() != null) doctor1.setSpecialization(doctor.getSpecialization());
        if (doctor.getPhone() != null) doctor1.setPhone(doctor.getPhone());
        if (doctor.getEmail() != null) doctor1.setEmail(doctor.getEmail());

        return doctorRepository.save(doctor1);
    }

    @Override
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public Doctor getDoctorByName(String name) {
        return doctorRepository.getDoctorByName(name);
    }
    @Override
    public Doctor updateDoctor(Long id, Doctor doctor) {
        Doctor doctor1 = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id " + id));

        doctor1.setName(doctor.getName());
        doctor1.setSpecialization(doctor.getSpecialization());
        doctor1.setPhone(doctor.getPhone());
        doctor1.setEmail(doctor.getEmail());

        return doctorRepository.save(doctor1);
    }
}
