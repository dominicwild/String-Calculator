package com.calc;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class StringCalculator {

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
	

	public static int add(String numbers) {
		if (numbers.equals("")) {
			return 0;
		}

		String splitRegex = "(,|\\\\n)";

		if (numbers.startsWith(CUSTOM_DELIMITER_PREFIX)) {
			String customDelimiter = numbers.charAt(2) + "";
			splitRegex = Pattern.quote(customDelimiter);
			numbers = numbers.substring(5);
		}

		String[] splitNumbers = numbers.split(splitRegex);
		List<Integer> parsedNumbers = Stream.of(splitNumbers)
				.map(Integer::parseInt)
				.filter(number -> number <= MAX_NUMBER_SUPPORTED)
				.collect(toList());

		checkForNoNegativeNumbers(parsedNumbers);


		return parsedNumbers.stream().reduce(0, Integer::sum);
	}


	private static void checkForNoNegativeNumbers(List<Integer> parsedNumbers) {
		List<Integer> negativeNumbers = parsedNumbers.stream().filter(number -> number < 0).collect(toList());
		if(!negativeNumbers.isEmpty()){
			throw new NegativesNotAllowed(negativeNumbers);
		}
	}
}