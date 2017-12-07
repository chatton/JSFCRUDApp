package com.geog.controller;

import static com.geog.util.Messages.addGlobalMessage;
import static com.geog.util.Messages.addMessage;
import static com.geog.util.Util.anyFalse;
import static com.geog.util.Util.codeIsValid;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.geog.dao.MySQLDao;
import com.geog.dao.NullSQLDao;
import com.geog.dao.SqlDAO;
import com.geog.model.Country;
import com.geog.util.Pages;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/*
 * Controller class in charge of handling interactions
 * between the SQL database and the view for all things 
 * Country related.
 */
@SessionScoped
@ManagedBean
public class CountryController {

	private SqlDAO db;
	private List<Country> countries;
	private Country selected; // hold onto the country in order to update it.

	public CountryController() {
		try {
			db = new MySQLDao();
		} catch (SQLException e) {
			db = new NullSQLDao();
			addGlobalMessage("Error connecting to SQL database.");
		}
		countries = new ArrayList<>();
		selected = new Country();
	}

	public void loadCountries() {
		try {
			countries = db.getAllCountries();
		} catch (SQLException e) {
			
			countries = new ArrayList<>();
		}
	}

	/*
	 * saves the selected country so that it can be referenced in the jsf page.
	 */
	public String update(final Country country) {
		selected = country;
		return Pages.UPDATE_COUNTRY;
	}

	/*
	 * deletes the country from the sql db.
	 */
	public String delete(final Country country) {
		try {
			return db.deleteCountry(country);
		} catch (SQLException e) {
			addGlobalMessage("Error deleting country: " + country.getName()); // stays on the same page.
			return null;
		}
	}

	/*
	 * performs the actual update operation in the db.
	 */
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

	public String add(Country country) {

		final boolean validName = codeIsValid(country.getName());
		if (!validName) {
			addMessage("countryform:noName", "Name is mandatory.");
		}

		final boolean validCode = codeIsValid(country.getCode(), 4);
		if (!validCode) {
			addMessage("countryform:noCode", "Code is mandatory And must be < 4 characters");
		}

		// don't continue if either of the mandatory fields are not valid.
		if (anyFalse(validName, validCode)) {
			return Pages.ADD_COUNTRY;
		}

		try {
			return db.addCountry(country);
		} catch (MySQLIntegrityConstraintViolationException e) {
			addGlobalMessage("ERROR: Country Code: " + country.getCode() + " already exists.");
			return Pages.ADD_COUNTRY; // stay on add country page.
		} catch (SQLException e) {
			return Pages.ADD_COUNTRY;
		}
	}

	// JSF bean methods
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
