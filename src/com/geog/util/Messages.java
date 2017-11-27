package com.geog.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Messages {
	private Messages() {
	}

	public static void addMessage(String element, String message) {
		FacesContext.getCurrentInstance().addMessage(element, new FacesMessage(message));
	}

	public static void addGlobalMessage(String message) {
		addMessage(null, message);
	}
}
