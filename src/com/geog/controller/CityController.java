package com.geog.controller;

import java.sql.SQLException;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.geog.dao.MySQLDao;
import com.geog.model.City;

@ApplicationScoped
@ManagedBean
public class CityController {

	private final MySQLDao db;
	private City selected;

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

	public String getDetailsFor(City city) {
		this.selected = city;
		return "city_details";
	}

	public City getSelected() {
		return selected;
	}

	public void setSelected(City selected) {
		this.selected = selected;
	}

	public String add(City city) {
		try {
			return db.addCity(city);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return "cities";
		}
	}

}
