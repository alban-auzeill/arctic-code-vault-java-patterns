package com.auzeill.github.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

import static com.auzeill.github.tools.HttpUtils.newTextRequest;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * GitHub has selected a list of the most-starred and most-depended-upon repositories part of its
 * backup into Arctic Code Vault ( https://archiveprogram.github.com )
 * The goal of this step is to extract the list of item selected by github assuming it is publicly available
 * at https://archiveprogram.github.com/assets/img/archive-repos.txt
 * And filtering to only keep the organization or user name.
 */
public class Step1ExtractGitHubGreatestHits {

  public static final String SRC_URL = "https://archiveprogram.github.com/assets/img/archive-repos.txt";

  public static final Path STEP1_PATH = Paths.get("src", "main", "resources", "step1");
  public static final Path GITHUB_GREATEST_REPOSITORIES_PATH = STEP1_PATH.resolve("github-greatest-repositories.txt");
  public static final Path GITHUB_GREATEST_REPOSITORY_OWNERS_PATH = STEP1_PATH.resolve("github-greatest-repository-owners.txt");

  public static void main(String[] args) throws IOException, InterruptedException {
    if (!Files.exists(GITHUB_GREATEST_REPOSITORIES_PATH) || !Files.exists(GITHUB_GREATEST_REPOSITORY_OWNERS_PATH)) {
      String body = HttpUtils.body(newTextRequest(SRC_URL));
      Set<String> owners = new TreeSet<>(StringUtils.LOWER_CASE_COMPARATOR);
      Set<String> repositories = new TreeSet<>(StringUtils.LOWER_CASE_COMPARATOR);
      for (String repository : StringUtils.trimmedNotEmptyLines(body)) {
        if (repository.contains("/")) {
          repositories.add(repository);
          String owner = owner(repository);
          owners.add(owner);
        } else {
          System.out.println("ERROR, missing '/' separator in repository: " + repository);
        }
      }
      Files.writeString(GITHUB_GREATEST_REPOSITORIES_PATH, String.join("\n", repositories), UTF_8);
      Files.writeString(GITHUB_GREATEST_REPOSITORY_OWNERS_PATH, String.join("\n", owners), UTF_8);
    }
  }

  public static String owner(String repository) {
    int sep = repository.indexOf('/');
    if (sep < 1) {
      throw new IllegalStateException("Missing '/' separator in: " + repository);
    }
    return repository.substring(0, sep);
  }

  public static Set<String> loadGreatestRepositories() throws IOException {
    return StringUtils.loadOrderedSetIfExist(GITHUB_GREATEST_REPOSITORIES_PATH);
  }

  public static Set<String> loadGreatestRepositoryOwners() throws IOException {
    return StringUtils.loadOrderedSetIfExist(GITHUB_GREATEST_REPOSITORY_OWNERS_PATH);
  }

}
