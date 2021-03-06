/* (rank 135) copied from https://github.com/apache/beam/blob/7c43ab6a8df9b23caa7321fddff9a032a71908f6/sdks/java/core/src/main/java/org/apache/beam/sdk/testing/RegexMatcher.java
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.beam.sdk.testing;

import java.util.regex.Pattern;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/** Hamcrest matcher to assert a string matches a pattern. */
public class RegexMatcher extends BaseMatcher<String> {
  private final Pattern pattern;

  public RegexMatcher(String regex) {
    this.pattern = Pattern.compile(regex);
  }

  @Override
  public boolean matches(Object o) {
    if (!(o instanceof String)) {
      return false;
    }
    return pattern.matcher((String) o).matches();
  }

  @Override
  public void describeTo(Description description) {
    description.appendText(String.format("matches regular expression %s", pattern));
  }

  public static RegexMatcher matches(String regex) {
    return new RegexMatcher(regex);
  }
}
