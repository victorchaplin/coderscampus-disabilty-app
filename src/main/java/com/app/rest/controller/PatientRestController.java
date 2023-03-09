package com.app.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.app.dao.model.PatientDto;
import com.app.service.PatientService;

@RestController
public class PatientRestController {
	@Autowired
	private PatientService patientService;

	@DeleteMapping("/patient/{id}")
	public ResponseEntity<String> deletePatient(@PathVariable("id") long id) {
		patientService.deletePatient(id);
		return ResponseEntity.ok("success");
	}

	@DeleteMapping("/patient/disability/{id}")
	public ResponseEntity<String> deletePatientDisability(@PathVariable("id") long id) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		PatientDto patientDto = patientService.findUserByEmail(username);
		patientService.deletePatientDisability(id,patientDto);
		return ResponseEntity.ok("success");
	}
}
