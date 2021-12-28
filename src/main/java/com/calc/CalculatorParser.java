package com.calc;

import java.util.Arrays;
import java.util.List;

import com.calc.parsers.ConversionUtil;
import com.calc.parsers.MultiDelimiterParser;
import com.calc.parsers.NumberParser;
import com.calc.parsers.SingleDelimiterParser;
import com.calc.parsers.TwoSingleDelimiterParser;

public class CalculatorParser {

	public static List<Integer> parseNumbers(String numbersInput) {
		String splitRegex = "(,|\\\\n)";

		if (numbersInput.startsWith(StringCalculator.CUSTOM_DELIMITER_PREFIX)) {
			List<NumberParser> parsers = getParsersFor(numbersInput);
			
			for (NumberParser numberParser : parsers) {
				if(numberParser.match()){
					return numberParser.parse();
				}
			}
		}

		String[] splitNumbers = numbersInput.split(splitRegex);
		return ConversionUtil.stringsToInts(splitNumbers);
	}

	private static List<NumberParser> getParsersFor(String numbersInput) {
		return Arrays.asList(
				new SingleDelimiterParser(numbersInput),
				new TwoSingleDelimiterParser(numbersInput),
				new MultiDelimiterParser(numbersInput));
	}

}
