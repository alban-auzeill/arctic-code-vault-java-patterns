/* (rank 65) copied from https://github.com/facebook/buck/blob/ae8e8fc013413f0144b73971eceeff3fd6f51d6c/src/com/facebook/buck/cxx/CxxInferSourceFilter.java
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook.buck.cxx;

import com.facebook.buck.cxx.toolchain.InferBuckConfig;
import java.util.Optional;
import java.util.regex.Pattern;

class CxxInferSourceFilter {

  private final Optional<Pattern> blacklistRegex;

  CxxInferSourceFilter(InferBuckConfig inferConfig) {
    Optional<String> rawFilterRegex = inferConfig.getBlacklistRegex();

    blacklistRegex = rawFilterRegex.map(Pattern::compile);
  }

  public boolean isBlacklisted(CxxSource source) {
    return blacklistRegex.isPresent()
        && blacklistRegex.get().matcher(source.getPath().toString()).matches();
  }
}
