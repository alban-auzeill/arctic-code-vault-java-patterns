package com.baeldung.regexp.datepattern.gregorian; // (rank 448) copied from https://github.com/eugenp/tutorials/blob/3fb80e088c2d523eeed5836ac8cd4871d19989b6/core-java-modules/core-java-datetime-string/src/main/java/com/baeldung/regexp/datepattern/gregorian/February29thMatcher.java

import com.baeldung.regexp.datepattern.DateMatcher;

import java.util.regex.Pattern;

public class February29thMatcher implements DateMatcher {

    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$");

    @Override
    public boolean matches(String date) {
        return DATE_PATTERN.matcher(date).matches();
    }
}
