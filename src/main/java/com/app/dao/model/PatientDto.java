package com.app.dao.model;

import java.util.List;

public class PatientDto extends UserDto {
	private List<DisabilityDto> disabilities;

	/**
	 * @return the disabilities
	 */
	public List<DisabilityDto> getDisabilities() {
		return disabilities;
	}

	/**
	 * @param disabilities the disabilities to set
	 */
	public void setDisabilities(List<DisabilityDto> disabilities) {
		this.disabilities = disabilities;
	}

}
