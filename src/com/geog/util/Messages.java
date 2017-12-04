package com.geog.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Messages {
	// private constructor to prevent construction
	private Messages() {}

	/*
	 * short helper method to add a message to a given jsf element.
	 */
	public static void addMessage(String element, String message) {
		FacesContext.getCurrentInstance().addMessage(element, new FacesMessage(message));
	}

	/*
	 * helper method to add a global message.
	 */
	public static void addGlobalMessage(String message) {
		addMessage(null, message);
	}
}
