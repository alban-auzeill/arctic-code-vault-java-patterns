package io.quarkus.runtime.util; // (rank 94) copied from https://github.com/quarkusio/quarkus/blob/ef55a323d52af0969c7507289c37ddcf2259da7b/core/runtime/src/main/java/io/quarkus/runtime/util/JavaVersionUtil.java

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaVersionUtil {

    private static final Pattern PATTERN = Pattern.compile("(?:1\\.)?(\\d+)(?:\\..*)?");

    private static boolean IS_JAVA_11_OR_NEWER;
    private static boolean IS_JAVA_13_OR_NEWER;

    static {
        performChecks();
    }

    // visible for testing
    static void performChecks() {
        Matcher matcher = PATTERN.matcher(System.getProperty("java.version", ""));
        if (matcher.matches()) {
            int first = Integer.parseInt(matcher.group(1));
            IS_JAVA_11_OR_NEWER = (first >= 11);
            IS_JAVA_13_OR_NEWER = (first >= 13);
        } else {
            IS_JAVA_11_OR_NEWER = false;
            IS_JAVA_13_OR_NEWER = false;
        }
    }

    public static boolean isJava11OrHigher() {
        return IS_JAVA_11_OR_NEWER;
    }

    public static boolean isJava13OrHigher() {
        return IS_JAVA_13_OR_NEWER;
    }
}
