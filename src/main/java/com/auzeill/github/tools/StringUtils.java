package com.auzeill.github.tools;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class StringUtils {

  public static final Comparator<String> LOWER_CASE_COMPARATOR = Comparator.comparing(k -> k.toLowerCase(Locale.ROOT));

  private StringUtils() {
    // utility class
  }

  public static List<String> lines(String text) {
    return Arrays.stream(text.split("\r\n|\r|\n"))
      .collect(Collectors.toList());
  }

  public static List<String> notEmptyLines(String text) {
    return lines(text).stream()
      .filter(line -> !line.isEmpty())
      .collect(Collectors.toList());
  }

  public static List<String> trimmedNotEmptyLines(String text) {
    return lines(text).stream()
      .map(String::trim)
      .filter(line -> !line.isEmpty())
      .collect(Collectors.toList());
  }

}
