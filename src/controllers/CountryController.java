package controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import beans.Country;
import dao.MySQLDao;
import dao.dbinterfaces.CountryDBInterface;

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

	public String executeUpdate() {
		boolean validName = !selected.getName().trim().isEmpty();
		if (!validName) {
			
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
