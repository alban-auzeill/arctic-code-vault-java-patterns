package com.auzeill.github.tools;

import com.google.gson.JsonObject;
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
      String sha = data.getAsJsonPrimitive("sha").getAsString();
      String packageName = "pkg" + sha;
      Path packageFolder = SRC_PATH.resolve(packageName);
      if (!Files.exists(packageFolder)) {
        String name = data.getAsJsonPrimitive("name").getAsString();
        System.out.println(name);
        String path = data.getAsJsonPrimitive("path").getAsString();
        String htmlUrl = data.getAsJsonPrimitive("html_url").getAsString();
        String encoding = data.getAsJsonPrimitive("encoding").getAsString();
        if (!encoding.equals("base64")) {
          throw new IllegalArgumentException("Unsupported encoding: " + encoding);
        }
        String encodedContent = data.getAsJsonPrimitive("content").getAsString();
        byte[] content = Base64.getDecoder().decode(encodedContent.replaceAll("[\r\n]", ""));
        Files.createDirectory(packageFolder);
        Files.writeString(packageFolder.resolve("package-info.java"), "" +
          "/**\n" +
          " * url : " + htmlUrl + "\n" +
          " * path: " + path + "\n" +
          " * name: " + name + "\n" +
          " */\n" +
          "package " + packageName + ";\n", UTF_8);

        Path destFile = packageFolder.resolve(name);
        Files.write(destFile, content);
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
}
