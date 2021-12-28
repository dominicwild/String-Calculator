package com.calc;

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
			NumberParser multiDelimiterParser = new MultiDelimiterParser(numbersInput);
			NumberParser twoDelimiterParser = new TwoSingleDelimiterParser(numbersInput);
			NumberParser singleDelimiterParser = new SingleDelimiterParser(numbersInput);

			if (singleDelimiterParser.match()) {
				return singleDelimiterParser.parse();
			}

			if (twoDelimiterParser.match()) {
				return twoDelimiterParser.parse();
			}

			if (multiDelimiterParser.match()) {
				return multiDelimiterParser.parse();
			}
		}

		String[] splitNumbers = numbersInput.split(splitRegex);
		return ConversionUtil.stringsToInts(splitNumbers);
	}

}
