package com.calc;

import org.junit.jupiter.api.Test;
import org.quicktheories.WithQuickTheories;

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

}
