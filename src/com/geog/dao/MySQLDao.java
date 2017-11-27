package com.geog.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.geog.model.City;
import com.geog.model.Country;
import com.geog.model.Region;
import com.geog.util.Pages;

public class MySQLDao {

	private Connection connection; // the actual DB connection.

	public MySQLDao() {
		connect();
	}

	public Connection getConnection() {
		return connection;
	}

	public String addCountry(final Country country) throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement("INSERT INTO COUNTRY VALUES (?,?,?)");
		stmt.setString(1, country.getCode());
		stmt.setString(2, country.getName());
		stmt.setString(3, country.getDetails());
		stmt.executeUpdate();

		return "countries"; // navigate to countries page on success
	}

	// region methods

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

	// city methods

	public List<City> getAllCities() throws SQLException {
		final Statement stmt = connection.createStatement();
		final String queryString = "SELECT * FROM CITY";
		final ResultSet rs = stmt.executeQuery(queryString);

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
			cities.add(city);
		}
		return cities;
	}

	public String addCity(City city) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO CITY VALUES (?, ?, ?, ?, ?, ?, ?)");
		stmt.setString(1, city.getCode());
		stmt.setString(2, city.getCountryCode());
		stmt.setString(3, city.getRegCode());
		stmt.setString(4, city.getName());
		stmt.setLong(5, city.getPopulation());
		stmt.setBoolean(6, city.getIsCoastal());
		stmt.setDouble(7, city.getAreaKm());
		stmt.execute();

		return Pages.CITIES;

	}

	private void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/geography", "root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Country> loadAllCountries() throws SQLException {
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

	public String executeUpdate(Country country) throws SQLException {
		PreparedStatement stmt = connection
				.prepareStatement("UPDATE COUNTRY SET co_name=?, co_details=? WHERE co_code=?");

		stmt.setString(1, country.getName());
		stmt.setString(2, country.getDetails());
		stmt.setString(3, country.getCode());
		stmt.executeUpdate();
		return Pages.COUNTRIES;
	}

	public String deleteCountry(Country country) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("DELETE FROM COUNTRY WHERE co_code=?");
		stmt.setString(1, country.getCode());
		stmt.execute();
		return Pages.COUNTRIES;
	}

	public String addRegion(Region region) throws SQLException {
		// TODO Auto-generated method stub
		String query = "INSERT INTO REGION VALUES (?, ?, ?, ?)";
		PreparedStatement stmt = connection.prepareStatement(query);

		stmt.setString(1, region.getCountryCode());
		stmt.setString(2, region.getCode());
		stmt.setString(3, region.getName());
		stmt.setString(4, region.getDescription());

		stmt.execute();
		return Pages.REGIONS;
	}
}
