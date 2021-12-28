package com.calc;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.quicktheories.generators.SourceDSL;

import static com.calc.StringCalculator.MAX_NUMBER_SUPPORTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.calc.StringCalculator.NegativesNotAllowed;

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
				.forAll(range(0, MAX_NUMBER_SUPPORTED))
				.check((number) -> StringCalculator.add("" + number) == number);
	}

	@Test
	void two_numbers_returns_sum() {
		qt()
				.forAll(range(0, MAX_NUMBER_SUPPORTED), range(0, MAX_NUMBER_SUPPORTED))
				.check((num1, num2) -> StringCalculator.add(num1 + "," + num2) == num1 + num2);
	}

	@ParameterizedTest
	@ValueSource(strings = { ",", "\\n" })
	void separated_numbers_returns_sum(String delimiter) {
		qt()
				.forAll(intArrays(range(0, 1000), range(0, MAX_NUMBER_SUPPORTED)))
				.check((numbers) -> {
					StringJoiner numberJoin = new StringJoiner(delimiter);
					int sumOfNumbers = 0;

					for (int number : numbers) {
						numberJoin.add(number + "");
						sumOfNumbers += number;
					}

					return StringCalculator.add(numberJoin.toString()) == sumOfNumbers;
				});
	}

	@Test
	void numbers_with_mixed_separators_return_sum() {
		qt()
				.forAll(intArrays(range(0, 1000), range(0, MAX_NUMBER_SUPPORTED)))
				.check((numbers) -> {
					StringBuilder stringBuilder = new StringBuilder();
					int sumOfNumbers = 0;

					for (int number : numbers) {
						stringBuilder.append(number + randomDelimiter());
						sumOfNumbers += number;
					}

					return StringCalculator.add(stringBuilder.toString()) == sumOfNumbers;
				});
	}

	@Test
	void numbers_with_custom_separator_return_sum() {
		qt()
				.forAll(intArrays(range(1, 1000), range(0, MAX_NUMBER_SUPPORTED)),
						strings().basicLatinAlphabet().ofLength(1)
								.assuming(string -> !string.isBlank() && !string.matches("[0-9]")))
				.check((numbers, delimiter) -> {
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append("//" + delimiter + "\\n");
					int sumOfNumbers = 0;

					for (int number : numbers) {
						stringBuilder.append(number + delimiter);
						sumOfNumbers += number;
					}

					return StringCalculator.add(stringBuilder.toString()) == sumOfNumbers;
				});
	}

	@Test
	void numbers_with_multiple_single_character_delimiters_return_sum() {
		String excludedSpecialChars = Pattern.quote("[]");
		qt()
				.forAll(intArrays(range(1, 1000), range(0, MAX_NUMBER_SUPPORTED)),
						strings().basicLatinAlphabet().ofLength(2)
								.assuming(string -> !string.isBlank()
										&& !string.matches(".*[0-9" + excludedSpecialChars + "].*")))
				.check((numbers, delimiter) -> {
					char delimiter1 = delimiter.charAt(0);
					char delimiter2 = delimiter.charAt(1);
					String delimiterPrefix = String.format("//[%s][%s]\\n", delimiter1, delimiter2);

					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append(delimiterPrefix);
					int sumOfNumbers = 0;

					for (int number : numbers) {
						char randomDelimiter = randomDelimiterFrom(delimiter1, delimiter2);

						stringBuilder.append("" + number + randomDelimiter);
						sumOfNumbers += number;
					}

					return StringCalculator.add(stringBuilder.toString()) == sumOfNumbers;
				});
	}

	private char randomDelimiterFrom(char delimiter1, char delimiter2) {
		if (Math.random() > 0.5) {
			return delimiter1;
		}
		return delimiter2;
	}

	@Test
	void negative_numbers_throws_exception() {
		qt()
				.forAll(intArrays(range(1, 1000), range(-10_000, MAX_NUMBER_SUPPORTED)))
				.check((numbers) -> {
					numbers[0] = -1;
					StringBuilder stringBuilder = new StringBuilder();

					for (int number : numbers) {
						stringBuilder.append(number + randomDelimiter());
					}

					NegativesNotAllowed ex = assertThrows(NegativesNotAllowed.class,
							() -> StringCalculator.add(stringBuilder.toString()));
					List<Integer> negativeNumbers = negativeNumbersFrom(numbers);

					return ex.getNumbers().equals(negativeNumbers);
				});
	}

	@Test
	void ignore_numbers_greater_than_1000() {
		qt()
				.forAll(intArrays(range(1, 1000), range(0, MAX_NUMBER_SUPPORTED * 10)))
				.check((numbers) -> {
					StringBuilder stringBuilder = new StringBuilder();
					int sumOfNumbers = 0;

					for (int number : numbers) {
						stringBuilder.append(number + randomDelimiter());
						if (number <= 1000) {
							sumOfNumbers += number;
						}
					}

					return StringCalculator.add(stringBuilder.toString()) == sumOfNumbers;
				});
	}

	@Test
	void custom_delimiters_can_be_of_any_length_when_surrounded_in_square_brackets() {
		String excludedSpecialChars = Pattern.quote("[]\\");
		qt()
				.forAll(intArrays(range(2, 1000), range(0, MAX_NUMBER_SUPPORTED)),
						strings().basicLatinAlphabet().ofLengthBetween(2, 3)
								.assuming(string -> !string.trim().isBlank()
										&& !string.matches(".*[0-9" + excludedSpecialChars + "].*")))
				.check((numbers, delimiter) -> {
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append("//[" + delimiter + "]\\n");
					int sumOfNumbers = 0;

					for (int number : numbers) {
						stringBuilder.append(number + delimiter);
						sumOfNumbers += number;
					}

					return StringCalculator.add(stringBuilder.toString()) == sumOfNumbers;
				});
	}

	private String randomDelimiter() {
		return Math.random() > 0.5 ? "," : "\\n";
	}

	private List<Integer> negativeNumbersFrom(int[] numbers) {
		List<Integer> negativeNumbers = Arrays.stream(numbers)
				.filter(number -> number < 0)
				.boxed()
				.collect(Collectors.toList());
		return negativeNumbers;
	}
}
