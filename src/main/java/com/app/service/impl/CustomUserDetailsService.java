package com.app.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.model.Patient;
import com.app.model.User;
import com.app.repositoy.PatientRepository;
import com.app.repositoy.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PatientRepository patientRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);

		if (user != null) {
			List<String> roleList = new ArrayList<>();
			roleList.add(user.getRole());
			Collection<? extends GrantedAuthority> mapRoles = roleList.stream()
					.map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
					mapRoles);
		} else {
			Optional<Patient> optional = patientRepository.findByEmail(email);
			if (optional.isPresent()) {
				Patient patient = optional.get();
				List<String> roleList = new ArrayList<>();
				roleList.add(patient.getRole());
				Collection<? extends GrantedAuthority> mapRoles = roleList.stream()
						.map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
				return new org.springframework.security.core.userdetails.User(patient.getEmail(), patient.getPassword(),
						mapRoles);
			}
		}
		throw new UsernameNotFoundException("Invalid username or password.");
	}
}
