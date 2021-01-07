package com.auzeill.github.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class StringUtils {

  public static final Gson GSON = new GsonBuilder()
    .setPrettyPrinting()
    .create();

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

  public static List<String> loadTrimmedList(Path path) throws IOException {
    return trimmedNotEmptyLines(Files.readString(path, UTF_8));
  }

  public static Set<String> loadOrderedSetIfExist(Path path) throws IOException {
    Set<String> elements = new TreeSet<>(StringUtils.LOWER_CASE_COMPARATOR);
    if (Files.exists(path)) {
      elements.addAll(StringUtils.loadTrimmedList(path));
    }
    return elements;
  }

  public static void saveList(Path path, Collection<String> list) throws IOException {
    saveList(path, list.stream());
  }

  public static void saveList(Path path, Stream<String> stream) throws IOException {
    StringBuilder out = new StringBuilder();
    stream.forEach(line -> out.append(line).append('\n'));
    String content = out.toString();
    String previousContent = Files.exists(path) ? Files.readString(path, UTF_8) : null;
    if (!content.equals(previousContent)) {
      Files.writeString(path, content, UTF_8);
    }
  }

  public static void appendLine(Path path, String line) throws IOException {
    append(path, line + "\n");
  }

  public static void append(Path path, String text) throws IOException {
    try (OutputStream out = new FileOutputStream(path.toFile(), true)) {
      out.write(text.getBytes(UTF_8));
    }
  }

  public static JsonObject asJsonObject(String json) {
    return GSON.fromJson(json, JsonObject.class);
  }

  public static Set<String> removeLowerCaseOf(Set<String> list, Set<String> lowerCaseElementsToRemove) {
    Set<String> elements = new TreeSet<>(StringUtils.LOWER_CASE_COMPARATOR);
    for (String element : list) {
      if (!lowerCaseElementsToRemove.contains(element.toLowerCase(Locale.ROOT))) {
        elements.add(element);
      }
    }
    return elements;
  }

}
