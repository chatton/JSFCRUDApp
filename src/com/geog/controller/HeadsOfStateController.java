package com.geog.controller;

import static com.geog.util.Messages.addGlobalMessage;
import static com.geog.util.Messages.addMessage;
import static com.geog.util.Util.anyFalse;
import static com.geog.util.Util.codeIsValid;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.geog.dao.MongoDao;
import com.geog.dao.MySQLDao;
import com.geog.model.Country;
import com.geog.model.HeadOfState;
import com.geog.util.Pages;

/*
 * Controller class in charge of handling interactions
 * between the Mongo/SQL database and the view for all things 
 * HeadsOfState related.
 */
@SessionScoped
@ManagedBean
public class HeadsOfStateController {

	/*
	 * has instance of both DAOs in order to check values in both databases.
	 */
	private MongoDao mongoDb;
	private MySQLDao sqlDb;
	private List<HeadOfState> headsOfState;

	public HeadsOfStateController() {
		mongoDb = new MongoDao();
		try {
			sqlDb = new MySQLDao();
		} catch (SQLException e) {
			addGlobalMessage("Error connecting to SQL database.");
		}
	}

	public void loadHeadsOfState() {
		this.headsOfState = mongoDb.getHeadsOfState();
	}

	public List<HeadOfState> getHeadsOfState() {
		return headsOfState;
	}

	/*
	 * returns true if a country with the provided country code already exists.
	 */
	private boolean countryCodeExists(final String countryCode, final List<Country> countries) {
		return countries.stream().anyMatch(country -> country.getCode().equals(countryCode));
	}

	/*
	 * returns true if the country already has a head of state.
	 */
	private boolean countryAlreadyHasHeadOfState(final String countryCode, final List<HeadOfState> hos) {
		return hos.stream().anyMatch(h -> h.get_id().equals(countryCode));
	}

	public String add(final HeadOfState hos) {
		final String countryCode = hos.get_id(); // the id is the country code. e.g. "USA"
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
			countries = sqlDb.getAllCountries();
		} catch (SQLException e) {
			addGlobalMessage("Error connecting to SQL data base.");
			return Pages.ADD_HEAD_OF_STATE;
		}

		/*
		 * check the SQL database to see if the country code exists.
		 */
		if (!countryCodeExists(countryCode, countries)) {
			addGlobalMessage("Country: " + countryCode + " does not exist.");
			return Pages.ADD_HEAD_OF_STATE;
		}

		if (countryAlreadyHasHeadOfState(countryCode, headsOfState)) {
			addGlobalMessage("Country: " + countryCode + " already has a head of state.");
			return Pages.ADD_HEAD_OF_STATE;
		}

		// add the head of state if everything is valid across databases.
		return mongoDb.add(hos);
	}

	public String delete(final HeadOfState hos) {
		return mongoDb.delete(hos);
	}

}
