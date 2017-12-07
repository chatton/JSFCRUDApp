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
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.geog.model.City;
import com.geog.model.Country;
import com.geog.model.Region;
import com.geog.util.Pages;

/*
 * MySql Data base access object, provides access to the SQL database.
 */
public class MySQLDao implements SqlDAO {

	private Connection connection; // the actual DB connection.

	public MySQLDao() throws SQLException {
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
		final ResultSet rs = stmt.executeQuery(
				"SELECT * FROM CITY INNER JOIN COUNTRY ON COUNTRY.CO_CODE = CITY.CO_CODE INNER JOIN REGION ON REGION.REG_CODE = CITY.REG_CODE");
		
		
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
	private void connect() throws SQLException {
		try {
			Context context = new InitialContext();
			String jndiName = "java:comp/env/jdbc/geography";
			DataSource mysqlDS = (DataSource) context.lookup(jndiName);
			connection = mysqlDS.getConnection();
		} catch (NamingException e) {
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


//mysql> SELECT * FROM CITY INNER JOIN REGION ON CITY.REG_CODE = REGION.REG_CODE INNER JOIN COUNTRY ON COUNTRY.CO_CODE = REGION.CO_CODE;
//+----------+---------+----------+----------------+------------+-----------+---------+---------+----------+------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+---------+--------------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
//| cty_code | co_code | reg_code | cty_name       | population | isCoastal | areaKM  | co_code | reg_code | reg_name         | reg_desc                                                                                                                                                                                                            | co_code | co_name                  | co_details                                                                                                                                                                                                                            |
//+----------+---------+----------+----------------+------------+-----------+---------+---------+----------+------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+---------+--------------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
//| GC       | GIB     | GR       | Gibberish City |     100000 | false     |  500.00 | GIB     | GR       | Gibberish Region | It's a gibberish region                                                                                                                                                                                             | GIB     | A Gibberish Name         | It's a REALLY gibberish country - updated                                                                                                                                                                                             |
//| DUB      | IRL     | DUB      | Dublin         |     553165 | true      |  114.99 | IRL     | DUB      | Co. Dublin       | County Dublin is the most populous county in Ireland. It is divided into four administrative areas: Dublin city, Dun Laoghaire?Rathdown, Fingal and South Dublin.                                                   | IRL     | Republic of Ireland      | Ireland also described as the Republic of Ireland (Poblacht na h…ireann), is a sovereign state in north-western Europe occupying 26 of 32 counties of the island of Ireland.                                                          |
//| SWO      | IRL     | DUB      | Swords         |      68683 | true      |   11.35 | IRL     | DUB      | Co. Dublin       | County Dublin is the most populous county in Ireland. It is divided into four administrative areas: Dublin city, Dun Laoghaire?Rathdown, Fingal and South Dublin.                                                   | IRL     | Republic of Ireland      | Ireland also described as the Republic of Ireland (Poblacht na h…ireann), is a sovereign state in north-western Europe occupying 26 of 32 counties of the island of Ireland.                                                          |
//| GAL      | IRL     | GAL      | Galway         |      79934 | true      |   53.35 | IRL     | GAL      | Co. Galway       | County Galway is a county in the West of Ireland. It is part of the province of Connacht. There are several Irish-speaking areas in the west of the county.                                                         | IRL     | Republic of Ireland      | Ireland also described as the Republic of Ireland (Poblacht na h…ireann), is a sovereign state in north-western Europe occupying 26 of 32 counties of the island of Ireland.                                                          |
//| GOR      | IRL     | GAL      | Gort           |       2644 | false     |   19.91 | IRL     | GAL      | Co. Galway       | County Galway is a county in the West of Ireland. It is part of the province of Connacht. There are several Irish-speaking areas in the west of the county.                                                         | IRL     | Republic of Ireland      | Ireland also described as the Republic of Ireland (Poblacht na h…ireann), is a sovereign state in north-western Europe occupying 26 of 32 counties of the island of Ireland.                                                          |
//| LOU      | IRL     | GAL      | Loughrea       |       5057 | false     |   25.35 | IRL     | GAL      | Co. Galway       | County Galway is a county in the West of Ireland. It is part of the province of Connacht. There are several Irish-speaking areas in the west of the county.                                                         | IRL     | Republic of Ireland      | Ireland also described as the Republic of Ireland (Poblacht na h…ireann), is a sovereign state in north-western Europe occupying 26 of 32 counties of the island of Ireland.                                                          |
//| ATH      | IRL     | WMH      | Athlone        |      21349 | false     |   30.00 | IRL     | WMH      | Co. Westmeath    | County Westmeath is a county in the province of Leinster. It originally formed part of the historic Kingdom of Meath.                                                                                               | IRL     | Republic of Ireland      | Ireland also described as the Republic of Ireland (Poblacht na h…ireann), is a sovereign state in north-western Europe occupying 26 of 32 counties of the island of Ireland.                                                          |
//| MUL      | IRL     | WMH      | Mullingar      |      20928 | false     |   28.23 | IRL     | WMH      | Co. Westmeath    | County Westmeath is a county in the province of Leinster. It originally formed part of the historic Kingdom of Meath.                                                                                               | IRL     | Republic of Ireland      | Ireland also described as the Republic of Ireland (Poblacht na h…ireann), is a sovereign state in north-western Europe occupying 26 of 32 counties of the island of Ireland.                                                          |
//| MAR      | UK      | KNT      | Margate        |      61223 | true      |   23.40 | UK      | KNT      | Kent             | Kent is a county in South East England and one of the home counties. It borders Greater London to the north west, Surrey to the west and East Sussex to the south west.                                             | UK      | United Kingdom           | The United Kingdom of Great Britain and Northern Ireland, commonly known as the United Kingdom (UK) or Britain, is a sovereign country in western Europe.                                                                             |
//| SAN      | UK      | KNT      | Sandwich       |       4985 | false     |   13.00 | UK      | KNT      | Kent             | Kent is a county in South East England and one of the home counties. It borders Greater London to the north west, Surrey to the west and East Sussex to the south west.                                             | UK      | United Kingdom           | The United Kingdom of Great Britain and Northern Ireland, commonly known as the United Kingdom (UK) or Britain, is a sovereign country in western Europe.                                                                             |
//| LON      | UK      | LON      | London         |    8673713 | false     | 1572.00 | UK      | LON      | London           | London is the capital and most populous city of England and the United Kingdom.Standing on the River Thames in the south east of the island of Great Britain, London has been a major settlement for two millennia. | UK      | United Kingdom           | The United Kingdom of Great Britain and Northern Ireland, commonly known as the United Kingdom (UK) or Britain, is a sovereign country in western Europe.                                                                             |
//| hhh      | USA     | 124      | 123            |          0 | false     |    0.00 | USA     | 124      | USA 2            |                                                                                                                                                                                                                     | USA     | United States of America | The United States of America , commonly known as the United States (U.S.) or America, is a constitutional federal republic composed of 50 states, a federal district, five major self-governing territories, and various possessions. |
//| ALB      | USA     | NYK      | Albany         |      98469 | false     |   56.20 | USA     | NYK      | New York         | New York is a state in the northeastern United States. New York was one of the original thirteen colonies that formed the United States.                                                                            | USA     | United States of America | The United States of America , commonly known as the United States (U.S.) or America, is a constitutional federal republic composed of 50 states, a federal district, five major self-governing territories, and various possessions. |
//| CTY      | USA     | NYK      | A new new york |      50000 | false     |   25.65 | USA     | NYK      | New York         | New York is a state in the northeastern United States. New York was one of the original thirteen colonies that formed the United States.                                                                            | USA     | United States of America | The United States of America , commonly known as the United States (U.S.) or America, is a constitutional federal republic composed of 50 states, a federal district, five major self-governing territories, and various possessions. |
//| GVL      | USA     | NYK      | Greenville     |       3739 | false     |   39.10 | USA     | NYK      | New York         | New York is a state in the northeastern United States. New York was one of the original thirteen colonies that formed the United States.                                                                            | USA     | United States of America | The United States of America , commonly known as the United States (U.S.) or America, is a constitutional federal republic composed of 50 states, a federal district, five major self-governing territories, and various possessions. |
//| NY2      | USA     | NYK      | New York 2     |       1000 | false     |   50.00 | USA     | NYK      | New York         | New York is a state in the northeastern United States. New York was one of the original thirteen colonies that formed the United States.                                                                            | USA     | United States of America | The United States of America , commonly known as the United States (U.S.) or America, is a constitutional federal republic composed of 50 states, a federal district, five major self-governing territories, and various possessions. |
//| NYK      | USA     | NYK      | New York       |    8537673 | true      |  468.48 | USA     | NYK      | New York         | New York is a state in the northeastern United States. New York was one of the original thirteen colonies that formed the United States.                                                                            | USA     | United States of America | The United States of America , commonly known as the United States (U.S.) or America, is a constitutional federal republic composed of 50 states, a federal district, five major self-governing territories, and various possessions. |
//+----------+---------+----------+----------------+------------+-----------+---------+---------+----------+------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+---------+--------------------------+---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
//17 rows in set (0.00 sec)