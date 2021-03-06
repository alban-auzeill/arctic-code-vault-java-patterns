/* (rank 84) copied from https://github.com/apache/hive/blob/4446414f4478091db1eb20bc782a5c1825356153/testutils/ptest2/src/main/java/org/apache/hive/ptest/execution/TestCheckPhase.java
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.hive.ptest.execution;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestCheckPhase extends Phase {
  private final File mPatchFile;
  private final String mPatchURL;
  private Set<String> modifiedTestFiles;
  private static Cache<String, Boolean> patchUrls = CacheBuilder.newBuilder().expireAfterWrite
          (7, TimeUnit.DAYS).maximumSize(10000).build();

  private static final Pattern fileNameFromDiff = Pattern.compile("[/][^\\s]*");
  private static final Pattern javaTest = Pattern.compile("Test.*java");

  public TestCheckPhase(List<HostExecutor> hostExecutors,
                        LocalCommandFactory localCommandFactory,
                        ImmutableMap<String, String> templateDefaults,
                        String patchUrl, File patchFile, Logger logger, Set<String> modifiedTestFiles) {
    super(hostExecutors, localCommandFactory, templateDefaults, logger);
    this.mPatchFile = patchFile;
    this.mPatchURL = patchUrl;
    this.modifiedTestFiles = modifiedTestFiles;
  }
  @Override
  public void execute() throws Exception {
    if (mPatchURL != null) {
      boolean patchUrlWasSeen = patchUrls.asMap().containsKey(mPatchURL);
      if (!patchUrlWasSeen) {
        patchUrls.put(mPatchURL, true);
      } else {
        throw new Exception("Patch URL " + mPatchURL + " was found in seen patch url's cache and " +
                "a test was probably run already on it. Aborting...");
      }
    }
    if(mPatchFile != null) {
      logger.info("Reading patchfile " + mPatchFile.getAbsolutePath());
      FileReader fr = null;
      try {
        fr = new FileReader(mPatchFile);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
          if(line.startsWith("+++")) {
            logger.info("Searching line : " + line);
            Matcher fileNameMatcher = fileNameFromDiff.matcher(line);
            if (fileNameMatcher.find()) {
              String filePath = fileNameMatcher.group(0);
              String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
              Matcher javaTestMatcher = javaTest.matcher(fileName);
              if (javaTestMatcher.find() || fileName.endsWith(".q")) {
                modifiedTestFiles.add(fileName);
              }
            }
          }
        }
      } finally {
        fr.close();
      }
    } else {
      logger.error("Patch file is null");
    }
  }
}
