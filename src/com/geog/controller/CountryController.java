package com.geog.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.geog.dao.MySQLDao;
import com.geog.model.Country;
import com.geog.util.Pages;

import static com.geog.util.Messages.addMessage;
import static com.geog.util.Messages.addGlobalMessage;
import static com.geog.util.Util.anyFalse;
import static com.geog.util.Util.codeIsValid;

@ApplicationScoped
@ManagedBean
public class CountryController {

	private final MySQLDao db;
	private List<Country> countries;
	private Country selected;

	public CountryController() {
		db = new MySQLDao();
		countries = new ArrayList<>();
		selected = new Country();
	}

	public void loadCountries() {
		try {
			countries = db.loadAllCountries();
		} catch (SQLException e) {
			e.printStackTrace();

			countries = new ArrayList<>();
		}
	}

	public String update(Country country) {
		selected = country;
		return Pages.UPDATE_COUNTRY;
	}

	public String delete(Country country) {

		try {
			return db.deleteCountry(country);
		} catch (SQLException e) {
			return null;
		}

	}

	public String executeUpdate() {
		if (!codeIsValid(selected.getName())) {
			// don't perform any db operations with an invalid name.
			addGlobalMessage("Name must not be empty.");
			return Pages.UPDATE_COUNTRY;
		}

		try {
			return db.executeUpdate(selected);
		} catch (SQLException e) {
			return Pages.UPDATE_COUNTRY;
		}

	}

	private boolean hasUniqueCode(Country country) {
		return countries.stream().noneMatch(c -> c.getCode().equals(country.getCode()));
	}

	public String add(Country country) {
		final boolean validName = codeIsValid(country.getName());
		if (!validName) {
			addMessage("countryform:noName", "Name is mandatory.");
		}

		final boolean validCode = codeIsValid(country.getCode(), 4);
		if (!validCode) {
			addMessage("countryform:noCode", "Code is mandatory And must be < 4 characters");
		}

		if (anyFalse(validName, validCode)) {
			return Pages.ADD_COUNTRY;
		}

		if (!hasUniqueCode(country)) {
			addGlobalMessage("ERROR: Country Code: " + country.getCode() + " already exists.");
			return Pages.ADD_COUNTRY; // stay on add country page.
		}

		try {
			return db.addCountry(country);
		} catch (SQLException e) {
			return Pages.ADD_COUNTRY;
		}
	}

	public Country getSelected() {
		return selected;
	}

	public void setSelected(Country selected) {
		this.selected = selected;
	}

	public List<Country> getCountries() {
		return countries;
	}

}
