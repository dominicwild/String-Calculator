package com.calc.parsers;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class NumberParserAbstract implements NumberParser {

	private final String numbersInput;
	private Matcher matcher;

	public NumberParserAbstract(String numbersInput) {
		this.numbersInput = numbersInput;
	}

	protected abstract String getNumberSplitRegex();

	protected abstract String getDelimiterRegex();

	@Override
	public boolean match() {
		this.matcher = Pattern.compile(getDelimiterRegex()).matcher(getDelimiterDefinition());
		return matcher.find();
	}

	@Override
	public List<Integer> parse() {
		String[] splitNumberStrings = getNumbers().split(getNumberSplitRegex());
		return ConversionUtil.stringsToInts(splitNumberStrings);
	}

	protected String getNumbers() {
		Matcher numbersMatcher = Pattern.compile("//.*\\\\n(.*)").matcher(numbersInput);
		numbersMatcher.find();
		return numbersMatcher.group(1);
	}

	protected String getDelimiterDefinition() {
		Matcher delimiterMatcher = Pattern.compile("//(.*)\\\\n.*").matcher(numbersInput);
		delimiterMatcher.find();
		return delimiterMatcher.group(1);
	}

	protected Matcher getMatcher() {
		return matcher;
	}
}
