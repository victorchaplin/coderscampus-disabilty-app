package com.app.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.app.service.DisabilityService;

import jakarta.validation.Valid;

@RestController
public class DisabilityRestController {
	@Autowired
	private DisabilityService disabilityService;

	@DeleteMapping("/disability/{id}")
	public ResponseEntity<String> deleteDisability(@Valid @PathVariable("id") long id) {
		disabilityService.deleteDisability(id);
		return ResponseEntity.ok("success");
	}

}
