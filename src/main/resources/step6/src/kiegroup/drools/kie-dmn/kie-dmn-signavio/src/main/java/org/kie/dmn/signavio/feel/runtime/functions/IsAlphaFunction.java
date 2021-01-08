/* (rank 75) copied from https://github.com/kiegroup/drools/blob/24a0abbc754733e7d4d7db7fbe49fc6d4fb323e8/kie-dmn/kie-dmn-signavio/src/main/java/org/kie/dmn/signavio/feel/runtime/functions/IsAlphaFunction.java
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.dmn.signavio.feel.runtime.functions;

import java.util.regex.Pattern;

import org.kie.dmn.feel.runtime.functions.BaseFEELFunction;
import org.kie.dmn.feel.runtime.functions.FEELFnResult;
import org.kie.dmn.feel.runtime.functions.ParameterName;

public class IsAlphaFunction
        extends BaseFEELFunction {

    private static final Pattern ALPHA_PATTERN = Pattern.compile("[a-zA-Z]+");

    public IsAlphaFunction() {
        super("isAlpha");
    }

    public FEELFnResult<Boolean> invoke(@ParameterName("text") String text) {
        boolean result = ALPHA_PATTERN.matcher(text).matches();
        
        return FEELFnResult.ofResult(result);
    }
}
