package com.baeldung.regexp.datepattern; // (rank 448) copied from https://github.com/eugenp/tutorials/blob/3fb80e088c2d523eeed5836ac8cd4871d19989b6/core-java-modules/core-java-datetime-string/src/main/java/com/baeldung/regexp/datepattern/FormattedDateMatcher.java

import java.util.regex.Pattern;

class FormattedDateMatcher implements DateMatcher {

    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^\\d{4}-\\d{2}-\\d{2}$");

    @Override
    public boolean matches(String date) {
        return DATE_PATTERN.matcher(date).matches();
    }
}
