package com.calc.parsers;

import java.util.regex.Pattern;

public class SingleDelimiterParser extends NumberParserAbstract {

    private static final String DELIMITER_REGEX = "^(.)$";

    public SingleDelimiterParser(String numbersInput) {
        super(numbersInput);
    }

    @Override
    protected String getNumberSplitRegex() {
        return Pattern.quote(getMatcher().group(1));
    }

    @Override
    protected String getDelimiterRegex() {
        return DELIMITER_REGEX;
    }
}
