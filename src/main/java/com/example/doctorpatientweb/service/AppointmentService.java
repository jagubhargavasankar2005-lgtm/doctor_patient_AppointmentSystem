package com.example.doctorpatientweb.service;

import com.example.doctorpatientweb.entity.Appointment;

import java.time.LocalDate;

public interface AppointmentService {

    Appointment bookAppointment(Long patientId, LocalDate s);

    Appointment bookeddate(Long patientId);
}
