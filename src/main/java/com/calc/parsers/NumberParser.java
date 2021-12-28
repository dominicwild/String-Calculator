package com.calc.parsers;

import java.util.List;

public interface NumberParser {
    boolean match();
    List<Integer> parse();
}
