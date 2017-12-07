package com.geog.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.geog.model.City;
import com.geog.model.Country;
import com.geog.model.Region;

public interface SqlDAO {

	/*
	 * Adds a country to the database.
	 */
	public String addCountry(final Country country) throws SQLException;

	/*
	 * return a list of Region objects created from values taken from the database.
	 */
	public List<Region> getAllRegions() throws SQLException;

	/*
	 * builds up a list of all the cities in the database.
	 */
	public List<City> getAllCities() throws SQLException;

	/*
	 * adds a city to the database.
	 */
	public String addCity(City city) throws SQLException;

	/*
	 * gets all the countries from the database.
	 */
	public List<Country> getAllCountries() throws SQLException;

	/*
	 * updates the provided countries details in the database.
	 */
	public String executeUpdate(final Country country) throws SQLException;

	/*
	 * deletes the given country from the database.
	 */
	public String deleteCountry(final Country country) throws SQLException;

	/*
	 * adds the provided region object to the database.
	 */
	public String addRegion(final Region region) throws SQLException;

	public Connection getConnection();
}
