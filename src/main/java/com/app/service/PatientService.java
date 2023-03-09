package com.app.service;

import java.util.List;

import com.app.dao.model.PatientDto;

import jakarta.validation.Valid;

public interface PatientService {
	public List<PatientDto> getAllPatients();

	public void deletePatient(long patientId);

	public PatientDto findUserByEmail(String email);

	public void savePatient(PatientDto userDto);

	public PatientDto findUserById(long id);

	public void deletePatientDisability(@Valid long id, PatientDto patientDto);

}
