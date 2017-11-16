package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import beans.Country;
import beans.Region;
import dao.dbinterfaces.CountryDBInterface;
import dao.dbinterfaces.RegionDBInterface;

public class MySQLDao {

	private Connection connection; // the actual DB connection.
	private final CountryDBInterface countryDBinterface; // manages country related db interactions.
	private final RegionDBInterface regionDBinterface; // region related db interactions.

	public MySQLDao() {
		connect();
		this.countryDBinterface = new CountryDBInterface();
		this.regionDBinterface = new RegionDBInterface();
	}

	// country methods
	public List<Country> getAllCountries() throws SQLException {
		return countryDBinterface.getAllCountries(connection);
	}

	public String addCountry(Country country) throws SQLException {
		return countryDBinterface.add(country, connection);
	}

	// region methods

	public List<Region> getAllRegions() throws SQLException {
		return regionDBinterface.getAllRegions(connection);
	}

	private void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/geography", "root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void disconnect() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
