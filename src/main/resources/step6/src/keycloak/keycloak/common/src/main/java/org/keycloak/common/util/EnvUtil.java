/* (rank 73) copied from https://github.com/keycloak/keycloak/blob/66dfa32cd569a7416de21b4dc04db212e8fccce5/common/src/main/java/org/keycloak/common/util/EnvUtil.java
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Replaces any ${} strings with their corresponding system property.
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public final class EnvUtil {
    private static final Pattern p = Pattern.compile("[$][{]([^}]+)[}]");

    private EnvUtil() {

    }

    /**
     * Replaces any ${} strings with their corresponding system property.
     *
     * @param val
     * @return
     */
    public static String replace(String val) {
        Matcher matcher = p.matcher(val);
        StringBuffer buf = new StringBuffer();
        while (matcher.find()) {
            String envVar = matcher.group(1);
            String envVal = System.getProperty(envVar);
            if (envVal == null) envVal = "NOT-SPECIFIED";
            matcher.appendReplacement(buf, envVal.replace("\\", "\\\\"));
        }
        matcher.appendTail(buf);
        return buf.toString();
    }
}


