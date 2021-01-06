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

  public static final Path GITHUB_GREATEST_HITS_FILE = Paths.get("src", "main", "resources", "step1", "github-greatest-hits.txt");

  public static void main(String[] args) throws IOException, InterruptedException {
    String body = HttpUtils.body(newTextRequest(SRC_URL));
    Set<String> elements = new TreeSet<>(StringUtils.LOWER_CASE_COMPARATOR);
    for (String line : StringUtils.trimmedNotEmptyLines(body)) {
      int sep = line.indexOf('/');
      if (sep > 1) {
        String element = line.substring(0, sep);
        if (elements.add(element)) {
          System.out.println(element);
        }
      }
    }
    Files.writeString(GITHUB_GREATEST_HITS_FILE, String.join("\n", elements), UTF_8);
  }

}
