package com.auzeill.github.tools;

import java.io.IOException;

public class ExtractMain {

  private static final String[] NO_ARGS = {};

  public static void main(String[] args) throws IOException, InterruptedException {
    Step1ExtractGitHubGreatestHits.main(NO_ARGS);
    Step2FilterOrganization.main(NO_ARGS);
    Step3JavaClassesPerOrganization.main(NO_ARGS);
    Step4ExtractRepositoriesMetadata.main(NO_ARGS);
    Step5SortRepositoriesByScore.main(NO_ARGS);
  }

}
