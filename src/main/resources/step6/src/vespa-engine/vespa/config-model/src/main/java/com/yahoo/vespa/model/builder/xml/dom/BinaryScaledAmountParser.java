// Copyright 2017 Yahoo Holdings. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root. (rank 298) copied from https://github.com/vespa-engine/vespa/blob/f8dfd639931905a9d6dae95f5b4e0ae812042117/config-model/src/main/java/com/yahoo/vespa/model/builder/xml/dom/BinaryScaledAmountParser.java
package com.yahoo.vespa.model.builder.xml.dom;

import com.yahoo.binaryprefix.BinaryPrefix;
import com.yahoo.binaryprefix.BinaryScaledAmount;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Tony Vaagenes
 */
public class BinaryScaledAmountParser {
    //The pattern must match the one given in the schema
    private static Pattern pattern = Pattern.compile("(\\d+(\\.\\d*)?)\\s*([kmgKMG])?");

    public static BinaryScaledAmount parse(String valueString) {
        Matcher matcher = pattern.matcher(valueString);

        if (!matcher.matches()) {
            throw new RuntimeException("Pattern and schema is out of sync.");
        }

        double amount = Double.valueOf(matcher.group(1));
        String binaryPrefixString = matcher.group(3);

        return new BinaryScaledAmount(amount, asBinaryPrefix(binaryPrefixString));
    }

    private static BinaryPrefix asBinaryPrefix(String binaryPrefixString) {
        if (binaryPrefixString == null) {
            return BinaryPrefix.unit;
        } else {
            return BinaryPrefix.fromSymbol(binaryPrefixString.toUpperCase().charAt(0));
        }
    }
}
