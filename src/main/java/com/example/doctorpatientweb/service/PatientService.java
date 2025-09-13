package com.example.doctorpatientweb.service;

import com.example.doctorpatientweb.entity.Patient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface PatientService {
    Patient savePatient(Patient patient);

    List<Patient> getAllPatients();

    Patient getPatientById(Long id);

    Patient updatePatient(Long id, Patient patient);

    Patient updatePatientPartially(Long id, Patient partialPatient);


    Map<String, Object> deleteByName(String name);
}
