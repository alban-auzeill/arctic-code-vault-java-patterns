/* (rank 138) copied from https://github.com/apache/nifi/blob/f9ae3bb9c970cd8d6d1d9e10f07cab9bdb66baa9/nifi-commons/nifi-expression-language/src/main/java/org/apache/nifi/attribute/expression/language/evaluation/functions/FindEvaluator.java
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
 */
package org.apache.nifi.attribute.expression.language.evaluation.functions;

import org.apache.nifi.attribute.expression.language.EvaluationContext;
import org.apache.nifi.attribute.expression.language.StandardEvaluationContext;
import org.apache.nifi.attribute.expression.language.evaluation.BooleanEvaluator;
import org.apache.nifi.attribute.expression.language.evaluation.BooleanQueryResult;
import org.apache.nifi.attribute.expression.language.evaluation.Evaluator;
import org.apache.nifi.attribute.expression.language.evaluation.QueryResult;
import org.apache.nifi.attribute.expression.language.evaluation.literals.StringLiteralEvaluator;

import java.util.Collections;
import java.util.regex.Pattern;

public class FindEvaluator extends BooleanEvaluator {

    private final Evaluator<String> subject;
    private final Evaluator<String> search;

    private final Pattern compiledPattern;

    public FindEvaluator(final Evaluator<String> subject, final Evaluator<String> search) {
        this.subject = subject;
        this.search = search;

        // if the search string is a literal, we don't need to evaluate it each time; we can just
        // pre-compile it. Otherwise, it must be compiled every time.
        if (search instanceof StringLiteralEvaluator) {
            this.compiledPattern = Pattern.compile(search.evaluate(new StandardEvaluationContext(Collections.emptyMap())).getValue());
        } else {
            this.compiledPattern = null;
        }
    }

    @Override
    public QueryResult<Boolean> evaluate(final EvaluationContext evaluationContext) {
        final String subjectValue = subject.evaluate(evaluationContext).getValue();
        if (subjectValue == null) {
            return new BooleanQueryResult(false);
        }
        final Pattern pattern;
        if (compiledPattern == null) {
            String expression = search.evaluate(evaluationContext).getValue();
            if (expression == null) {
                return new BooleanQueryResult(false);
            }
            pattern = Pattern.compile(expression);
        } else {
            pattern = compiledPattern;
        }

        final boolean found = pattern.matcher(subjectValue).find();

        return new BooleanQueryResult(found);
    }

    @Override
    public Evaluator<?> getSubjectEvaluator() {
        return subject;
    }

}
