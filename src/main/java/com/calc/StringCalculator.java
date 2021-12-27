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
		String numbers = numbersInput;

		if (numbersInput.startsWith(CUSTOM_DELIMITER_PREFIX)) {
			String delimiterDefinition = getDelimiterDefinitionFrom(numbersInput);
			numbers = getNumbersFrom(numbersInput);
			splitRegex = determineSplitRegexFrom(delimiterDefinition);
		}

		String[] splitNumbers = numbers.split(splitRegex);
		return Stream.of(splitNumbers)
				.map(Integer::parseInt)
				.filter(number -> number <= MAX_NUMBER_SUPPORTED)
				.collect(toList());
	}

	private static String determineSplitRegexFrom(String delimiterDefinition) {
		Matcher multiDelimiterMatcher = Pattern.compile("^\\[(.*?)\\]$").matcher(delimiterDefinition);
		Matcher twoSingleDelimiterMatcher = Pattern.compile("^\\[(.)\\]\\[(.)\\]$").matcher(delimiterDefinition);

		if (twoSingleDelimiterMatcher.find()) {
			String firstDelimiter = Pattern.quote(twoSingleDelimiterMatcher.group(1));
			String secondDelimiter = Pattern.quote(twoSingleDelimiterMatcher.group(2));
			return String.format("(%s|%s)", firstDelimiter, secondDelimiter);
		}

		if (multiDelimiterMatcher.find()) {
			String multiCharDelimiter = multiDelimiterMatcher.group(1);
			return Pattern.quote(multiCharDelimiter);
		}

		return Pattern.quote(delimiterDefinition);
	}

	private static String getNumbersFrom(String numbersInput) {
		Matcher numbersMatcher = Pattern.compile("//.*\\\\n(.*)").matcher(numbersInput);
		numbersMatcher.find();
		return numbersMatcher.group(1);
	}

	private static String getDelimiterDefinitionFrom(String numbersInput) {
		Matcher delimiterMatcher = Pattern.compile("//(.*)\\\\n.*").matcher(numbersInput);
		delimiterMatcher.find();
		return delimiterMatcher.group(1);
	}

	private static void checkForNoNegativeNumbers(List<Integer> parsedNumbers) {
		List<Integer> negativeNumbers = parsedNumbers.stream().filter(number -> number < 0).collect(toList());
		if (!negativeNumbers.isEmpty()) {
			throw new NegativesNotAllowed(negativeNumbers);
		}
	}
}