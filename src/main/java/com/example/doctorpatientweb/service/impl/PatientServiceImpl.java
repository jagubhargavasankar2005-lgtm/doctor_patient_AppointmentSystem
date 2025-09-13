package com.example.doctorpatientweb.service.impl;
import com.example.doctorpatientweb.entity.Patient;
import com.example.doctorpatientweb.repository.PatientRepository;
import com.example.doctorpatientweb.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    PatientRepository patientRepository;

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).get();
    }

    @Override
    public Patient updatePatient(Long id, Patient patient) {
        Patient existingPatient = getPatientById(id);
        existingPatient.setName(patient.getName());
        existingPatient.setAge(patient.getAge());
        existingPatient.setGender(patient.getGender());
        existingPatient.setEmail(patient.getEmail());
        existingPatient.setPhone(patient.getPhone());
        return patientRepository.save(existingPatient);
    }

    @Override
    public Patient updatePatientPartially(Long id, Patient partialPatient) {
        Patient existingPatient = getPatientById(id);

        if (partialPatient.getName() != null) {
            existingPatient.setName(partialPatient.getName());
        }
        if (partialPatient.getAge() != null) {
            existingPatient.setAge(partialPatient.getAge());
        }
        if (partialPatient.getGender() != null) {
            existingPatient.setGender(partialPatient.getGender());
        }
        if (partialPatient.getEmail() != null) {
            existingPatient.setEmail(partialPatient.getEmail());
        }
        if (partialPatient.getPhone() != null) {
            existingPatient.setPhone(partialPatient.getPhone());
        }
        return patientRepository.save(existingPatient);
    }

    @Override
    public Map<String, Object> deleteByName(String name) {
        Integer delCount= patientRepository.deleteByName(name);
        Map<String, Object> result= new HashMap<>();
        if(delCount >= 1) {
            result.put("code", "SUCCESS");
            result.put("message", "Records deleted successfully");
        }
    return result;
    }

//    @Override
//    public void deleteById(Long id) {
//        patientRepository.deleteById(id);
//    }



}
