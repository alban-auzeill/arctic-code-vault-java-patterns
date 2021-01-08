package com.baeldung.emptystrings; // (rank 448) copied from https://github.com/eugenp/tutorials/blob/3fb80e088c2d523eeed5836ac8cd4871d19989b6/core-java-modules/core-java-string-operations-2/src/main/java/com/baeldung/emptystrings/SomeClassWithValidations.java

import javax.validation.constraints.Pattern;

class SomeClassWithValidations {

    @Pattern(regexp = "\\A(?!\\s*\\Z).+")
    private String someString;

    SomeClassWithValidations setSomeString(String someString) {
        this.someString = someString;
        return this;
    }
}
