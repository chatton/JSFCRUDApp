package com.geog.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/*
 * Represents a Country from the SQL database.
 */
@ManagedBean
public class Country {

	private String code = "";
	private String name = "";
	private String details = "";

	// no args constructor for jsf
	public Country() {}

	public Country(String code, String name, String details) {
		this.code = code;
		this.name = name;
		this.details = details;
	}

	/*
	 * getters and setters for JSF
	 */
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

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details.trim();
	}
}
