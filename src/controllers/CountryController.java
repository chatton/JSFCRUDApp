package controllers;

import java.sql.SQLException;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import beans.Country;
import dao.MySQLDao;

@ApplicationScoped
@ManagedBean
public class CountryController {
	
	private final MySQLDao db;

	public CountryController() {
		db = new MySQLDao();
	}
	
	public String update(Country country) {
		System.out.println("Updating country: " + country.getName());
		return "update_country";
	}
	
	public String add(Country country) {
		try {
			return db.addCountry(country);
		} catch (SQLException e) {
			return "add_country"; // stay on add country page.
		}
	}
	
	public List<Country> getAll(){
		try {
			return db.getAllCountries();
		} catch (SQLException e) {
			 // h:message SQLCommunicationException
		}
		return null;
	}
}
