package com.geog.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.sound.midi.Soundbank;

import com.geog.dao.MySQLDao;
import com.geog.finders.CityFinder;
import com.geog.finders.DatabaseException;
import com.geog.model.City;
import com.geog.model.SearchQueryOptions;

@ApplicationScoped
@ManagedBean
public class CityController {

	private final MySQLDao db;
	private City selected;
	private List<City> cities;
	private SearchQueryOptions options;

	public CityController() {
		this.db = new MySQLDao();
	}

	public void loadCities() {
		try {
			cities = db.getAllCities();
		} catch (SQLException e) {
			e.printStackTrace();
			cities = new ArrayList<>();
		}
	}

	public List<City> getCities() {
		return cities;
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

	public String search(final SearchQueryOptions options) {
		this.options = options;
		System.out.println("Search(options)");
		return "search_results";
	}

	public SearchQueryOptions getOptions() {
		return options;
	}

	public void setOptions(SearchQueryOptions options) {
		this.options = options;
	}

	public List<City> searchResults() {
		System.out.println("Searching for results.");

		List<City> cities = new ArrayList<>();
		try {
			cities = new CityFinder(db.getConnection()).find(options);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return cities;

	}

}
