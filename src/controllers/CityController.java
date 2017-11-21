package controllers;

import java.sql.SQLException;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import beans.City;
import dao.MySQLDao;

@ApplicationScoped
@ManagedBean
public class CityController {
	private final MySQLDao db;

	public CityController() {
		this.db = new MySQLDao();
	}

	public List<City> getAll() {
		try {
			return db.getAllCities();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
