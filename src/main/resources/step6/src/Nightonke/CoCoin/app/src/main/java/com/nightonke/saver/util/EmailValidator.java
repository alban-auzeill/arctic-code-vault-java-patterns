package com.nightonke.saver.util; // (rank 861) copied from https://github.com/Nightonke/CoCoin/blob/5566b054856db7e25cd6b3dd54ccf96461b32699/app/src/main/java/com/nightonke/saver/util/EmailValidator.java

/**
 * Created by 伟平 on 2015/11/20.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    public boolean validate(final String hex) {

        matcher = pattern.matcher(hex);
        return matcher.matches();

    }
}