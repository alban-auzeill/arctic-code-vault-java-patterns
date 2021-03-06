/* (rank 23) copied from https://github.com/apache/skywalking/blob/bc64c6a12770031478d29e2f19004796584374c9/test/e2e/e2e-common/src/main/java/org/apache/skywalking/e2e/utils/Envs.java
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.e2e.utils;

import com.google.common.base.Strings;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Envs {
    public static String resolve(final String text) {
        if (Strings.isNullOrEmpty(text)) {
            return "";
        }

        final Pattern pattern = Pattern.compile("\\$\\{(?<name>\\w+)}");
        final Matcher matcher = pattern.matcher(text);
        final StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            final String name = matcher.group("name");
            final String value = System.getenv(name);
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(Strings.nullToEmpty(value)));
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }
}
