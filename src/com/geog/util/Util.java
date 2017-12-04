package com.geog.util;

import java.util.stream.Stream;

public class Util {
	private Util() {
	}

	/*
	 * takes in a number of booleans and returns true if any of them are false, used
	 * as a helper method in checking various conditions.
	 */
	public static boolean anyFalse(final Boolean... vals) {
		return Stream.of(vals).anyMatch(val -> val == false);
	}

	/*
	 * helper method that returns whether or not a given code is valid based on it
	 * being empty or too long.
	 */
	public static boolean codeIsValid(final String code, final int length) {
		return !code.isEmpty() && code.length() < length;
	}

	/*
	 * if no length is specified, assume it can be longer, e.g. names and
	 * descriptions.
	 */
	public static boolean codeIsValid(final String code) {
		return codeIsValid(code, Integer.MAX_VALUE);
	}
}
