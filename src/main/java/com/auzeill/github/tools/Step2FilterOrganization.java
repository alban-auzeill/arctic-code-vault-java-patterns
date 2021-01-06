package com.auzeill.github.tools;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.auzeill.github.tools.StringUtils.removeLowerCaseOf;

public class Step2FilterOrganization {

  public static final Path STEP2_PATH = Paths.get("src", "main", "resources", "step2");
  public static final Path ORGANIZATIONS_PATH = STEP2_PATH.resolve("organizations.txt");
  public static final Path NOT_ORGANIZATION_PATH = STEP2_PATH.resolve("not-organization.txt");

  public static Set<String> loadOrganizations() throws IOException {
    return StringUtils.loadOrderedSetIfExist(ORGANIZATIONS_PATH);
  }

  public static Set<String> loadNotOrganization() throws IOException {
    return StringUtils.loadOrderedSetIfExist(NOT_ORGANIZATION_PATH);
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    Set<String> organizations = loadOrganizations();
    Set<String> lowerCaseOrganizations = organizations.stream().map(o -> o.toLowerCase(Locale.ROOT)).collect(Collectors.toCollection(TreeSet::new));
    Set<String> notOrganization = loadNotOrganization();
    GitHubRestApi restApi = new GitHubRestApi();
    Set<String> elementToRemove = new TreeSet<>();
    elementToRemove.addAll(lowerCaseOrganizations);
    elementToRemove.addAll(notOrganization);
    boolean hasChange = false;
    for (String element : Step1ExtractGitHubGreatestHits.loadGreatestRepositoryOwners()) {
      elementToRemove.remove(element);
      if (!lowerCaseOrganizations.contains(element) && !notOrganization.contains(element)) {
        String organizationUrl = "https://api.github.com/orgs/" + element;
        HttpResponse<String> response = restApi.core(HttpUtils.githubAPIRequest(organizationUrl));
        if (response.statusCode() == 404) {
          notOrganization.add(element);
          StringUtils.appendLine(NOT_ORGANIZATION_PATH, element);
          hasChange = true;
          System.out.println("not found " + element);
        } else if (response.statusCode() == 200) {
          JsonObject json = StringUtils.asJsonObject(response.body());
          String organization = json.getAsJsonPrimitive("login").getAsString();
          if (!organization.equalsIgnoreCase(element)) {
            throw new IllegalStateException("Invalid login '" + organization + "' for element '" + element + "'");
          }
          lowerCaseOrganizations.add(element);
          organizations.add(organization);
          StringUtils.appendLine(ORGANIZATIONS_PATH, organization);
          hasChange = true;
          if (organization.equals(element)) {
            System.out.println(organization);
          } else {
            System.out.println(element + " => " + organization);
          }
        } else {
          System.out.println("ERROR " + response.statusCode() + " for " + organizationUrl);
        }
      }
    }
    if (hasChange || !elementToRemove.isEmpty()) {
      StringUtils.saveList(ORGANIZATIONS_PATH, removeLowerCaseOf(loadOrganizations(), elementToRemove));
      StringUtils.saveList(NOT_ORGANIZATION_PATH, removeLowerCaseOf(loadNotOrganization(), elementToRemove));
    }
  }
}
