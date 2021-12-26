package com.calc;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class StringCalculator {

	public static final int MAX_NUMBER_SUPPORTED = 1000;
	private static final String CUSTOM_DELIMITER_PREFIX = "//";

	public static class NegativesNotAllowed extends RuntimeException {
		private static final long serialVersionUID = 1L;

		private final List<Integer> negativeNumbers;

		public NegativesNotAllowed(List<Integer> negativeNumbers) {
			super("error: negatives not allowed: " + negativeNumbers);
			this.negativeNumbers = negativeNumbers;
		}

		public List<Integer> getNumbers() {
			return Collections.unmodifiableList(negativeNumbers);
		}
	}

	public static int add(String numbersInput) {
		if (numbersInput.equals("")) {
			return 0;
		}

		List<Integer> parsedNumbers = parseNumbersFrom(numbersInput);

		checkForNoNegativeNumbers(parsedNumbers);

		return parsedNumbers.stream().reduce(0, Integer::sum);
	}

	private static List<Integer> parseNumbersFrom(String numbersInput) {
		String splitRegex = "(,|\\\\n)";

		if (numbersInput.startsWith(CUSTOM_DELIMITER_PREFIX)) {
			Pattern customLengthDelimiterRegex = Pattern.compile(CUSTOM_DELIMITER_PREFIX + "\\[(.*?)\\]");
			Matcher delimiterMatcher = customLengthDelimiterRegex.matcher(numbersInput);

			if (delimiterMatcher.find()) {
				String multiCharDelimiter = delimiterMatcher.group(1);
				splitRegex = Pattern.quote(multiCharDelimiter);
				numbersInput = numbersInput.substring(6 + multiCharDelimiter.length());
			} else {
				String singleCharDelimiter = numbersInput.charAt(2) + "";
				splitRegex = Pattern.quote(singleCharDelimiter);
				numbersInput = numbersInput.substring(5);
			}
		}

		String[] splitNumbers = numbersInput.split(splitRegex);
		return Stream.of(splitNumbers)
				.map(Integer::parseInt)
				.filter(number -> number <= MAX_NUMBER_SUPPORTED)
				.collect(toList());
	}

	private static void checkForNoNegativeNumbers(List<Integer> parsedNumbers) {
		List<Integer> negativeNumbers = parsedNumbers.stream().filter(number -> number < 0).collect(toList());
		if (!negativeNumbers.isEmpty()) {
			throw new NegativesNotAllowed(negativeNumbers);
		}
	}
}