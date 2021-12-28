package com.calc;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;

public class StringCalculator {

	public static final int MAX_NUMBER_SUPPORTED = 1000;
	static final String CUSTOM_DELIMITER_PREFIX = "//";

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

		List<Integer> parsedNumbers = CalculatorParser.parseNumbers(numbersInput);

		checkForNoNegativeNumbers(parsedNumbers);

		return parsedNumbers.stream()
				.filter(number -> number <= MAX_NUMBER_SUPPORTED)
				.reduce(0, Integer::sum);
	}

	private static void checkForNoNegativeNumbers(List<Integer> parsedNumbers) {
		List<Integer> negativeNumbers = parsedNumbers.stream().filter(number -> number < 0).collect(toList());
		if (!negativeNumbers.isEmpty()) {
			throw new NegativesNotAllowed(negativeNumbers);
		}
	}
}