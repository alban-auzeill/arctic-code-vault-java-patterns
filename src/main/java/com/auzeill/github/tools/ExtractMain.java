package com.auzeill.github.tools;

import java.io.IOException;

public class ExtractMain {

  private static final String[] NO_ARGS = {};

  public static void main(String[] args) throws IOException, InterruptedException {
    Step1ExtractGitHubGreatestHits.main(NO_ARGS);
    Step2ExtractRepositoriesMetadata.main(NO_ARGS);
    Step3SortRepositoriesByScore.main(NO_ARGS);
    Step4SearchJavaPatterns.main(NO_ARGS);
    Step5DownloadSourceCode.main(NO_ARGS);
    Step6DecodeSourceCode.main(NO_ARGS);
  }

}
