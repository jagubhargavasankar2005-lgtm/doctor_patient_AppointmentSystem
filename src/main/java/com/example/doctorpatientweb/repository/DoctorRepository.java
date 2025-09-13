package com.example.doctorpatientweb.repository;

import com.example.doctorpatientweb.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query(value="select *from doctor where name=:name", nativeQuery = true)
    Doctor getDoctorByName(String name);

}
