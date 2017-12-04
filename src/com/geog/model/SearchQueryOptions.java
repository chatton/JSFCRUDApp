package com.geog.model;

import javax.faces.bean.ManagedBean;

/*
 * managed bean to represent the search options
 * when finding a city. These are required to construct
 * the SQL query and return the result set.
 */
@ManagedBean(name = "search") // call it "search" in the jsf fields
public class SearchQueryOptions {

	private String lessThanOrGreater;
	private long population;
	private boolean isCoastal;
	private String countryCode;

	// public no-args for jsf
	public SearchQueryOptions() {}
	
	public String getLessThanOrGreater() {
		return lessThanOrGreater;
	}

	public void setLessThanOrGreater(String lessThanOrGreater) {
		this.lessThanOrGreater = lessThanOrGreater;
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

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode.trim();
	}

}
