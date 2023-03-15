package com.app.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.app.dao.model.UserDto;
import com.app.service.UserService;

import jakarta.validation.Valid;

@Controller
public class UserWebController {
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String home() {
		return "login";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/profile/save")
	public String profileSave(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
		UserDto existingUser = null;
		try {
			existingUser = userService.findUserByEmail(userDto.getEmail());
		} catch (Exception e) {
		}

		if (existingUser == null) {
			result.rejectValue("email", null, "Account does not exist.");
		} else {
			existingUser.setFirstName(userDto.getFirstName());
			existingUser.setLastName(userDto.getLastName());
			if (null != userDto.getPassword() && !userDto.getPassword().isEmpty())
				existingUser.setPassword(userDto.getPassword());
		}

		if (result.hasErrors()) {
			model.addAttribute("user", userDto);
			if ("user".equalsIgnoreCase(existingUser.getRole()))
				return "redirect:/userhome?error";
			return "redirect:/adminhome?error";
		}

		userService.updateUser(existingUser);
		if ("user".equalsIgnoreCase(existingUser.getRole()))
			return "redirect:/userhome?success";
		return "redirect:/adminhome?success";

	}

	@GetMapping("/adminhome")
	public String adminhome(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		UserDto user = userService.findUserByEmail(username);
		model.addAttribute("user", user);
		return "admin/home";
	}
	@GetMapping("/userhome")
	public String userHome(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		UserDto user = userService.findUserByEmail(username);
		model.addAttribute("user", user);
		return "users/home";
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		UserDto patient = new UserDto();
		model.addAttribute("patient", patient);
		UserDto user = new UserDto();
		model.addAttribute("user", user);
		return "register";
	}

	@PostMapping("/register/save/user")
	public String registerUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
		UserDto existingUser = null;
		try {
			existingUser = userService.findUserByEmail(userDto.getEmail());
		} catch (Exception e) {

		}

		if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
			result.rejectValue("email", null, "There is already an account registered with the same email");
		}

		if (result.hasErrors()) {
			model.addAttribute("user", userDto);
			return "/register";
		}
		userDto.setRole("user");
		userService.saveUser(userDto);
		return "redirect:/register?usersuccess";
	}

	@GetMapping("/adminusers")
	public String admiUsers(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		UserDto user = userService.findUserByEmail(username);   
		List<UserDto> users = userService.findAllUsers();
		users.remove(user);
		model.addAttribute("user", user);
		model.addAttribute("users", users);
		return "admin/user";
	}
}
