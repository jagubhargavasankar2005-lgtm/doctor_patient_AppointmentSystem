package com.example.doctorpatientweb.repository;
import com.example.doctorpatientweb.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

}
