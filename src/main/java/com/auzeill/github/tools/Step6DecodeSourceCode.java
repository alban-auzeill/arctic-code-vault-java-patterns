package com.auzeill.github.tools;

import com.auzeill.github.tools.utlis.StringUtils;
import com.google.gson.JsonObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Step6DecodeSourceCode {

  public static final Path STEP6_PATH = Paths.get("src", "main", "resources", "step6");
  public static final Path SRC_PATH = STEP6_PATH.resolve("src");
  private static final Pattern REPOSITORY_REGEX = Pattern.compile("^https://github\\.com/([^/]++/[^/]++)/");

  public static void main(String[] args) throws IOException {
    if (!Files.exists(SRC_PATH)) {
      Files.createDirectory(SRC_PATH);
    }
    Map<String, Integer> repositoriesRanking = loadRepositoryRanking();
    Map<String, JsonObject> urlContents = Step5DownloadSourceCode.loadUrlContents();
    for (JsonObject json : urlContents.values()) {
      JsonObject data = json.getAsJsonObject("data");
      String htmlUrl = data.getAsJsonPrimitive("html_url").getAsString();
      Path destFile = localPath(htmlUrl);
      if (!Files.exists(destFile)) {
        Files.createDirectories(destFile.getParent());
        String path = data.getAsJsonPrimitive("path").getAsString();
        System.out.println(path);
        String encoding = data.getAsJsonPrimitive("encoding").getAsString();
        if (!encoding.equals("base64")) {
          throw new IllegalArgumentException("Unsupported encoding: " + encoding);
        }
        String encodedContent = data.getAsJsonPrimitive("content").getAsString();
        byte[] content = Base64.getDecoder().decode(encodedContent.replaceAll("[\r\n]", ""));
        Files.write(destFile, insertUrlInSourceCode(content, htmlUrl, ranking(repositoriesRanking, htmlUrl)));
      }

    }

  }

  public static Map<String, Integer> loadRepositoryRanking() throws IOException {
    Map<String, Integer> repositoriesRanking = new HashMap<>();
    Path repositoriesPath = Step3SortRepositoriesByScore.bestRepositoriesPath("java");
    List<String> repositories = StringUtils.loadTrimmedList(repositoriesPath);
    for (int i = 0; i < repositories.size(); i++) {
      repositoriesRanking.put(repositories.get(i), i + 1);
    }
    return repositoriesRanking;
  }

  enum CommentState {
    NO_COMMENT, FIRST_SLASH, LINE_COMMENT, BLOCK_COMMENT, END_STAR
  }

  private static Path localPath(String htmlUrl) {
    return SRC_PATH.resolve(Paths.get(htmlUrl
      .replaceFirst("^https://github\\.com/", "")
      .replaceFirst("/blob/[0-9a-f]{40}/", "/")
      .replace('/', File.separatorChar)));
  }

  private static int ranking(Map<String, Integer> repositoriesRanking, String htmlUrl) {
    int defaultValue = repositoriesRanking.size() + 1;
    Matcher matcher = REPOSITORY_REGEX.matcher(htmlUrl);
    if (matcher.find()) {
      String repository = matcher.group(1);
      Integer rank = repositoriesRanking.get(repository);
      if (rank != null) {
        return rank;
      }
      System.out.println("WARN failed to find ranking for " + repository);
    } else {
      System.out.println("WARN failed to match repository pattern: " + htmlUrl);
    }
    return defaultValue;
  }

  private static byte[] insertUrlInSourceCode(byte[] content, String htmlUrl, int ranking) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int i = 0;
    CommentState state = CommentState.NO_COMMENT;
    while (i < content.length) {
      byte ch = content[i];
      if (ch == '/') {
        switch (state) {
          case NO_COMMENT:
            state = CommentState.FIRST_SLASH;
            break;
          case FIRST_SLASH:
            state = CommentState.LINE_COMMENT;
            break;
          case END_STAR:
            state = CommentState.NO_COMMENT;
            break;
        }
      } else if (ch == '*') {
        switch (state) {
          case FIRST_SLASH:
            state = CommentState.BLOCK_COMMENT;
            break;
          case BLOCK_COMMENT:
          case END_STAR:
            state = CommentState.END_STAR;
            break;
        }
      } else {
        switch (state) {
          case FIRST_SLASH:
            state = CommentState.NO_COMMENT;
            break;
          case END_STAR:
            state = CommentState.BLOCK_COMMENT;
            break;
        }
      }
      if (ch == '\r' || ch == '\n') {
        String commentPrefix = (state == CommentState.LINE_COMMENT || state == CommentState.BLOCK_COMMENT) ? "" : " //";
        out.write((commentPrefix + " (rank " + ranking + ") copied from " + htmlUrl).getBytes(UTF_8));
        out.write(content, i, content.length - i);
        break;
      }
      out.write(ch);
      i++;
    }
    return out.toByteArray();
  }
}
