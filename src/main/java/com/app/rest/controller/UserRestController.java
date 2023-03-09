package com.app.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.app.service.UserService;

@RestController
public class UserRestController {
	@Autowired
	private UserService userService;

	@DeleteMapping("/user/{id}")
	public ResponseEntity<String> deletePatient(@PathVariable("id") long id) {
		userService.deleteUser(id);
		return ResponseEntity.ok("success");
	}
}
