package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import beans.Country;

@ManagedBean
public class CountryManager {

	private Connection connection;

	public CountryManager() {
		connection = null;
	}
	
	public void update(Country country) {
		System.out.println(country.getName());
	}
	
	
	private void disconnect() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/geography", "root", "");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Override
	public String add(Country country) {
		connect();
		Statement stmt;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(String.format("INSERT INTO COUNTRY VALUES (\"%s\", \"%s\", \"%s\")", country.getCode(), country.getName(), country.getDetails()));
		} catch (SQLException e) {
			e.printStackTrace();
			return "add_country"; // don't go anywhere, stay on current page.
		}
		disconnect();
		return "countries"; // navigate to countries page on success
	}	
	
//	@Override
	public List<Country> getAll()  {
		connect();
		Statement stmt;
		try {
			stmt = connection.createStatement();
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
			return countries;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<>(); // no elements if there was an error 
	}


//	@Override
//	public void delete(Country item) {
//		
//	}

}
