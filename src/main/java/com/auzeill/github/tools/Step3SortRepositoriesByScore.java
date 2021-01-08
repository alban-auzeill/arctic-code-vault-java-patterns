package com.auzeill.github.tools;

import com.auzeill.github.tools.utlis.StringUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class Step3SortRepositoriesByScore {

  public static final Path STEP3_PATH = Paths.get("src", "main", "resources", "step3");

  public static void main(String[] args) throws IOException {
    Map<String, JsonObject> metadata = Step2ExtractRepositoriesMetadata.loadRepositoriesMetadata();

    Set<String> languages = metadata.values().stream()
      .map(Repo::new)
      .map(Repo::languages)
      .flatMap(Collection::stream)
      .collect(Collectors.toCollection(TreeSet::new));
    StringUtils.saveList(STEP3_PATH.resolve("all-languages.txt") , languages);

    Map<String, String> languageAliasMap = new HashMap<>();
    languageAliasMap.put("java", "Java");
    languageAliasMap.put("javascript", "JavaScript");
    languageAliasMap.put("typescript", "TypeScript");
    languageAliasMap.put("csharp", "C#");
    languageAliasMap.put("kotlin", "Kotlin");
    languageAliasMap.put("ruby", "Ruby");
    languageAliasMap.put("go", "Go");
    languageAliasMap.put("scala", "Scala");
    languageAliasMap.put("python", "Python");
    languageAliasMap.put("php", "PHP");
    languageAliasMap.put("html", "HTML");
    languageAliasMap.put("css", "CSS");
    languageAliasMap.put("xml", "XML");
    languageAliasMap.put("vb", "Visual Basic");
    languageAliasMap.put("vb-net", "Visual Basic .NET");
    languageAliasMap.put("c", "C");
    languageAliasMap.put("cpp", "C++");
    languageAliasMap.put("objective-c", "Objective-C");
    languageAliasMap.put("plsql", "PLSQL");
    languageAliasMap.put("abap", "ABAP");
    languageAliasMap.put("tsql", "TSQL");
    languageAliasMap.put("swift", "Swift");
    languageAliasMap.put("apex", "Apex");
    languageAliasMap.put("cobol", "COBOL");

    for (Map.Entry<String, String> languageAliasEntry : languageAliasMap.entrySet()) {
      String alias = languageAliasEntry.getKey();
      String language = languageAliasEntry.getValue();
      List<String> repositories = metadata.values().stream()
        .map(Repo::new)
        .map(repo -> score(repo, language))
        .filter(Objects::nonNull)
        .sorted(Comparator.comparing(repoScore -> -repoScore.score))
        .map(repoScore -> repoScore.repo.fullName())
        .collect(Collectors.toList());
      StringUtils.saveList(bestRepositoriesPath(alias) , repositories);
    }
  }

  public static Path bestRepositoriesPath(String familyName) {
    return STEP3_PATH.resolve("best-" + familyName + "-repositories.txt");
  }

  public enum OwnerType {
    ORGANIZATION, USER
  }

  @Nullable
  public static RepoScore score(Repo repo, String language) {
    double languageRatio = repo.languageRatio(language);
    long languageBytes = repo.languageBytes(language);
    long stars = repo.stars();
    long forks = repo.forks();
    if (languageRatio < 0.5 || languageBytes < 50_000 || stars < 5 || forks < 10) {
      return null;
    }
    double score = Math.log10((double) languageBytes) +
      Math.log10(stars * 1000.0) +
      Math.log10(forks * 100.0) +
      Math.log10(repo.subscribers() * 10.0);

    if (repo.ownerType() == OwnerType.ORGANIZATION) {
      score += 4;
    }
    score *= 1 - ((1 - languageRatio) / 10);
    return new RepoScore(repo, score);
  }

  public static class Repo {

    private final JsonObject json;

    public Repo(JsonObject json) {
      this.json = json;
    }

    public String fullName() {
      return json.getAsJsonPrimitive("full_name").getAsString();
    }

    public String owner() {
      return json.getAsJsonPrimitive("owner").getAsString();
    }

    public OwnerType ownerType() {
      String ownerType = json.getAsJsonPrimitive("owner-type").getAsString();
      switch (ownerType) {
        case "Organization":
          return OwnerType.ORGANIZATION;
        case "User":
          return OwnerType.USER;
        default:
          throw new IllegalStateException("Unexpected owner-type: " + ownerType);
      }
    }

    public String name() {
      return json.getAsJsonPrimitive("name").getAsString();
    }

    public long size() {
      return json.getAsJsonPrimitive("size").getAsLong();
    }

    public long stars() {
      return json.getAsJsonPrimitive("stars").getAsLong();
    }

    public long subscribers() {
      return json.getAsJsonPrimitive("subscribers").getAsLong();
    }

    public long forks() {
      return json.getAsJsonPrimitive("subscribers").getAsLong();
    }

    public JsonObject languagesObject() {
      return json.getAsJsonObject("languages");
    }

    /*
     * For example:
     * ABAP
     * ASP
     * ASP.NET
     * Apex
     * C
     * C#
     * C++
     * COBOL
     * CSS
     * F#
     * Go
     * HTML
     * Java
     * JavaScript
     * Kotlin
     * Objective-C
     * PHP
     * PLSQL
     * Python
     * Ruby
     * Scala
     * Swift
     * TSQL
     * TypeScript
     * Visual Basic
     * Visual Basic .NET
     * Vue
     * XML
     */
    public Set<String> languages() {
      return languagesObject().keySet();
    }

    public long languageBytes(String name) {
      JsonPrimitive bytes = languagesObject().getAsJsonPrimitive(name);
      return bytes == null ? 0 : bytes.getAsLong();
    }

    public long languagesTotalBytes() {
      long total = 0;
      JsonObject object = languagesObject();
      for (String name : object.keySet()) {
        total += object.getAsJsonPrimitive(name).getAsLong();
      }
      return total;
    }

    public double languageRatio(String name) {
      long totalBytes = languagesTotalBytes();
      return totalBytes == 0 ? 0.0 : ((double) languageBytes(name)) / ((double) totalBytes);
    }

  }

  public static class RepoScore {

    private final Repo repo;
    private final double score;

    public RepoScore(Repo repo, double score) {
      this.repo = repo;
      this.score = score;
    }

  }

}
