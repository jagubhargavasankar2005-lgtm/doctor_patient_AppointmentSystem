package com.example.doctorpatientweb.controller;
import com.example.doctorpatientweb.entity.Patient;
import com.example.doctorpatientweb.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    PatientService patientService;

    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientService.savePatient(patient);
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        return patientService.updatePatient(id, patient);
    }

    @PatchMapping("/{id}")
    public Patient updatePatientPartially(@PathVariable Long id, @RequestBody Patient partialPatient) {
        return patientService.updatePatientPartially(id, partialPatient);
    }

    @DeleteMapping
    public Map<String, Object> deletePatient(@RequestParam String name) {
         return patientService.deleteByName(name);
    }
}
