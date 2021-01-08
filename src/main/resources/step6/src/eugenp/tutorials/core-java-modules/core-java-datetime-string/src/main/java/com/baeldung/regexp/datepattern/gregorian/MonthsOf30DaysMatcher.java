package com.baeldung.regexp.datepattern.gregorian; // (rank 448) copied from https://github.com/eugenp/tutorials/blob/3fb80e088c2d523eeed5836ac8cd4871d19989b6/core-java-modules/core-java-datetime-string/src/main/java/com/baeldung/regexp/datepattern/gregorian/MonthsOf30DaysMatcher.java

import com.baeldung.regexp.datepattern.DateMatcher;

import java.util.regex.Pattern;

public class MonthsOf30DaysMatcher implements DateMatcher {

    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$");

    @Override
    public boolean matches(String date) {
        return DATE_PATTERN.matcher(date).matches();
    }

}
