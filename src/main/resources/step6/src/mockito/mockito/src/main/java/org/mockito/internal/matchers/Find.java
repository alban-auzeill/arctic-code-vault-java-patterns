/* (rank 90) copied from https://github.com/mockito/mockito/blob/0ddbcb689e43f6a01f549c5efdd75defb39ebc14/src/main/java/org/mockito/internal/matchers/Find.java
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.mockito.ArgumentMatcher;

public class Find implements ArgumentMatcher<String>, Serializable {

    private final String regex;

    public Find(String regex) {
        this.regex = regex;
    }

    public boolean matches(String actual) {
        return actual != null && Pattern.compile(regex).matcher(actual).find();
    }

    public String toString() {
        return "find(\"" + regex.replaceAll("\\\\", "\\\\\\\\") + "\")";
    }
}
