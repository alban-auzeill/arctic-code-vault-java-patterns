package com.baeldung.isnumeric; // (rank 448) copied from https://github.com/eugenp/tutorials/blob/3fb80e088c2d523eeed5836ac8cd4871d19989b6/core-java-modules/core-java-string-operations/src/main/java/com/baeldung/isnumeric/IsNumeric.java

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class IsNumeric {
    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public boolean usingCoreJava(String strNum) {
        if (strNum == null) {
            return false;
        }

        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean usingPreCompiledRegularExpressions(String strNum) {
        if (strNum == null) {
            return false;
        }

        return pattern.matcher(strNum)
            .matches();
    }

    public boolean usingNumberUtils_isCreatable(String strNum) {
        return NumberUtils.isCreatable(strNum);
    }

    public boolean usingNumberUtils_isParsable(String strNum) {
        return NumberUtils.isParsable(strNum);
    }

    public boolean usingStringUtils_isNumeric(String strNum) {
        return StringUtils.isNumeric(strNum);
    }

    public boolean usingStringUtils_isNumericSpace(String strNum) {
        return StringUtils.isNumericSpace(strNum);
    }
}
