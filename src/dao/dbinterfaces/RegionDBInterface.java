package dao.dbinterfaces;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import beans.Region;

public class RegionDBInterface {

	public List<Region> getAllRegions(final Connection connection) throws SQLException {
		final Statement stmt = connection.createStatement();
		final String queryString = "SELECT * FROM REGION";
		final ResultSet rs = stmt.executeQuery(queryString);

		final List<Region> regions = new ArrayList<>();
		while (rs.next()) {
			Region region = new Region();
			region.setCode(rs.getString("reg_code"));
			region.setCountryCode(rs.getString("co_code"));
			region.setDescription(rs.getString("reg_desc"));
			region.setName(rs.getString("reg_name"));
			regions.add(region);
		}
		return regions;
	}
}
