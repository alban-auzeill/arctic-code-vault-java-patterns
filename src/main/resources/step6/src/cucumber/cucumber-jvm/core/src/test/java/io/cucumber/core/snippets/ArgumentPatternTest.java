package io.cucumber.core.snippets; // (rank 310) copied from https://github.com/cucumber/cucumber-jvm/blob/a64adbe2264dab2d4a5ee0f2d92d1b193b800a18/core/src/test/java/io/cucumber/core/snippets/ArgumentPatternTest.java

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

class ArgumentPatternTest {

    private final Pattern singleDigit = Pattern.compile("(\\d)");
    private final ArgumentPattern argumentPattern = new ArgumentPattern(singleDigit);

    @Test
    void replacesMatchWithoutEscapedNumberClass() {
        assertThat(argumentPattern.replaceMatchesWithGroups("1"), is(equalTo("(\\d)")));
    }

    @Test
    void replacesMultipleMatchesWithPattern() {
        assertThat(argumentPattern.replaceMatchesWithGroups("13"), is(equalTo("(\\d)(\\d)")));
    }

    @Test
    void replaceMatchWithSpace() {
        assertThat(argumentPattern.replaceMatchesWithSpace("4"), is(equalTo(" ")));
    }

}
