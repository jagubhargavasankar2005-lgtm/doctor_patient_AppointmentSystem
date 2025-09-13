package com.example.doctorpatientweb.controller;
import com.example.doctorpatientweb.entity.Appointment;
import com.example.doctorpatientweb.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/appointments")
@Service
public class AppointmentController {
    @Autowired
    AppointmentService appointmentService;
    @PostMapping("/book/{patientId}")
    public Appointment bookAppointment(
            @PathVariable Long patientId,
            @RequestParam LocalDate date) {
        return appointmentService.bookAppointment(patientId, date);
    }
    @GetMapping("/{patientId}")
    public Appointment bookeddate(@PathVariable Long patientId){
        return appointmentService.bookeddate(patientId);
    }
}
