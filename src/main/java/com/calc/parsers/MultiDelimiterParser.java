package com.calc.parsers;

import java.util.regex.Pattern;

public class MultiDelimiterParser extends NumberParserAbstract {
	
	private static final String DELIMITER_REGEX = "^\\[(.*?)\\]$";

	public MultiDelimiterParser(String numbersInput) {
		super(numbersInput);
	}

	@Override
	protected String getNumberSplitRegex() {
		String multiCharDelimiter = getMatcher().group(1);
		return Pattern.quote(multiCharDelimiter);
	}

	@Override
	protected String getDelimiterRegex() {
		return DELIMITER_REGEX;
	}

}
