package com.geog.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.geog.model.City;
import com.geog.model.Country;
import com.geog.model.Region;

/*
 * a bare-bones implementation that does "nothing"
 * the purpose is to prevent null pointers when there's an
 * error connecting to the database.
 */
public class NullSQLDao implements SqlDAO {

	@Override
	public String addCountry(Country country) throws SQLException {
		return null;
	}

	@Override
	public List<Region> getAllRegions() throws SQLException {
		return new ArrayList<>();
	}

	@Override
	public List<City> getAllCities() throws SQLException {
		return new ArrayList<>();
	}

	@Override
	public String addCity(City city) throws SQLException {
		return null;
	}

	@Override
	public List<Country> getAllCountries() throws SQLException {
		return new ArrayList<>();
	}

	@Override
	public String executeUpdate(Country country) throws SQLException {
		return null;
	}

	@Override
	public String deleteCountry(Country country) throws SQLException {
		return null;
	}

	@Override
	public String addRegion(Region region) throws SQLException {
		return null;
	}

	@Override
	public Connection getConnection() {
		return null;
	}

}
