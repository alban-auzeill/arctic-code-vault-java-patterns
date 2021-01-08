/******************************************************************************* (rank 47) copied from https://github.com/eclipse/deeplearning4j/blob/881a672fa13f3b37177eb2ea023e39b0de893645/deeplearning4j/deeplearning4j-nlp-parent/deeplearning4j-nlp/src/main/java/org/deeplearning4j/text/tokenization/tokenizer/preprocessor/StringCleaning.java
 * Copyright (c) 2015-2018 Skymind, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ******************************************************************************/

package org.deeplearning4j.text.tokenization.tokenizer.preprocessor;

import java.util.regex.Pattern;

/**
 * Various string cleaning utils
 * @author Adam GIbson
 */
public class StringCleaning {

    private static final Pattern punctPattern = Pattern.compile("[\\d\\.:,\"\'\\(\\)\\[\\]|/?!;]+");

    private StringCleaning() {}

    /**
     * Removes ASCII punctuation marks, which are: 0123456789.:,"'()[]|/?!;
     * @param base the base string
     * @return the cleaned string
     */
    public static String stripPunct(String base) {
        return punctPattern.matcher(base).replaceAll("");
    }
}
