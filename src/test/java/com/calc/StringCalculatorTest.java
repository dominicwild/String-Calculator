package com.calc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.*;
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
				.forAll(range(0, Integer.MAX_VALUE), range(0, Integer.MAX_VALUE))
				.check((num1, num2) -> StringCalculator.add(num1 + "," + num2) == num1 + num2);
	}

}
