package com.geog.finders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geog.model.City;
import com.geog.model.SearchQueryOptions;

public class CityFinder implements Finder<City> {
	private final static Map<String, String> symbolMap;

	static { // map the value from the view to the SQL
		symbolMap = new HashMap<>();
		symbolMap.put("gt", ">");
		symbolMap.put("lt", "<");
		symbolMap.put("e", "=");
	}

	private String lessOrGreaterThan = "<";
	private long population;
	private String countryCode;
	private String isCoastal;
	private final Connection connection;

	public CityFinder(final Connection connection) {
		this.connection = connection;
	}

	private CityFinder isCoastal(final boolean isCoastal) {
		this.isCoastal = isCoastal ? "true" : "false";
		return this;
	}

	private CityFinder countryCode(final String countryCode) {
		// % default will display all countries if no code is specified.
		this.countryCode = countryCode.isEmpty() ? "%" : countryCode;
		return this;
	}

	private CityFinder lessOrGeaterThan(final String symbol) {
		this.lessOrGreaterThan = symbolMap.get(symbol);
		return this;
	}

	private CityFinder population(final long population) {
		this.population = population;
		return this;
	}

	@Override
	public List<City> find(final SearchQueryOptions options) throws DatabaseException {
		try {

			countryCode(options.getCountryCode()).lessOrGeaterThan(options.getLessThanOrGreater())
					.population(options.getPopulation()).countryCode(options.getCountryCode())
					.isCoastal(options.getIsCoastal());

			// safe to do direct string formatting on query because this information isn't coming from user typing.
			String queryString = String.format(
					"SELECT * FROM CITY WHERE POPULATION %s ? AND CO_CODE LIKE ? AND ISCOASTAL LIKE ?",
					this.lessOrGreaterThan);

			PreparedStatement stmt = connection.prepareStatement(queryString);

			stmt.setLong(1, population);
			stmt.setString(2, countryCode);
			stmt.setString(3, isCoastal);

			ResultSet rs = stmt.executeQuery();
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
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new DatabaseException(e.getMessage());
		}
	}

}
