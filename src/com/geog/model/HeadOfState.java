package com.geog.model;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class HeadOfState {

	private String _id;
	private String headOfState;

	public HeadOfState() {}

	public String getHeadOfState() {
		return headOfState;
	}

	public void setHeadOfState(final String headOfState) {
		this.headOfState = headOfState.trim();
	}

	public String get_id() {
		return _id;
	}

	public void set_id(final String _id) {
		this._id = _id.trim();
	}

}
