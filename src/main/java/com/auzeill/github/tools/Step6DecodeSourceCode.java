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

      /*
       * json example:
       * {
       * "url":
       * "https://api.github.com/repositories/507775/contents/x-pack/plugin/sql/sql-cli/src/main/java/org/elasticsearch/xpack/sql/cli/command/ClearScreenCliCommand.java?ref\u003dfeab123ba400b150f3dcd04dd27cf57474b70d5a",
       * "data": {
       * "name":"ClearScreenCliCommand.java",
       * "path":"x-pack/plugin/sql/sql-cli/src/main/java/org/elasticsearch/xpack/sql/cli/command/ClearScreenCliCommand.java",
       * "sha":"ffde1ec556a0d2936a3dc03d6ab27129854ad561",
       * "size":801,
       * "url":
       * "https://api.github.com/repos/elastic/elasticsearch/contents/x-pack/plugin/sql/sql-cli/src/main/java/org/elasticsearch/xpack/sql/cli/command/ClearScreenCliCommand.java?ref\u003dfeab123ba400b150f3dcd04dd27cf57474b70d5a",
       * "html_url":
       * "https://github.com/elastic/elasticsearch/blob/feab123ba400b150f3dcd04dd27cf57474b70d5a/x-pack/plugin/sql/sql-cli/src/main/java/org/elasticsearch/xpack/sql/cli/command/ClearScreenCliCommand.java",
       * "git_url":"https://api.github.com/repos/elastic/elasticsearch/git/blobs/ffde1ec556a0d2936a3dc03d6ab27129854ad561",
       * "download_url":
       * "https://raw.githubusercontent.com/elastic/elasticsearch/feab123ba400b150f3dcd04dd27cf57474b70d5a/x-pack/plugin/sql/sql-cli/src/main/java/org/elasticsearch/xpack/sql/cli/command/ClearScreenCliCommand.java",
       * "type":"file",
       * "content":
       * "LyoKICogQ29weXJpZ2h0IEVsYXN0aWNzZWFyY2ggQi5WLiBhbmQvb3IgbGlj\nZW5zZWQgdG8gRWxhc3RpY3NlYXJjaCBCLlYuIHVuZGVyIG9uZQogKiBvciBt\nb3JlIGNvbnRyaWJ1dG9yIGxpY2Vuc2UgYWdyZWVtZW50cy4gTGljZW5zZWQg\ndW5kZXIgdGhlIEVsYXN0aWMgTGljZW5zZTsKICogeW91IG1heSBub3QgdXNl\nIHRoaXMgZmlsZSBleGNlcHQgaW4gY29tcGxpYW5jZSB3aXRoIHRoZSBFbGFz\ndGljIExpY2Vuc2UuCiAqLwpwYWNrYWdlIG9yZy5lbGFzdGljc2VhcmNoLnhw\nYWNrLnNxbC5jbGkuY29tbWFuZDsKCmltcG9ydCBvcmcuZWxhc3RpY3NlYXJj\naC54cGFjay5zcWwuY2xpLkNsaVRlcm1pbmFsOwoKaW1wb3J0IGphdmEudXRp\nbC5yZWdleC5NYXRjaGVyOwppbXBvcnQgamF2YS51dGlsLnJlZ2V4LlBhdHRl\ncm47CgovKioKICogY2xzIGNvbW1hbmQgdGhhdCBjbGVhbnMgdGhlIHNjcmVl\nbgogKi8KcHVibGljIGNsYXNzIENsZWFyU2NyZWVuQ2xpQ29tbWFuZCBleHRl\nbmRzIEFic3RyYWN0Q2xpQ29tbWFuZCB7CgogICAgcHVibGljIENsZWFyU2Ny\nZWVuQ2xpQ29tbWFuZCgpIHsKICAgICAgICBzdXBlcihQYXR0ZXJuLmNvbXBp\nbGUoImNscyIsIFBhdHRlcm4uQ0FTRV9JTlNFTlNJVElWRSkpOwogICAgfQoK\nICAgIEBPdmVycmlkZQogICAgcHJvdGVjdGVkIGJvb2xlYW4gZG9IYW5kbGUo\nQ2xpVGVybWluYWwgdGVybWluYWwsIENsaVNlc3Npb24gY2xpU2Vzc2lvbiwg\nTWF0Y2hlciBtLCBTdHJpbmcgbGluZSkgewogICAgICAgIHRlcm1pbmFsLmNs\nZWFyKCk7CiAgICAgICAgcmV0dXJuIHRydWU7CiAgICB9Cn0K\n",
       * "encoding":"base64",
       * "_links": {
       * "self":
       * "https://api.github.com/repos/elastic/elasticsearch/contents/x-pack/plugin/sql/sql-cli/src/main/java/org/elasticsearch/xpack/sql/cli/command/ClearScreenCliCommand.java?ref\u003dfeab123ba400b150f3dcd04dd27cf57474b70d5a",
       * "git":"https://api.github.com/repos/elastic/elasticsearch/git/blobs/ffde1ec556a0d2936a3dc03d6ab27129854ad561",
       * "html":
       * "https://github.com/elastic/elasticsearch/blob/feab123ba400b150f3dcd04dd27cf57474b70d5a/x-pack/plugin/sql/sql-cli/src/main/java/org/elasticsearch/xpack/sql/cli/command/ClearScreenCliCommand.java"
       * }
       * }
       * }
       */
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
