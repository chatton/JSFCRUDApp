package com.geog.controller;

import static com.geog.util.Messages.addGlobalMessage;
import static com.geog.util.Messages.addMessage;
import static com.geog.util.Util.anyFalse;
import static com.geog.util.Util.codeIsValid;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.geog.dao.MySQLDao;
import com.geog.model.Region;
import com.geog.util.Pages;
/*
 * Controller class in charge of handling interactions
 * between the SQL database and the view for all things 
 * Region related.
 */
@SessionScoped
@ManagedBean
public class RegionController {
	private final MySQLDao db;
	private List<Region> regions;

	public RegionController() {
		this.regions = new ArrayList<>();
		this.db = new MySQLDao();
	}

	public void loadRegions() {
		try {
			regions = db.getAllRegions();
		} catch (SQLException e) {
			regions = new ArrayList<>();
		}
	}

	public List<Region> getRegions() {
		return regions;
	}

	public String add(Region region) {

		boolean countryCodeIsValid = codeIsValid(region.getCountryCode(), 4);
		if (!countryCodeIsValid) {
			addMessage("regionform:noCountryCode", "Country code is mandatory. And must be < 4 characters");
		}

		boolean regionCodeIsValid = codeIsValid(region.getCode(), 4);
		if (!regionCodeIsValid) {
			addMessage("regionform:noCode", "Region code is mandatory. And must be < 4 characters.");
		}

		boolean regionNameIsValid = codeIsValid(region.getName());
		if (!regionNameIsValid) {
			addMessage("regionform:noName", "Name is mandatory.");
		}

		if (anyFalse(countryCodeIsValid, regionCodeIsValid, regionNameIsValid)) {
			return Pages.ADD_REGION;
		}

		try {
			return db.addRegion(region);
		} catch (SQLIntegrityConstraintViolationException e) {
			// foreign key does not exist or country does not exist.
			addGlobalMessage("Error adding Region: " + region.getCode() + " to Country: " + region.getCountryCode());
			return Pages.ADD_REGION;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return Pages.ADD_REGION;
		}
	}
}
