/* (rank 106) copied from https://github.com/openzipkin/zipkin/blob/ad8520e1e535d52d9f20a94a1086924726158a05/zipkin-storage/elasticsearch/src/test/java/zipkin2/elasticsearch/integration/IgnoredDeprecationWarnings.java
 * Copyright 2015-2020 The OpenZipkin Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package zipkin2.elasticsearch.integration;

import java.util.List;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static java.util.regex.Pattern.compile;

/**
 * When ES emits a deprecation warning header in response to a method being called, the integration
 * test will fail. We cannot always fix our code however to take into account all deprecation
 * warnings, as we have to support multiple versions of ES. For these cases, add the warning message
 * to {@link #IGNORE_THESE_WARNINGS} array so it will not raise an exception anymore.
 */
abstract class IgnoredDeprecationWarnings {

  // These will be matched using header.contains(ignored[i]), so find a unique substring of the
  // warning header for it to be ignored
  static List<Pattern> IGNORE_THESE_WARNINGS = asList(
    compile("Elasticsearch 7\\.x will read, but not allow creation of new indices containing ':'"),
    compile("has index patterns \\[.*] matching patterns from existing older templates"),
    compile("has index patterns \\[.*] matching patterns from existing composable templates")
  );
}
