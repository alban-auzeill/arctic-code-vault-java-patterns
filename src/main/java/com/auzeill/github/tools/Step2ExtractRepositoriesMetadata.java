package com.auzeill.github.tools;

import com.auzeill.github.tools.utlis.GitHubRestApi;
import com.auzeill.github.tools.utlis.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static com.auzeill.github.tools.utlis.HttpUtils.body;
import static com.auzeill.github.tools.utlis.HttpUtils.githubAPIRequest;
import static com.auzeill.github.tools.utlis.StringUtils.asJsonObject;

public class Step2ExtractRepositoriesMetadata {
  private static final Gson GSON = new GsonBuilder()
    .create();

  public static final Path STEP2_PATH = Paths.get("src", "main", "resources", "step2");
  public static final Path REPOSITORY_METADATA_PATH = STEP2_PATH.resolve("repositories-metadata.txt");
  public static final Path INVALID_REPOSITORIES_PATH = STEP2_PATH.resolve("invalid-repositories.txt");
  public static final Path MOVED_REPOSITORIES_PATH = STEP2_PATH.resolve("moved-repositories.txt");

  public static Map<String, JsonObject> loadRepositoriesMetadata() throws IOException {
    Map<String, JsonObject> map = new TreeMap<>();
    for (String line : StringUtils.loadOrderedSetIfExist(REPOSITORY_METADATA_PATH)) {
      JsonObject jsonSummary = GSON.fromJson(line, JsonObject.class);
      String lowerCaseName = jsonSummary.getAsJsonPrimitive("full_name").getAsString().toLowerCase(Locale.ROOT);
      map.put(lowerCaseName, jsonSummary);
    }
    return map;
  }

  public static Set<String> loadInvalidRepositories() throws IOException {
    return StringUtils.loadOrderedSetIfExist(INVALID_REPOSITORIES_PATH);
  }

  public static Map<String, String> loadMovedRepositories() throws IOException {
    Map<String, String> map = new TreeMap<>(StringUtils.LOWER_CASE_COMPARATOR);
    for (String line : StringUtils.loadOrderedSetIfExist(MOVED_REPOSITORIES_PATH)) {
      int sep = line.indexOf(';');
      if (sep != -1) {
        map.put(line.substring(0, sep), line.substring(sep + 1));
      }
    }
    return map;
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    Map<String, JsonObject> metadataMap = loadRepositoriesMetadata();
    Map<String, JsonObject> cleanMetadataMap = new TreeMap<>(StringUtils.LOWER_CASE_COMPARATOR);
    Set<String> greatestRepositories = Step1ExtractGitHubGreatestHits.loadGreatestRepositories();
    Set<String> invalidRepositories = loadInvalidRepositories();
    Set<String> cleanInvalidRepositories = new TreeSet<>(StringUtils.LOWER_CASE_COMPARATOR);
    Map<String, String> movedRepositories = loadMovedRepositories();
    Map<String, String> cleanMovedRepositories = new TreeMap<>(StringUtils.LOWER_CASE_COMPARATOR);
    GitHubRestApi restApi = new GitHubRestApi();
    for (String oldRepositoryName : greatestRepositories) {
      oldRepositoryName = oldRepositoryName.toLowerCase(Locale.ROOT);
      String newRepositoryName = movedRepositories.getOrDefault(oldRepositoryName, oldRepositoryName);
      if (!oldRepositoryName.equals(newRepositoryName)) {
        cleanMovedRepositories.put(oldRepositoryName, newRepositoryName);
      }
      JsonObject existingJsonSummary = metadataMap.get(newRepositoryName);
      if (existingJsonSummary != null) {
        cleanMetadataMap.put(newRepositoryName, existingJsonSummary);
      } else if (invalidRepositories.contains(oldRepositoryName)) {
        cleanInvalidRepositories.add(oldRepositoryName);
      } else {
        String organizationUrl = "https://api.github.com/repos/" + newRepositoryName;
        HttpResponse<String> response = restApi.core(githubAPIRequest(organizationUrl));
        if (response.statusCode() == 403 || response.statusCode() == 404 || response.statusCode() == 451) {
          System.out.println("ERROR " + response.statusCode() + " for repository " + newRepositoryName);
          StringUtils.appendLine(INVALID_REPOSITORIES_PATH, oldRepositoryName);
          invalidRepositories.add(oldRepositoryName);
          cleanInvalidRepositories.add(oldRepositoryName);
        } else {
          JsonObject json = asJsonObject(body(response));
          String repositoryFullNameLowerCase = json.get("full_name").getAsString().toLowerCase(Locale.ROOT);
          if (!repositoryFullNameLowerCase.equals(newRepositoryName)) {
            String line = oldRepositoryName + ";" + repositoryFullNameLowerCase;
            System.out.println(line);
            StringUtils.appendLine(MOVED_REPOSITORIES_PATH, line);
            movedRepositories.put(oldRepositoryName, repositoryFullNameLowerCase);
            cleanMovedRepositories.put(oldRepositoryName, repositoryFullNameLowerCase);
          }
          JsonObject owner = json.getAsJsonObject("owner");
          JsonObject jsonSummary = new JsonObject();
          jsonSummary.add("full_name", json.get("full_name"));
          jsonSummary.add("owner", owner.get("login"));
          jsonSummary.add("owner-type", owner.get("type"));
          jsonSummary.add("name", json.get("name"));
          jsonSummary.add("size", json.get("size"));
          jsonSummary.add("stars", json.get("stargazers_count"));
          jsonSummary.add("subscribers", json.get("subscribers_count"));
          jsonSummary.add("forks", json.get("forks"));

          String languagesUrl = "https://api.github.com/repos/" + repositoryFullNameLowerCase + "/languages";
          jsonSummary.add("languages", asJsonObject(body(restApi.core(githubAPIRequest(languagesUrl)))));

          metadataMap.put(repositoryFullNameLowerCase, jsonSummary);
          cleanMetadataMap.put(repositoryFullNameLowerCase, jsonSummary);
          String jsonLine = GSON.toJson(jsonSummary);
          System.out.println(jsonLine);
          StringUtils.appendLine(REPOSITORY_METADATA_PATH, jsonLine);
        }
      }
    }

    StringUtils.saveList(INVALID_REPOSITORIES_PATH, cleanInvalidRepositories);
    StringUtils.saveList(REPOSITORY_METADATA_PATH, cleanMetadataMap.values().stream()
      .map(GSON::toJson));
    StringUtils.saveList(MOVED_REPOSITORIES_PATH, cleanMovedRepositories.entrySet().stream()
      .map(e -> e.getKey() + ";" + e.getValue()));
  }

}
