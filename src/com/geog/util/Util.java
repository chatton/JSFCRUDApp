package com.geog.util;

import java.util.stream.Stream;

public class Util {
	private Util() {
	}

	public static boolean anyFalse(final Boolean... vals) {
		return Stream.of(vals).anyMatch(val -> val == false);
	}

	public static boolean codeIsValid(final String code, final int length) {
		return !code.isEmpty() && code.length() < length;
	}

	public static boolean codeIsValid(final String code) {
		return codeIsValid(code, Integer.MAX_VALUE);
	}
}
