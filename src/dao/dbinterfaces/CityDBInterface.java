package dao.dbinterfaces;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import beans.City;

public class CityDBInterface {
	public List<City> getAllCities(final Connection connection) throws SQLException {
		final Statement stmt = connection.createStatement();
		final String queryString = "SELECT * FROM CITY";
		final ResultSet rs = stmt.executeQuery(queryString);

		final List<City> cities = new ArrayList<>();
		while (rs.next()) {
			City city = new City();
			city.setCityCode(rs.getString("cty_code"));
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
}
