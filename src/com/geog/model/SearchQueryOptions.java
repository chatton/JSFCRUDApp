package com.geog.model;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "search")
public class SearchQueryOptions {

	private String lessThanOrGreater;
	private long population;
	private boolean isCoastal;
	private String countryCode;

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
		this.countryCode = countryCode;
	}

}
