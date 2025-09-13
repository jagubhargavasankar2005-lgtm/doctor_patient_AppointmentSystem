// AppointmentDto.java
package com.example.doctorpatientweb.dto;

import java.time.LocalDate;

import com.example.doctorpatientweb.entity.Appointment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto extends Appointment {
    private LocalDate appointmentDate;
}
