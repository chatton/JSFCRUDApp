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

/*
 * A city finder gives back a List of cities that match
 * the search criteria.
 */
public class CityFinder implements Finder<City> {
	// static as every CityFinder instance will have the same map.
	private final static Map<String, String> symbolMap;

	/*
	 * mapping of the values that are taken from the JSF form to the equivalent
	 * symbol that will be inserted into the SQL statement.
	 */
	static { // static initialization block is used to provide initial values.
		symbolMap = new HashMap<>();
		symbolMap.put("gt", ">");
		symbolMap.put("lt", "<");
		symbolMap.put("e", "=");
	}

	final Connection connection;

	public CityFinder(final Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<City> find(final SearchQueryOptions options) {
		try {

			final String lessOrGreaterThan = symbolMap.get(options.getLessThanOrGreater());
			final long population = options.getPopulation();
			final String countryCode = options.getCountryCode().isEmpty() ? "%" : options.getCountryCode();
			final String isCoastal = options.getIsCoastal() ? "TRUE" : "FALSE";

			final StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM CITY INNER JOIN COUNTRY ON COUNTRY.CO_CODE = CITY.CO_CODE INNER JOIN REGION ON CITY.REG_CODE = REGION.REG_CODE WHERE ");
			if (population != 0) { // ignore population filter if it's 0 - the user doesn't care about population.
				sb.append("POPULATION " + lessOrGreaterThan + " ? AND ");
			}
			sb.append("CITY.CO_CODE LIKE ? AND ISCOASTAL LIKE ?");

			final PreparedStatement stmt = connection.prepareStatement(sb.toString());

			/*
			 * include this index variable as we might not be including the population into
			 * the query.
			 */
			int index = 1;
			if (population != 0) {
				stmt.setLong(index++, population);
			}

			stmt.setString(index++, countryCode);
			stmt.setString(index, isCoastal);

			final ResultSet rs = stmt.executeQuery();
			final List<City> cities = new ArrayList<>();
			while(rs.next()) {
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
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return new ArrayList<>();
		}
	}
}