package com.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.model.DisabilityDto;
import com.app.model.Disability;
import com.app.repositoy.DisabilityRepository;
import com.app.service.DisabilityService;

@Service
public class DisabilityServiceImpl implements DisabilityService {
	@Autowired
	private DisabilityRepository disabilityRepository;

	@Override
	public void saveDisablity(DisabilityDto disabilityDto) {
		DisabilityDto disabilityDto2 = getDisabilityByName(disabilityDto.getDisability());
		if (null != disabilityDto2)
			throw new RuntimeException("Disability already exist");
		Disability disability = new Disability();
		disability.setDisability(disabilityDto.getDisability());
		disability.setId(disabilityDto.getId());
		disabilityRepository.save(disability);
	}

	@Override
	public void updateDisablity(DisabilityDto disabilityDto) {
		Disability disability = disabilityRepository.findById(disabilityDto.getId()).get();
		disability.setDisability(disabilityDto.getDisability());
		disabilityRepository.save(disability);
	}

	@Override
	public List<DisabilityDto> getAll() {
		List<DisabilityDto> list = new ArrayList<>();
		List<Disability> disabilities = disabilityRepository.findAll();
		for (Disability disability : disabilities) {
			DisabilityDto disabilityDto = new DisabilityDto();
			disabilityDto.setDisability(disability.getDisability());
			disabilityDto.setId(disability.getId());
			list.add(disabilityDto);
		}
		return list;
	}

	@Override
	public DisabilityDto getDisabilityByName(String disability) {
		Optional<Disability> optional = disabilityRepository.findByDisability(disability);
		if (optional.isPresent()) {
			Disability existingDisability = optional.get();
			DisabilityDto disabilityDto = new DisabilityDto();
			disabilityDto.setDisability(existingDisability.getDisability());
			disabilityDto.setId(existingDisability.getId());
			return disabilityDto;
		}
		return null;
	}

	@Override
	public void deleteDisability(long id) {
		Optional<Disability> existingDisability = disabilityRepository.findById(id);
		if (existingDisability.isPresent())
			disabilityRepository.deleteById(id);
		else
			throw new RuntimeException("Disability with given Id does not exist");
	}

	@Override
	public DisabilityDto getDisabilityById(long id) {
		Optional<Disability> optional = disabilityRepository.findById(id);
		if (optional.isPresent()) {
			Disability existingDisability = optional.get();
			DisabilityDto disabilityDto = new DisabilityDto();
			disabilityDto.setDisability(existingDisability.getDisability());
			disabilityDto.setId(existingDisability.getId());
			return disabilityDto;
		}
		return null;
	}

}
