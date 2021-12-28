package com.calc.parsers;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

public class ConversionUtil {
    	public static List<Integer> stringsToInts(String[] splitNumberStrings) {
		return Stream.of(splitNumberStrings)
				.map(Integer::parseInt)
				.collect(toList());
	}
}
