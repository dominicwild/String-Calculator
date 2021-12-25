package com.calc;

import java.util.stream.Stream;

class StringCalculator {

	private static final String CUSTOM_DELIMITER_PREFIX = "//";

	public static int add(String numbers) {
		if (numbers.equals("")) {
			return 0;
		}

		String splitRegex = "(,|\\\\n)";

		if (numbers.startsWith(CUSTOM_DELIMITER_PREFIX)) {
			String customDelimiter = numbers.charAt(2) + "";
			splitRegex = String.format("\\Q%s\\E", customDelimiter);
			numbers = numbers.substring(5);
		}

		return Stream.of(numbers.split(splitRegex)).map(Integer::parseInt).reduce(0, Integer::sum);
	}
}