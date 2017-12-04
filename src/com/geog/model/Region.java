package com.geog.model;

import javax.faces.bean.ManagedBean;

/*
 * Represents a Region from the SQL database.
 */
@ManagedBean
public class Region {

	private String countryCode;
	private String code;
	private String name;
	private String description;

	// public no-args constructor for jsf
	public Region() {}

	/*
	 * getters and setters for jsf
	 */
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode.trim();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description.trim();
	}

}
