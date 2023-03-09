package com.app.service;

import java.util.List;

import com.app.dao.model.DisabilityDto;

public interface DisabilityService {

	public void saveDisablity(DisabilityDto disabilityDto);

	public List<DisabilityDto> getAll();

	public DisabilityDto getDisabilityByName(String disability);

	public void deleteDisability(long id);

	void updateDisablity(DisabilityDto disabilityDto);

	public DisabilityDto getDisabilityById(long id);

}
