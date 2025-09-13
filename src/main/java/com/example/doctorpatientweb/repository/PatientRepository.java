package com.example.doctorpatientweb.repository;

import com.example.doctorpatientweb.entity.Patient;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Modifying
    @Transactional
    @Query(value="delete from patient where name=:name", nativeQuery = true)
    Integer deleteByName(String name);
}
