package controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import beans.Region;
import dao.MySQLDao;

@ApplicationScoped
@ManagedBean
public class RegionController {
	private final MySQLDao db;
	
	public RegionController() {
		this.db = new MySQLDao();
	}
	
	public List<Region> getAll(){
		try {
			return db.getAllRegions();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
}
