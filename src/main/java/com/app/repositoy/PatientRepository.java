package com.app.repositoy;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
	Optional<Patient> findByEmail(String email);
}
