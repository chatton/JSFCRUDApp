package com.geog.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.geog.model.City;
import com.geog.model.Country;
import com.geog.model.Region;
import com.geog.util.Pages;

/*
 * MySql Data base access object, provides access to the SQL database.
 */
public class MySQLDao {

	private Connection connection; // the actual DB connection.

	public MySQLDao() {
		connect(); // perform the initial connection.
	}

	public Connection getConnection() {
		return connection;
	}

	/*
	 * Adds a country to the database.
	 */
	public String addCountry(final Country country) throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement("INSERT INTO COUNTRY VALUES (?,?,?)");
		stmt.setString(1, country.getCode());
		stmt.setString(2, country.getName());
		stmt.setString(3, country.getDetails());
		stmt.executeUpdate();
		return Pages.COUNTRIES; // navigate to countries page on success
	}

	/*
	 * return a list of Region objects created from values taken from the database.
	 */
	public List<Region> getAllRegions() throws SQLException {
		final Statement stmt = connection.createStatement();
		final String queryString = "SELECT * FROM REGION";
		final ResultSet rs = stmt.executeQuery(queryString);
		final List<Region> regions = new ArrayList<>();
		while (rs.next()) {
			final Region region = new Region();
			region.setCode(rs.getString("reg_code"));
			region.setCountryCode(rs.getString("co_code"));
			region.setDescription(rs.getString("reg_desc"));
			region.setName(rs.getString("reg_name"));
			regions.add(region);
		}
		return regions;
	}

	/*
	 * builds up a list of all the cities in the database.
	 */
	public List<City> getAllCities() throws SQLException {
		final Statement stmt = connection.createStatement();
//		final String queryString = "SELECT * FROM CITY";
		final ResultSet rs = stmt.executeQuery("SELECT * FROM CITY INNER JOIN COUNTRY ON COUNTRY.CO_CODE = CITY.CO_CODE INNER JOIN REGION ON REGION.REG_CODE = CITY.REG_CODE");
		final List<City> cities = new ArrayList<>();
		while (rs.next()) {
			final City city = new City();
			city.setCode(rs.getString("cty_code"));
			city.setAreaKm(rs.getDouble("areaKM"));
			city.setIsCoastal(rs.getBoolean("isCoastal"));
			city.setCountryCode(rs.getString("co_code"));
			city.setName(rs.getString("cty_name"));
			city.setPopulation(rs.getLong("population"));
			city.setRegCode(rs.getString("reg_code"));
			city.setRegionName(rs.getString("reg_name"));
			city.setCountryName(rs.getString("co_name"));
			cities.add(city);
		}
		return cities;
	}

	/*
	 * adds a city to the database.
	 */
	public String addCity(City city) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO CITY VALUES (?, ?, ?, ?, ?, ?, ?)");
		stmt.setString(1, city.getCode());
		stmt.setString(2, city.getCountryCode());
		stmt.setString(3, city.getRegCode());
		stmt.setString(4, city.getName());
		stmt.setLong(5, city.getPopulation());
		stmt.setString(6, city.getIsCoastal() ? "true" : "false");
		stmt.setDouble(7, city.getAreaKm());
		stmt.execute();
		return Pages.CITIES;
	}

	/*
	 * create a connection from the connection pool based on the configuration file.
	 */
	private void connect() {
		try {
			Context context = new InitialContext();
			String jndiName = "java:comp/env/jdbc/geography";
			DataSource mysqlDS = (DataSource) context.lookup(jndiName);
			connection = mysqlDS.getConnection();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * gets all the countries from the database.
	 */
	public List<Country> getAllCountries() throws SQLException {
		final Statement stmt = connection.createStatement();
		final ResultSet rs = stmt.executeQuery("SELECT * FROM COUNTRY");
		final List<Country> countries = new ArrayList<>();
		while (rs.next()) {
			final Country country = new Country();
			country.setCode(rs.getString("co_code"));
			country.setName(rs.getString("co_name"));
			country.setDetails(rs.getString("co_details"));
			countries.add(country);
		}
		return countries;
	}

	/*
	 * updates the provided countries details in the database.
	 */
	public String executeUpdate(final Country country) throws SQLException {
		final PreparedStatement stmt = connection
				.prepareStatement("UPDATE COUNTRY SET co_name=?, co_details=? WHERE co_code=?");

		stmt.setString(1, country.getName());
		stmt.setString(2, country.getDetails());
		stmt.setString(3, country.getCode());
		stmt.executeUpdate();
		return Pages.COUNTRIES;
	}

	/*
	 * deletes the given country from the database.
	 */
	public String deleteCountry(final Country country) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("DELETE FROM COUNTRY WHERE CO_CODE=?");
		stmt.setString(1, country.getCode());
		stmt.execute();
		return Pages.COUNTRIES;
	}

	/*
	 * adds the provided region object to the database.
	 */
	public String addRegion(final Region region) throws SQLException {
		final String query = "INSERT INTO REGION VALUES (?, ?, ?, ?)";
		final PreparedStatement stmt = connection.prepareStatement(query);

		stmt.setString(1, region.getCountryCode());
		stmt.setString(2, region.getCode());
		stmt.setString(3, region.getName());
		stmt.setString(4, region.getDescription());
		stmt.execute();

		return Pages.REGIONS;
	}
}
