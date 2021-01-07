package com.auzeill.github.tools;

import com.google.gson.JsonObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Step8DecodeSourceCode {

  public static final Path STEP8_PATH = Paths.get("src", "main", "resources", "step8");
  public static final Path SRC_PATH = STEP8_PATH.resolve("src");

  public static void main(String[] args) throws IOException {
    if (!Files.exists(SRC_PATH)) {
      Files.createDirectory(SRC_PATH);
    }

    Map<String, JsonObject> urlContents = Step7DownloadSourceCode.loadUrlContents();
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
        Files.write(destFile, insertUrlInSourceCode(content, htmlUrl));
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

  enum CommentState {
    NO_COMMENT, FIRST_SLASH, LINE_COMMENT, BLOCK_COMMENT, END_STAR
  }

  private static Path localPath(String htmlUrl) {
    return SRC_PATH.resolve(Paths.get(htmlUrl
      .replaceFirst("^https://github\\.com/", "")
      .replaceFirst("/blob/[0-9a-f]{40}/", "/")
      .replace('/', File.separatorChar)));
  }

  private static byte[] insertUrlInSourceCode(byte[] content, String htmlUrl) throws IOException {
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
        if (state == CommentState.LINE_COMMENT || state == CommentState.BLOCK_COMMENT) {
          out.write((" copied from " + htmlUrl).getBytes(UTF_8));
        } else {
          out.write((" // copied from " + htmlUrl).getBytes(UTF_8));
        }
        out.write(content, i, content.length - i);
        break;
      }
      out.write(ch);
      i++;
    }
    return out.toByteArray();
  }
}
