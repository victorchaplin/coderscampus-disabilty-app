package com.app.dao.model;

import java.util.Objects;

public class DisabilityDto {
	private long id;
	private String disability;

	/**
	 * @return the id
	 */
	public long getId() {   
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the disability
	 */
	public String getDisability() {
		return disability;
	}

	/**
	 * @param disability the disability to set
	 */
	public void setDisability(String disability) {
		this.disability = disability;
	}

	@Override
	public int hashCode() {
		return Objects.hash(disability, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DisabilityDto other = (DisabilityDto) obj;
		return Objects.equals(disability, other.disability) && id == other.id;
	}

}
