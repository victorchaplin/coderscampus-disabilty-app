package com.app.web.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.app.dao.model.DisabilityDto;
import com.app.dao.model.PatientDto;
import com.app.dao.model.UserDto;
import com.app.service.DisabilityService;
import com.app.service.PatientService;
import com.app.service.UserService;

import jakarta.validation.Valid;

@Controller
public class PatientWebController {
	@Autowired
	private PatientService patientService;
	@Autowired
	private UserService userService;
	@Autowired
	private DisabilityService disabilityService;

	@GetMapping("/patientview")
	public String editPatient(@Valid @RequestParam long id, Model model) {

		PatientDto patientDto = patientService.findUserById(id);
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		UserDto user = userService.findUserByEmail(username);
		model.addAttribute("user", user);
		if (null == patientDto) {
			List<PatientDto> list = patientService.getAllPatients();
			model.addAttribute("patients", list);
			return "redirect:/adminpatients?error";
		}
		model.addAttribute("patient", patientDto);
		return "admin/viewpatient";
	}

	@GetMapping("/patientdisabilities")
	public String viewPatientDisabilities(Model model) {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		List<DisabilityDto> disabilityDtos = disabilityService.getAll();
		DisabilityDto disabilityDto = new DisabilityDto();
		model.addAttribute("disabilityObj", disabilityDto);

		PatientDto patientDto = patientService.findUserByEmail(username);
		if (null == patientDto) {
			return "redirect:/patientshome?error";
		}
		if (patientDto.getDisabilities() != null && !patientDto.getDisabilities().isEmpty()) {
			disabilityDtos.removeAll(patientDto.getDisabilities());
		}
		model.addAttribute("disabilities", disabilityDtos);
		model.addAttribute("user", patientDto);

		model.addAttribute("patient", patientDto);
		return "patients/disability";
	}

	@GetMapping("/adminpatients")
	public String getDisabilities(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		UserDto user = userService.findUserByEmail(username);
		model.addAttribute("user", user);
		PatientDto disability = new PatientDto();
		model.addAttribute("patient", disability);
		List<PatientDto> list = patientService.getAllPatients();
		model.addAttribute("patients", list);
		return "admin/patient";
	}

	@PostMapping("/register/save/patient")
	public String registerPatient(@Valid @ModelAttribute("patient") PatientDto userDto, BindingResult result,
			Model model) {
		PatientDto existingUser = null;
		try {
			existingUser = patientService.findUserByEmail(userDto.getEmail());
		} catch (Exception e) {

		}

		if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
			result.rejectValue("email", null, "There is already an account registered with the same email");
		}

		if (result.hasErrors()) {
			model.addAttribute("patient", userDto);
			return "/register?error";
		}
		userDto.setRole("patient");
		patientService.savePatient(userDto);
		return "redirect:/register?patientsuccess";
	}

	@PostMapping("/patient/disability/save")
	public String savePatientDisability(@Valid @ModelAttribute("disabilityObj") DisabilityDto disabilityDto,
			BindingResult result, Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		PatientDto patientDto = patientService.findUserByEmail(username);
		DisabilityDto disabilityDto2 = disabilityService.getDisabilityById(disabilityDto.getId());
		if (patientDto.getDisabilities() == null || patientDto.getDisabilities().isEmpty()) {
			patientDto.setDisabilities(new ArrayList<DisabilityDto>());
		}
		patientDto.getDisabilities().add(disabilityDto2);
		patientService.savePatient(patientDto);
		return "redirect:/patientdisabilities?patientsuccess";
	}

	@GetMapping("/patientshome")
	public String userhome(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		PatientDto user = patientService.findUserByEmail(username);
		model.addAttribute("user", user);
		return "patients/home";
	}

	@PostMapping("/patient/update")
	public String profileSave(@Valid @ModelAttribute("user") PatientDto userDto, BindingResult result, Model model) {
		PatientDto existingUser = null;
		try {
			existingUser = patientService.findUserByEmail(userDto.getEmail());
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
			if ("patient".equalsIgnoreCase(userDto.getRole()))
				return "redirect:/patientshome?error";
			return "redirect:/adminhome?error";
		}

		patientService.savePatient(existingUser);
		return "redirect:/patientshome?success";
	}
}
