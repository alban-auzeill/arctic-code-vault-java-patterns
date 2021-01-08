package sfBugs; // (rank 359) copied from https://github.com/spotbugs/spotbugs/blob/a6f9acb2932b54f5b70ea8bc206afb552321a222/spotbugsTestCases/src/java/sfBugs/Bug3291092.java

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.umd.cs.findbugs.annotations.NoWarning;

public class Bug3291092 {

    static Pattern CAP_PATTERN = Pattern.compile("[a-z_0-9]+");

    @NoWarning("IL_INFINITE_LOOP")
    public static String constize(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append(field.charAt(0));
        Matcher m = CAP_PATTERN.matcher(field.substring(1));
        int start = 1;
        while (m.find()) {
            sb.append(field.substring(start, m.end() + 1).toUpperCase());
            sb.append("_");
            start = m.end();
        }
        sb.append(field.substring(start).toUpperCase());
        return sb.toString();
    }

    public static void main(String args[]) {
        String s = "camelCase";
        System.out.println(constize(s));
    }
}
