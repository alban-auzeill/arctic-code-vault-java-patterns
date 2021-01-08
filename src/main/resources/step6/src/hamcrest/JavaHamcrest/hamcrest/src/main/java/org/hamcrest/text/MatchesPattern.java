package org.hamcrest.text; // (rank 525) copied from https://github.com/hamcrest/JavaHamcrest/blob/adb5235fabef22e983b1523de511980d0f8bd282/hamcrest/src/main/java/org/hamcrest/text/MatchesPattern.java

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.regex.Pattern;

public class MatchesPattern extends TypeSafeMatcher<String> {
    private final Pattern pattern;

    public MatchesPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    protected boolean matchesSafely(String item) {
        return pattern.matcher(item).matches();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a string matching the pattern '" + pattern + "'");
    }

    /**
     * Creates a matcher of {@link java.lang.String} that matches when the examined string
     * exactly matches the given {@link java.util.regex.Pattern}.
     */
    public static Matcher<String> matchesPattern(Pattern pattern) {
        return new MatchesPattern(pattern);
    }

    /**
     * Creates a matcher of {@link java.lang.String} that matches when the examined string
     * exactly matches the given regular expression, treated as a {@link java.util.regex.Pattern}.
     */
    public static Matcher<String> matchesPattern(String regex) {
        return new MatchesPattern(Pattern.compile(regex));
    }
}
