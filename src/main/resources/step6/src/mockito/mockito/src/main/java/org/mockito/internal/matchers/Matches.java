/* (rank 90) copied from https://github.com/mockito/mockito/blob/0ddbcb689e43f6a01f549c5efdd75defb39ebc14/src/main/java/org/mockito/internal/matchers/Matches.java
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.mockito.ArgumentMatcher;

public class Matches implements ArgumentMatcher<Object>, Serializable {

    private final Pattern pattern;

    public Matches(String regex) {
        this(Pattern.compile(regex));
    }

    public Matches(Pattern pattern) {
        this.pattern = pattern;
    }

    public boolean matches(Object actual) {
        return (actual instanceof String) && pattern.matcher((String) actual).find();
    }

    public String toString() {
        return "matches(\"" + pattern.pattern().replaceAll("\\\\", "\\\\\\\\") + "\")";
    }
}
