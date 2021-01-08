package com.baeldung.isnumeric; // (rank 448) copied from https://github.com/eugenp/tutorials/blob/3fb80e088c2d523eeed5836ac8cd4871d19989b6/core-java-modules/core-java-string-operations/src/test/java/com/baeldung/isnumeric/RegularExpressionsUnitTest.java

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Pattern;

import org.junit.Test;

public class RegularExpressionsUnitTest {
    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum)
            .matches();
    }

    @Test
    public void whenUsingRegularExpressions_thenTrue() {
        // Valid Numbers
        assertThat(isNumeric("22")).isTrue();
        assertThat(isNumeric("5.05")).isTrue();
        assertThat(isNumeric("-200")).isTrue();

        // Invalid Numbers
        assertThat(isNumeric(null)).isFalse();
        assertThat(isNumeric("abc")).isFalse();
    }
}
