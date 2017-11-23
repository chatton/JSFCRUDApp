package com.geog.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import com.geog.dao.MySQLDao;
import com.geog.model.Country;

@ApplicationScoped
@ManagedBean
public class CountryController {

	private final MySQLDao db;
	private List<Country> all;
	private Country selected;

	public CountryController() {
		db = new MySQLDao();
		all = new ArrayList<>();
		selected = new Country();
	}

	public List<Country> loadAll() {
		try {
			List<Country> countriesFromDb = db.loadAllCountries();
			all = countriesFromDb;
			return countriesFromDb;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String update(Country country) {
		selected = country;
		return "update_country";
	}

	public String delete(Country country) {

		try {
			return db.deleteCountry(country);
		} catch (SQLException e) {
			System.out.println("Failed with: " + e.getMessage());
			return null;
		}

	}

	public String executeUpdate() {
		boolean validName = !selected.getName().trim().isEmpty();

		if (!validName) {
			// don't perform any db operations with an invalid name.
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Name must not be empty."));
			return "update_country";
		}

		try {
			return db.executeUpdate(selected);
		} catch (SQLException e) {
			return "update_country";
		}

	}

	public String add(Country country) {
		try {
			return db.addCountry(country);
		} catch (SQLException e) {
			return "add_country"; // stay on add country page.
		}
	}

	public Country getSelected() {
		return selected;
	}

	public void setSelected(Country selected) {
		this.selected = selected;
	}

	// public List<Country> getAll() {
	// try {
	// return db.getAllCountries();
	// } catch (SQLException e) {
	// // h:message SQLCommunicationException
	// }
	// return null;
	// }
}
