package com.baeldung.patternreuse; // (rank 448) copied from https://github.com/eugenp/tutorials/blob/3fb80e088c2d523eeed5836ac8cd4871d19989b6/core-java-modules/core-java-11/src/test/java/com/baeldung/patternreuse/PatternJava11UnitTest.java

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PatternJava11UnitTest {

    @Test
    public void givenPreCompiledPattern_whenCallAsMatchPredicate_thenReturnMatchPredicateToMatchesPattern() {
        List<String> namesToValidate = Arrays.asList("Fabio Silva", "Fabio Luis Silva");
        Pattern firstLastNamePreCompiledPattern = Pattern.compile("[a-zA-Z]{3,} [a-zA-Z]{3,}");

        Predicate<String> patternAsMatchPredicate = firstLastNamePreCompiledPattern.asMatchPredicate();
        List<String> validatedNames = namesToValidate.stream()
                .filter(patternAsMatchPredicate)
                .collect(Collectors.toList());

        assertTrue(validatedNames.contains("Fabio Silva"));
        assertFalse(validatedNames.contains("Fabio Luis Silva"));
    }
}
