package com.auzeill.github.tools;

import com.auzeill.github.tools.utlis.GitHubRestApi;
import com.auzeill.github.tools.utlis.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static com.auzeill.github.tools.utlis.HttpUtils.body;
import static com.auzeill.github.tools.utlis.HttpUtils.githubAPIRequest;

public class Step4SearchJavaPatterns {
  private static final Gson GSON = new GsonBuilder()
    .create();

  public static final Path STEP4_PATH = Paths.get("src", "main", "resources", "step4");
  public static final Path SEARCH_RESULT_PATH = STEP4_PATH.resolve("search-result.txt");

  public static Map<String, JsonObject> loadSearchResult() throws IOException {
    Map<String, JsonObject> map = new LinkedHashMap<>();
    if (Files.exists(SEARCH_RESULT_PATH)) {
      for (String line : StringUtils.loadTrimmedList(SEARCH_RESULT_PATH)) {
        JsonObject jsonSummary = GSON.fromJson(line, JsonObject.class);
        String searchKey = jsonSummary.getAsJsonPrimitive("search-key").getAsString();
        map.put(searchKey, jsonSummary);
      }
    }
    return map;
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    Map<String, JsonObject> searchResult = loadSearchResult();
    Map<String, JsonObject> cleanSearchResult = new LinkedHashMap<>();
    Path repositoriesPath = Step3SortRepositoriesByScore.bestRepositoriesPath("java");
    GitHubRestApi restApi = new GitHubRestApi();
    for (String repository : StringUtils.loadTrimmedList(repositoriesPath)) {
      searchIn(searchResult, cleanSearchResult, restApi, repository);
    }
    StringUtils.saveList(SEARCH_RESULT_PATH, cleanSearchResult.values().stream()
      .map(GSON::toJson));
  }

  private static void searchIn(Map<String, JsonObject> searchResult, Map<String, JsonObject> cleanSearchResult,
    GitHubRestApi restApi, String repository) throws IOException, InterruptedException {
    String[] keywords = {
      "java.util.regex.Pattern+Pattern.compile",
      "javax.validation.constraints.Pattern",
      "javax.validation.constraints.Email",
      "org.hibernate.validator.constraints.URL",
      "org.hibernate.validator.constraints.Email",
    };

    for (String keyword : keywords) {
      String searchKey = repository.toLowerCase(Locale.ROOT) + ":" + keyword;
      JsonObject result = searchResult.get(searchKey);
      if (result != null) {
        cleanSearchResult.put(searchKey, result);
      } else {
        String queryUrl = "https://api.github.com/search/code?q=" + keyword + "+language:java+repo:" + repository;
        JsonObject json = StringUtils.asJsonObject(body(restApi.search(githubAPIRequest(queryUrl))));
        JsonArray urls = new JsonArray();
        for (JsonElement item : json.getAsJsonArray("items")) {
          String url = item.getAsJsonObject().getAsJsonPrimitive("url").getAsString();
          urls.add(new JsonPrimitive(url));
        }
        result = new JsonObject();
        result.addProperty("search-key", searchKey);
        result.add("urls", urls);
        searchResult.put(searchKey, result);
        cleanSearchResult.put(searchKey, result);
        String line = GSON.toJson(result);
        System.out.println(line);
        StringUtils.appendLine(SEARCH_RESULT_PATH, line);
      }
    }
  }

}
