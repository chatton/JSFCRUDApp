package com.geog.controller;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.geog.dao.MySQLDao;
import com.geog.model.Region;
import com.geog.util.Pages;

import static com.geog.util.Messages.addMessage;
import static com.geog.util.Messages.addGlobalMessage;
import static com.geog.util.Util.anyFalse;

@ApplicationScoped
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

	private boolean hasUniqueCode(final Region region) {
		return regions.stream().noneMatch(r -> r.getCode().equals(region.getCode()));
	}

	private boolean countryCodeIsValid(final Region region) {
		final String code = region.getCountryCode();
		return !code.isEmpty() && code.length() < 4;
	}

	private boolean regionCodeIsValid(final Region region) {
		final String regionCode = region.getCode();
		return !regionCode.isEmpty() && regionCode.length() < 4;
	}

	private boolean nameIsValid(final Region region) {
		return !region.getName().isEmpty();
	}

	public String add(Region region) {

		boolean countryCodeIsValid = countryCodeIsValid(region);
		if (!countryCodeIsValid) {
			addMessage("regionform:noCountryCode", "Country code is mandatory. And must be < 4 characters");
		}

		boolean regionCodeIsValid = regionCodeIsValid(region);
		if (!regionCodeIsValid) {
			addMessage("regionform:noCode", "Region code is mandatory. And must be < 4 characters.");
		}

		boolean regionNameIsValid = nameIsValid(region);
		if (!regionNameIsValid) {
			addMessage("regionform:noName", "Name is mandatory.");
		}

		if (anyFalse(countryCodeIsValid, regionCodeIsValid, regionNameIsValid)) {
			return Pages.ADD_REGION;
		}

		if (!hasUniqueCode(region)) {
			addGlobalMessage("Region: " + region.getCode() + " already exists.");
			return Pages.ADD_REGION;
		}

		try {
			return db.addRegion(region);
		} catch (SQLIntegrityConstraintViolationException e) {
			// foreign key does not exist
			addGlobalMessage("Country: " + region.getCountryCode() + " does not exist.");
			return Pages.ADD_REGION;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return Pages.ADD_REGION;
		}
	}
}
