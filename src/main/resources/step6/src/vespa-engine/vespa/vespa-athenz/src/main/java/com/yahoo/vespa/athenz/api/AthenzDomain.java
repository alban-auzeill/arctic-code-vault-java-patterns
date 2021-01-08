// Copyright 2018 Yahoo Holdings. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root. (rank 298) copied from https://github.com/vespa-engine/vespa/blob/f8dfd639931905a9d6dae95f5b4e0ae812042117/vespa-athenz/src/main/java/com/yahoo/vespa/athenz/api/AthenzDomain.java
package com.yahoo.vespa.athenz.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author bjorncs
 */
public class AthenzDomain {

    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z0-9_][a-zA-Z0-9_\\-.]*[a-zA-Z0-9_]");

    private final String name;

    @JsonCreator
    public AthenzDomain(String name) {
        validateName(name);
        this.name = name;
    }

    private static void validateName(String name) {
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("Not a valid domain name: '" + name + "'");
        }
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public boolean isTopLevelDomain() {
        return !name.contains(".");
    }

    public AthenzDomain getParent() {
        return new AthenzDomain(name.substring(0, lastDot()));
    }

    public String getNameSuffix() {
        return name.substring(lastDot() + 1);
    }

    private int lastDot() {
        return name.lastIndexOf('.');
    }

    @Override
    public String toString() {
        return "AthenzDomain{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AthenzDomain that = (AthenzDomain) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
