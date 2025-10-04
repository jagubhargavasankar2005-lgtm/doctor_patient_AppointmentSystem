package com.example.doctorpatientweb.service.impl;

import com.example.doctorpatientweb.dto.AppointmentDto;
import com.example.doctorpatientweb.entity.Appointment;
import com.example.doctorpatientweb.entity.Doctor;
import com.example.doctorpatientweb.entity.Patient;
import com.example.doctorpatientweb.exception.ResourceNotFoundException;
import com.example.doctorpatientweb.repository.AppointmentRepository;
import com.example.doctorpatientweb.repository.DoctorRepository;
import com.example.doctorpatientweb.repository.PatientRepository;
import com.example.doctorpatientweb.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    // Problem → Specialization mapping
    private static final Map<String, String> problemToSpecialization = Map.of(
            "skin", "Dermatologist",
            "heart", "Cardiologist",
            "teeth", "Dentist",
            "eye", "Ophthalmologist",
            "child", "Pediatrician",
            "bone", "Orthopedic"
    );

    @Override
    public Appointment bookAppointment(Long patientId, LocalDate date) {
        // 1️⃣ Fetch patient
               // fallback if no match
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // 2️⃣ Normalize problem string
        String problem = patient.getProblem() == null ? "" : patient.getProblem().trim().toLowerCase();

        // 3️⃣ Map problem → specialization
        String specialization = problemToSpecialization.entrySet().stream()
                .filter(e -> problem.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("General Physician");

        Doctor doctor = doctorRepository.findAll().stream()
                .filter(d -> d.getSpecialization().equalsIgnoreCase(specialization))
                .findFirst()
                .orElse(null); // ✅ return null if no doctor found

        // 5️⃣ Check if doctor exists
        if (doctor == null) {
            System.out.println("No doctor available for " + specialization);
            return null; // or throw custom message if you want
        }

        // 6️⃣ Create & save appointment
        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(date);
        appointment.setStatus("BOOKED");
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        return appointmentRepository.save(appointment);
    }

    // In AppointmentServiceImpl.java
    @Override
    public AppointmentDto bookeddate(Long patientId) {
        // 1️⃣ Find the appointment for the patient
        Appointment appointment = appointmentRepository.findAll().stream()
                .filter(a -> a.getPatient().getId().equals(patientId))
                .findFirst()
                .orElse(null); // If no appointment found, return null

        // 2️⃣ If no appointment exists, return null or handle as needed
        if (appointment == null) {
            System.out.println("No appointment booked for patient ID: " + patientId);
            return null;
        }
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setAppointmentDate(appointment.getAppointmentDate());

        return appointmentDto;
    }

}
