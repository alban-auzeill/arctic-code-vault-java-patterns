package org.gridkit.jvmtool; // (rank 810) copied from https://github.com/aragozin/jvm-tools/blob/84f20a20334b48a06a285e27974c49c636e52145/sjk-core/src/test/java/org/gridkit/jvmtool/RegExHelper.java

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExHelper {

    private static final Pattern TOKENIZER = Pattern.compile(
            "\\\\[(]"
            + "|\\[.*\\]"
            + "|\\\\Q.*\\\\E"
            + "|.");

    public static String uncapture(String pattern) {
        StringBuilder sb = new StringBuilder();
        Matcher m = TOKENIZER.matcher(pattern);
        int n = 0;
        while(m.find(n)) {
            String tkn = m.group();
            if ("(".equals(tkn)) {
                sb.append("(?:");
            } else {
                sb.append(tkn);
            }
            n = m.end();
        }
        return sb.toString();
    }
}
