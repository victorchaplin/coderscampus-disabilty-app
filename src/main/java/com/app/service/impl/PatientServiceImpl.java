package com.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.dao.model.DisabilityDto;
import com.app.dao.model.PatientDto;
import com.app.model.Disability;
import com.app.model.Patient;
import com.app.repositoy.PatientRepository;
import com.app.service.DisabilityService;
import com.app.service.PatientService;

@Service
public class PatientServiceImpl implements PatientService {
	@Autowired
	private PatientRepository patientRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private DisabilityService disabilityService;

	@Override
	public List<PatientDto> getAllPatients() {
		List<Patient> patients = patientRepository.findAll();
		List<PatientDto> patientDtos = new ArrayList<>();
		for (Patient patient : patients) {
			patientDtos.add(convertToPatientDto(patient));
		}
		return patientDtos;
	}

	@Override
	public void deletePatient(long patientId) {
		Optional<Patient> patient = patientRepository.findById(patientId);
		if (!patient.isPresent()) {
			throw new RuntimeException("Patient with given Id does not exist");
		}
		patientRepository.deleteById(patientId);
	}

	@Override
	public PatientDto findUserByEmail(String email) {
		Optional<Patient> patient = patientRepository.findByEmail(email);
		if (!patient.isPresent()) {
			throw new RuntimeException("Patient with given email does not exist");
		}
		return convertToPatientDto(patient.get());
	}

	private PatientDto convertToPatientDto(Patient patient) {
		PatientDto patientDto = new PatientDto();
		patientDto.setFirstName(patient.getFirstName());
		patientDto.setLastName(patient.getLastName());
		patientDto.setEmail(patient.getEmail());
		patientDto.setRole(patient.getRole());
		patientDto.setId(patient.getId());
		if (null != patient.getDisabilities() && !patient.getDisabilities().isEmpty()) {
			List<DisabilityDto> disabilities = new ArrayList<>();
			for (Disability disability : patient.getDisabilities()) {
				DisabilityDto disabilityDto = new DisabilityDto();
				disabilityDto.setDisability(disability.getDisability());
				disabilityDto.setId(disability.getId());
				disabilities.add(disabilityDto);
			}
			patientDto.setDisabilities(disabilities);
		}
		return patientDto;
	}

	@Override
	public void savePatient(PatientDto userDto) {
		Patient patient = new Patient();
		patient.setFirstName(userDto.getFirstName());
		patient.setLastName(userDto.getLastName());
		patient.setEmail(userDto.getEmail());
		patient.setRole(userDto.getRole());
		patient.setId(userDto.getId());
		if (null == userDto.getPassword()) {
			Optional<Patient> p1 = patientRepository.findById(patient.getId());
			if (p1.isPresent()) {
				patient.setPassword(p1.get().getPassword());
			}
		} else
			patient.setPassword(passwordEncoder.encode(userDto.getPassword()));
		if (null != userDto.getDisabilities() && !userDto.getDisabilities().isEmpty()) {
			for (DisabilityDto disabilityDto : userDto.getDisabilities()) {
				Disability disability = new Disability();
				disability.setDisability(disabilityDto.getDisability());
				disability.setId(disabilityDto.getId());
				patient.addDisabilities(disability);
			}
		}
		patientRepository.save(patient);
	}

	@Override
	public PatientDto findUserById(long patientId) {
		Optional<Patient> patient = patientRepository.findById(patientId);
		if (!patient.isPresent()) {
			throw new RuntimeException("Patient with given id does not exist");
		}
		return convertToPatientDto(patient.get());

	}

	@Override
	public void deletePatientDisability(long disabilityId, PatientDto patientDto) {
		DisabilityDto disabilityDto = disabilityService.getDisabilityById(disabilityId);
		if (null == disabilityDto) {
			throw new RuntimeException("Disability with given id does not exist");
		}
		patientDto.getDisabilities().remove(disabilityDto);
		savePatient(patientDto);
	}
}
