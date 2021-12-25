package com.calc;

import java.util.stream.Stream;

class StringCalculator {
	public static int add(String numbers) {
		if (numbers.equals("")) {
			return 0;
		}

		return Stream.of(numbers.split(",")).map(Integer::parseInt).reduce(0, Integer::sum);
	}
}