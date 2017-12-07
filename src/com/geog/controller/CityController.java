package com.geog.controller;

import static com.geog.util.Messages.addGlobalMessage;
import static com.geog.util.Messages.addMessage;
import static com.geog.util.Util.anyFalse;
import static com.geog.util.Util.codeIsValid;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.geog.dao.MySQLDao;
import com.geog.dao.NullSQLDao;
import com.geog.dao.SqlDAO;
import com.geog.finders.CityFinder;
import com.geog.model.City;
import com.geog.model.SearchQueryOptions;
import com.geog.util.Pages;

/*
 * Controller class in charge of handling interactions
 * between the SQL database and the view for all things 
 * City related.
 */
@SessionScoped
@ManagedBean
public class CityController {

	private SqlDAO db;
	private City selected;
	private List<City> cities;
	private SearchQueryOptions options;

	public CityController() {
		try {
			this.db = new MySQLDao();
		} catch (SQLException e) {
			this.db = new NullSQLDao();
			addGlobalMessage("Error connecting to SQL database.");
		}
	}

	public void loadCities() {
		try {
			cities = db.getAllCities();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			cities = new ArrayList<>();
		}
	}

	public List<City> getCities() {
		return cities;
	}

	public String getDetailsFor(final City city) {
		this.selected = city;
		return Pages.CITY_DETAILS;
	}

	public City getSelected() {
		return selected;
	}

	public void setSelected(City selected) {
		this.selected = selected;
	}

	public String add(City city) {
		final boolean isValidCityCode = codeIsValid(city.getCode(), 4);
		if (!isValidCityCode) {
			addMessage("cityform:noCityCode", "City code is mandatory and must be < 4 characters.");
		}

		final boolean isValidCountryCode = codeIsValid(city.getCountryCode(), 4);
		if (!isValidCountryCode) {
			addMessage("cityform:noCountryCode", "Country code is mandatory and must be < 4 characters.");
		}

		final boolean isValidRegionCode = codeIsValid(city.getRegCode(), 4);
		if (!isValidRegionCode) {
			addMessage("cityform:noRegCode", "Region code is mandatory and must be < 4 characters.");
		}

		final boolean isValidCityName = codeIsValid(city.getName());
		if (!isValidCityName) {
			addMessage("cityform:noName", "City name is mandatory.");
		}

		if (anyFalse(isValidCityCode, isValidCountryCode, isValidRegionCode, isValidCityName)) {
			return Pages.ADD_CITY;
		}

		try {
			return db.addCity(city);
		} catch (SQLIntegrityConstraintViolationException e) {
			addGlobalMessage(String.format("ERROR attempting to add City: %s, Region: %s, and Country: %s.",
					city.getCode(), city.getRegCode(), city.getCountryCode()));
			return Pages.ADD_CITY;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return Pages.ADD_CITY;
		}

	}

	public String search(final SearchQueryOptions options) {
		this.options = options;
		return Pages.SEARCH_RESULTS;
	}

	public SearchQueryOptions getOptions() {
		return options;
	}

	public void setOptions(SearchQueryOptions options) {
		this.options = options;
	}

	public List<City> searchResults() {
		Connection conn = db.getConnection();
		if(conn != null) {
			return new CityFinder(db.getConnection()).find(options);
		} else {
			return new ArrayList<>();
		}
		
	}

}
