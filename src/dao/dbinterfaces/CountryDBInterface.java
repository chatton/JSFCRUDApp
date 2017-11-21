package dao.dbinterfaces;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import beans.Country;

public class CountryDBInterface {

	private List<Country> allCountries = new ArrayList<>();
	
	public List<Country> loadAll(Connection connection) throws SQLException{
		Statement stmt = connection.createStatement();
		String queryString = "SELECT * FROM COUNTRY";
		ResultSet rs = stmt.executeQuery(queryString);

		List<Country> countries = new ArrayList<>();
		while (rs.next()) {
			Country country = new Country();
			country.setCode(rs.getString("co_code"));
			country.setName(rs.getString("co_name"));
			country.setDetails(rs.getString("co_details"));
			countries.add(country);
		}
		allCountries = countries;
		return countries;
	}
	
	public List<Country> getAllCountries() {
		return new ArrayList<>(allCountries);
	}

	public String add(Country country, Connection connection) throws SQLException {
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(String.format("INSERT INTO COUNTRY VALUES (\"%s\", \"%s\", \"%s\")", country.getCode(),
				country.getName(), country.getDetails()));

		return "countries"; // navigate to countries page on success
	}

	public String executeUpdate(Country country, Connection connection) throws SQLException{
		System.out.println(country.getName());
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(String.format("UPDATE COUNTRY SET co_name=\"%s\", co_details=\"%s\" WHERE co_code=\"%s\"", 
				country.getName(), country.getDetails(), country.getCode()));
		
		return "countries";
	}
}
