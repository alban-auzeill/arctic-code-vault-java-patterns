package com.auzeill.github.tools;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static com.auzeill.github.tools.HttpUtils.body;
import static com.auzeill.github.tools.HttpUtils.githubAPIRequest;
import static com.auzeill.github.tools.StringUtils.asJsonObject;

public class Step3JavaClassesPerOrganization {

  public static final Path STEP3_PATH = Paths.get("src", "main", "resources", "step3");
  public static final Path JAVA_CLASSES_PER_ORGANIZATION_PATH = STEP3_PATH.resolve("java-classes-per-organization.txt");

  public static Map<String, Long> classesPerOrganization() throws IOException {
    Map<String, Long> map = new TreeMap<>(StringUtils.LOWER_CASE_COMPARATOR);
    for (String line : StringUtils.loadOrderedSetIfExist(JAVA_CLASSES_PER_ORGANIZATION_PATH)) {
      int sep = line.indexOf(';');
      if (sep != -1) {
        map.put(line.substring(sep + 1), Long.parseLong(line.substring(0, sep)));
      }
    }
    return map;
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    Map<String, Long> classesPerOrganizationMap = classesPerOrganization();
    Set<String> elementToRemove = new TreeSet<>(classesPerOrganizationMap.keySet());
    boolean hasChange = false;
    GitHubRestApi restApi = new GitHubRestApi();
    for (String organization : Step2FilterOrganization.loadOrganizations()) {
      elementToRemove.remove(organization);
      if (!classesPerOrganizationMap.containsKey(organization)) {
        String queryUrl = "https://api.github.com/search/code?q=class+language:java+org:" + organization;
        HttpResponse<String> response = restApi.search(githubAPIRequest(queryUrl));
        long totalCount = -1;
        if (response.statusCode() == 200) {
          JsonObject json = asJsonObject(response.body());
          totalCount = json.getAsJsonPrimitive("total_count").getAsLong();
        } else if (response.statusCode() == 422) {
          // The organization may has no public repository
          String organizationUrl = "https://api.github.com/orgs/" + organization;
          JsonObject json = asJsonObject(body(restApi.core(githubAPIRequest(organizationUrl))));
          int publicRepos = json.getAsJsonPrimitive("public_repos").getAsInt();
          if (publicRepos == 0) {
            totalCount = 0;
          } else {
            System.out.println("ERROR " + response.statusCode() + " and publicRepos == " + publicRepos + " for " + queryUrl);
          }
        } else {
          System.out.println("ERROR " + response.statusCode() + " for " + queryUrl);
        }
        if (totalCount != -1) {
          classesPerOrganizationMap.put(organization, totalCount);
          String line = totalCount + ";" + organization;
          System.out.println(line);
          StringUtils.appendLine(JAVA_CLASSES_PER_ORGANIZATION_PATH, line);
          hasChange = true;
        }
      }
    }
    if (hasChange || !elementToRemove.isEmpty()) {
      Comparator<Map.Entry<String, Long>> comparator = (e1, e2) -> {
        int valueComp = e1.getValue().compareTo(e2.getValue());
        return valueComp != 0 ? -valueComp : e1.getKey().toLowerCase(Locale.ROOT).compareTo(e2.getKey().toLowerCase(Locale.ROOT));
      };
      StringUtils.saveList(JAVA_CLASSES_PER_ORGANIZATION_PATH, classesPerOrganizationMap.entrySet().stream()
        .filter(e -> !elementToRemove.contains(e.getKey()))
        .sorted(comparator)
        .map(e -> e.getValue() + ";" + e.getKey()));
    }
  }

}
