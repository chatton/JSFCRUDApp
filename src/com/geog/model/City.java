package com.geog.model;

import javax.faces.bean.ManagedBean;

/*
 * City class is a managed bean that represents a City from the SQL database
 */
@ManagedBean
public class City {

	private String code;
	private String countryCode;
	private String regCode;
	private String name;
	private long population;
	private boolean isCoastal;
	private double areaKm;
	
	
	// country name is a variable to store the data taken from the Country SQL table.
	private String countryName ="";
	
	// used to display the region name in the search page.
	private String regionName = "";

	// public no-args constructor for JSF to be able to construct instances.
	public City() {
	}

	/*
	 * getters and setters for JSF to be able to manipulate and access the fields.
	 */
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code.trim();
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode.trim();
	}

	public String getRegCode() {
		return regCode;
	}

	public void setRegCode(String regCode) {
		this.regCode = regCode.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public long getPopulation() {
		return population;
	}

	public void setPopulation(long population) {
		this.population = population;
	}

	public boolean getIsCoastal() {
		return isCoastal;
	}

	public void setIsCoastal(boolean isCoastal) {
		this.isCoastal = isCoastal;
	}

	public double getAreaKm() {
		return areaKm;
	}

	public void setAreaKm(double areaKm) {
		this.areaKm = areaKm;
	}

	public void setCountryName(String name) {
		this.countryName = name;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setRegionName(String name) {
		this.regionName = name;
	}
	
	public String getRegionName() {
		return this.regionName;
	}

}
