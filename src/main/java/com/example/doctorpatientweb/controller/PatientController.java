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

    // ✅ Create Patient
    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientService.savePatient(patient);
    }

    // ✅ Get All Patients
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    // ✅ Get Patient by ID
    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    // ✅ Full Update (PUT)
    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        return patientService.updatePatient(id, patient);
    }

    // ✅ Partial Update (PATCH)
    @PatchMapping("/{id}")
    public Patient updatePatientPartially(@PathVariable Long id, @RequestBody Patient partialPatient) {
        return patientService.updatePatientPartially(id, partialPatient);
    }

    // ✅ Delete Patient
    @DeleteMapping
    public Map<String, Object> deletePatient(@RequestParam String name) {
         return patientService.deleteByName(name);
    }
}
