package com.app.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.dao.model.UserDto;
import com.app.model.User;
import com.app.repositoy.UserRepository;
import com.app.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void saveUser(UserDto userDto) {
		User user = new User();
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setRole(userDto.getRole());
		// encrypt the password using spring security
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		userRepository.save(user);

	}

	@Override
	public void updateUser(UserDto userDto) {
		User user = userRepository.findByEmail(userDto.getEmail());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setRole(userDto.getRole());
		// encrypt the password using spring security
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		userRepository.save(user);
	}

	@Override
	public UserDto findUserByEmail(String email) {
		User user = userRepository.findByEmail(email);
		if (null == user)
			throw new RuntimeException("User does not exist with given email.");
		return convertToUserDto(user);
	}

	private UserDto convertToUserDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		userDto.setRole(user.getRole());
		userDto.setId(user.getId());
		return userDto;
	}

	@Override
	public List<UserDto> findAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream().map((user) -> convertToUserDto(user)).collect(Collectors.toList());
	}

	@Override
	public void deleteUser(long id) {
		Optional<User> optional = userRepository.findById(id);
		if (!optional.isPresent()) {
			throw new RuntimeException("User with given id does not exist");
		}
		userRepository.deleteById(id);

	}

}
