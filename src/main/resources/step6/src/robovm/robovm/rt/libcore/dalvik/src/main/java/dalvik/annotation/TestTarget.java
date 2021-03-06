/* (rank 229) copied from https://github.com/robovm/robovm/blob/ef091902377c00dc0fb2db87e8d79c8afb5e9010/rt/libcore/dalvik/src/main/java/dalvik/annotation/TestTarget.java
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dalvik.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines an annotation used be used within the TestInfo annotation. It
 * specifies a single method target for the test (but can be used multiple
 * times).
 *
 * @deprecated Obsolete.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE })
@Deprecated
public @interface TestTarget {

    /**
     * Specifies the name of the method that is being tested.
     */
    String methodName() default "";

    /**
     * Specifies the name of a concept being tested. Use this if
     * {@code methodName} is not accurate enough. E.g. for
     * {@link java.util.regex.Pattern#compile(String)} {@code methodName} is not
     * sufficient since the String contains a pattern with its own syntax which
     * has to be tested with different aspects. Areas concerned are e.g. JDBC
     * (SELECT, INSERT, UPDATE, DELETE, ...), regex (character sets,
     * operators,...), formatters (DecimalFormat, DateFormat, ChoiceFormat,
     * ...), ...
     */
    String conceptName() default "";

    /**
     * Specifies the signature of the method that is being tested, in terms of
     * Java classes.
     */
    Class<?>[] methodArgs() default {};

}
