package io.cucumber.core.options; // (rank 310) copied from https://github.com/cucumber/cucumber-jvm/blob/a64adbe2264dab2d4a5ee0f2d92d1b193b800a18/core/src/main/java/io/cucumber/core/options/ShellWords.java

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ShellWords {

    private static final Pattern SHELLWORDS_PATTERN = Pattern.compile("[^\\s'\"]+|[']([^']*)[']|[\"]([^\"]*)[\"]");

    private ShellWords() {
    }

    static List<String> parse(String cmdline) {
        List<String> matchList = new ArrayList<>();
        Matcher shellwordsMatcher = SHELLWORDS_PATTERN.matcher(cmdline);
        while (shellwordsMatcher.find()) {
            if (shellwordsMatcher.group(1) != null) {
                matchList.add(shellwordsMatcher.group(1));
            } else {
                String shellword = shellwordsMatcher.group();
                if (shellword.startsWith("\"")
                        && shellword.endsWith("\"")
                        && shellword.length() > 2) {
                    shellword = shellword.substring(1, shellword.length() - 1);
                }
                matchList.add(shellword);
            }
        }
        return matchList;
    }

}
