package com.auzeill.github.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static com.auzeill.github.tools.HttpUtils.body;
import static com.auzeill.github.tools.HttpUtils.githubAPIRequest;
import static com.auzeill.github.tools.StringUtils.asJsonObject;

public class Step7DownloadSourceCode {

  private static final Gson GSON = new GsonBuilder()
    .create();

  public static final Path STEP7_PATH = Paths.get("src", "main", "resources", "step7");
  public static final Path URL_CONTENT_PATH = STEP7_PATH.resolve("url-contents.txt");
  public static final Path URL_ERROR_PATH = STEP7_PATH.resolve("url-errors.txt");

  public static Map<String, JsonObject> loadUrlContents() throws IOException {
    Map<String, JsonObject> map = new LinkedHashMap<>();
    if (Files.exists(URL_CONTENT_PATH)) {
      for (String line : StringUtils.loadTrimmedList(URL_CONTENT_PATH)) {
        JsonObject json = GSON.fromJson(line, JsonObject.class);
        String url = json.getAsJsonPrimitive("url").getAsString();
        map.put(url, json);
      }
    }
    return map;
  }

  public static Set<String> loadUrlErrors() throws IOException {
    Set<String> urls = new LinkedHashSet<>();
    if (Files.exists(URL_ERROR_PATH)) {
      urls.addAll(StringUtils.loadTrimmedList(URL_ERROR_PATH));
    }
    return urls;
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    Map<String, JsonObject> urlContents = loadUrlContents();
    Map<String, JsonObject> cleanUrlContents = new LinkedHashMap<>();
    Set<String> urlErrors = loadUrlErrors();
    Set<String> cleanUrlErrors = new LinkedHashSet<>();
    Map<String, JsonObject> searchResult = Step6SearchJavaPatterns.loadSearchResult();
    Set<String> uniqueUrls = new LinkedHashSet<>();
    for (JsonObject json : searchResult.values()) {
      JsonArray urls = json.getAsJsonArray("urls");
      for (JsonElement urlElement : urls) {
        uniqueUrls.add(urlElement.getAsString());
      }
    }
    GitHubRestApi restApi = new GitHubRestApi();
    for (String url : uniqueUrls) {
      JsonObject json = urlContents.get(url);
      if (json != null) {
        cleanUrlContents.put(url, json);
      } else if (urlErrors.contains(url)) {
        cleanUrlErrors.add(url);
      } else {
        try {
          JsonObject data = asJsonObject(body(restApi.core(githubAPIRequest(url))));
          String name = data.getAsJsonPrimitive("name").getAsString();
          if (!name.endsWith(".java")) {
            throw new IllegalArgumentException("Skip non java files: " + name);
          }
          String encoding = data.getAsJsonPrimitive("encoding").getAsString();
          if (!encoding.equals("base64")) {
            throw new IllegalArgumentException("Unsupported encoding: " + encoding);
          }
          String type = data.getAsJsonPrimitive("type").getAsString();
          if (!type.equals("file")) {
            throw new IllegalArgumentException("Unsupported type: " + type);
          }
          json = new JsonObject();
          json.addProperty("url", url);
          json.add("data", data);
          urlContents.put(url, json);
          cleanUrlContents.put(url, json);
          String line = GSON.toJson(json);
          System.out.println(line);
          StringUtils.appendLine(URL_CONTENT_PATH, line);
        } catch (Exception ex) {
          urlErrors.add(url);
          cleanUrlErrors.add(url);
          System.out.println("ERROR on " + url + " " + ex.getMessage());
          StringUtils.appendLine(URL_ERROR_PATH, url);
        }
      }
    }
    StringUtils.saveList(URL_CONTENT_PATH, cleanUrlContents.values().stream().map(GSON::toJson));
    StringUtils.saveList(URL_ERROR_PATH, cleanUrlErrors);
  }

}
