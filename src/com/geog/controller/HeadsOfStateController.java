package com.geog.controller;

import static com.geog.util.Messages.addGlobalMessage;
import static com.geog.util.Messages.addMessage;
import static com.geog.util.Util.codeIsValid;
import static com.geog.util.Util.anyFalse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.geog.dao.MongoDao;
import com.geog.dao.MySQLDao;
import com.geog.model.Country;
import com.geog.model.HeadOfState;
import com.geog.util.Pages;

@ApplicationScoped
@ManagedBean
public class HeadsOfStateController {

	private MongoDao mongoDb;
	private MySQLDao sqlDb;
	private List<HeadOfState> headsOfState;

	public HeadsOfStateController() {
		System.out.println("Creating heads of state controller.");
		mongoDb = new MongoDao();
		sqlDb = new MySQLDao();
	}

	public void loadHeadsOfState() {
		System.out.println("Loading heads of state from MongoDB");
		this.headsOfState = mongoDb.getHeadsOfState();
	}

	public List<HeadOfState> getHeadsOfState() {
		return headsOfState;
	}

	private boolean countryCodeExists(final String countryCode, final List<Country> countries) {
		return countries.stream().anyMatch(country -> country.getCode().equals(countryCode));
	}

	private boolean countryAlreadyHasHeadOfState(final String countryCode, final List<HeadOfState> hos) {
		return hos.stream().anyMatch(h -> h.get_id().equals(countryCode));
	}

	public String add(final HeadOfState hos) {
		final String countryCode = hos.get_id();
		final boolean countryCodeIsValid = codeIsValid(countryCode, 4);
		if (!countryCodeIsValid) {
			addMessage("headofstateform:noCode",
					"Country code is mandatory. Must be non-empty, non-duplicate and have < 4 characters");
		}
		final boolean hosNameIsValid = codeIsValid(hos.getHeadOfState());
		if (!hosNameIsValid) {
			addMessage("headofstateform:noHos", "Head of State is mandatory. Must be non-empty");
		}

		if (anyFalse(countryCodeIsValid, hosNameIsValid)) {
			return Pages.ADD_HEAD_OF_STATE;
		}

		List<Country> countries = new ArrayList<>();
		try {
			countries = sqlDb.loadAllCountries();
		} catch (SQLException e) {
			addGlobalMessage("Error connecting to SQL data base.");
			System.out.println(e.getMessage());
			return Pages.ADD_HEAD_OF_STATE;
		}

		if (!countryCodeExists(countryCode, countries)) {
			addGlobalMessage("Country: " + countryCode + " does not exist.");
			return Pages.ADD_HEAD_OF_STATE;
		}

		if (countryAlreadyHasHeadOfState(countryCode, headsOfState)) {
			addGlobalMessage("Country: " + countryCode + " already has a head of state.");
			return Pages.ADD_HEAD_OF_STATE;
		}

		return mongoDb.add(hos);
	}

	public String delete(final HeadOfState hos) {
		return mongoDb.delete(hos);
	}

}
