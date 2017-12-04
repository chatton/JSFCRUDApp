package com.geog.model;

import javax.faces.bean.ManagedBean;

/*
 * represents a Head of State from the MongoDB database.
 */
@ManagedBean
public class HeadOfState {

	private String _id;
	private String headOfState;

	/*
	 * no args-constructor for jsf
	 */
	public HeadOfState() {}

	public String getHeadOfState() {
		return headOfState;
	}

	public void setHeadOfState(final String headOfState) {
		this.headOfState = headOfState.trim();
	}

	/*
	 * violate camelCase naming conventions because the attribute must be called _id
	 * when creating an instance based off of the class.
	 */
	public String get_id() {
		return _id;
	}

	public void set_id(final String _id) {
		this._id = _id.trim();
	}

}
