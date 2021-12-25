package com.calc;

import org.junit.jupiter.api.Test;
import org.quicktheories.generators.Generate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.*;

import java.util.StringJoiner;

import static org.quicktheories.generators.Generate.*;

public class StringCalculatorTest {

	@Test
	void empty_string_returns_zero() {
		int zero = 0;
		String blankString = "";
		assertEquals(zero, StringCalculator.add(blankString));
	}

	@Test
	void one_number_returns_itself() {
		qt()
				.forAll(integers().all())
				.check((number) -> StringCalculator.add("" + number) == number);
	}

	@Test
	void two_numbers_returns_sum() {
		qt()
				.forAll(range(0, Integer.MAX_VALUE / 2), range(0, Integer.MAX_VALUE / 2))
				.check((num1, num2) -> StringCalculator.add(num1 + "," + num2) == num1 + num2);
	}

	@Test

	void comma_separated_numbers_returns_sum() {
		qt()
				.forAll(Generate.intArrays(range(0, 100_000), range(0, 10_000)))
				.check((numbers) -> {
					StringJoiner numberJoin = new StringJoiner(",");
					int sumOfNumbers = 0;
					for(int number : numbers) {
						numberJoin.add(number + "");
						sumOfNumbers += number;
					}
					return StringCalculator.add(numberJoin.toString()) == sumOfNumbers;
				});
	}

}
