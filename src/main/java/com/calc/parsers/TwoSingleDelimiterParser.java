package com.calc.parsers;

import java.util.regex.Pattern;

public class TwoSingleDelimiterParser extends NumberParserAbstract {

	private static final String DELIMITER_REGEX = "^\\[(.)\\]\\[(.)\\]$";

	public TwoSingleDelimiterParser(String numbersInput) {
		super(numbersInput);
	}

	@Override
	protected String getNumberSplitRegex() {
		String firstDelimiter = Pattern.quote(getMatcher().group(1));
		String secondDelimiter = Pattern.quote(getMatcher().group(2));
		return String.format("(%s|%s)", firstDelimiter, secondDelimiter);
	}

	@Override
	protected String getDelimiterRegex() {
		return DELIMITER_REGEX;
	}

}
