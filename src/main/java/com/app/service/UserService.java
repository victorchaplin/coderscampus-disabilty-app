package com.app.service;

import java.util.List;

import com.app.dao.model.UserDto;

public interface UserService {
	void saveUser(UserDto userDto);

	UserDto findUserByEmail(String email);

	List<UserDto> findAllUsers();

	void updateUser(UserDto existingUser);

	void deleteUser(long id);
}
