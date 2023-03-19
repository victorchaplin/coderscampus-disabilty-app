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
import org.springframework.web.bind.annotation.RequestParam;

import com.app.dao.model.DisabilityDto;
import com.app.dao.model.UserDto;
import com.app.service.DisabilityService;
import com.app.service.UserService;

import jakarta.validation.Valid;

@Controller
public class DisabilityWebController {
	@Autowired
	private DisabilityService disabilityService;
	@Autowired
	private UserService userService;

	@GetMapping("/admindisabilities")
	public String getDisabilities(Model model) { // comes from HTML page
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		UserDto user = userService.findUserByEmail(username);
		model.addAttribute("user", user);
		DisabilityDto disability = new DisabilityDto();
		model.addAttribute("disabilityObj", disability);
		List<DisabilityDto> list = disabilityService.getAll();
		model.addAttribute("disabilities", list);
		return "admin/disability";
	}

	@GetMapping("/disabilityedit")
	public String editDisability(@Valid @RequestParam long id, Model model) {
		DisabilityDto existingDisabilityDto = disabilityService.getDisabilityById(id);
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		UserDto user = userService.findUserByEmail(username);
		model.addAttribute("user", user);
		if (null == existingDisabilityDto) {
			List<DisabilityDto> list = disabilityService.getAll();
			model.addAttribute("disabilities", list);
			return "redirect:/admindisabilities?error";
		}
		model.addAttribute("disabilityObj", existingDisabilityDto);
		return "admin/editdisability";
	}

	@PostMapping("/disability/save")
	public String saveDisability(@Valid @ModelAttribute("disabilityObj") DisabilityDto disabilityDto,
			BindingResult result, Model model) {
		DisabilityDto existingDisabilityDto = disabilityService.getDisabilityByName(disabilityDto.getDisability());
		if (null != existingDisabilityDto) {
			result.rejectValue("disability", null, "Disability with given name already exist.");
		}
		if (result.hasErrors()) {
			List<DisabilityDto> list = disabilityService.getAll();
			model.addAttribute("disabilities", list);
			return "redirect:/admindisabilities?error";
		}
		disabilityService.saveDisablity(disabilityDto);
		List<DisabilityDto> list = disabilityService.getAll();
		model.addAttribute("disabilities", list);
		return "redirect:/admindisabilities?success";
	}

	@PostMapping("/disability/update")
	public String updateDisability(@Valid @ModelAttribute("disabilityObj") DisabilityDto disabilityDto,
			BindingResult result, Model model) {
		DisabilityDto existingDisabilityDto = disabilityService.getDisabilityById(disabilityDto.getId());
		if (null == existingDisabilityDto) {
			result.rejectValue("disability", null, "Disability with diven name does not exist.");
		}
		if (result.hasErrors()) {
			List<DisabilityDto> list = disabilityService.getAll();
			model.addAttribute("disabilities", list);
			return "redirect:/admindisabilities?error";
		}
		existingDisabilityDto.setDisability(disabilityDto.getDisability());
		disabilityService.updateDisablity(existingDisabilityDto);
		List<DisabilityDto> list = disabilityService.getAll();
		model.addAttribute("disabilities", list);
		return "redirect:/admindisabilities?success";
	}

}
