/** (rank 593) copied from https://github.com/bastillion-io/Bastillion/blob/faf2273f3bdd0dfc68808d81df1c16ee06ac983d/src/main/java/io/bastillion/manage/util/PasswordUtil.java
 *    Copyright (C) 2015 Loophole, LLC
 *
 *    This program is free software: you can redistribute it and/or  modify
 *    it under the terms of the GNU Affero General Public License, version 3,
 *    as published by the Free Software Foundation.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *    As a special exception, the copyright holders give permission to link the
 *    code of portions of this program with the OpenSSL library under certain
 *    conditions as described in each individual source file and distribute
 *    linked combinations including the program with the OpenSSL library. You
 *    must comply with the GNU Affero General Public License in all respects for
 *    all of the code used other than as permitted herein. If you modify file(s)
 *    with this exception, you may extend this exception to your version of the
 *    file(s), but you are not obligated to do so. If you do not wish to do so,
 *    delete this exception statement from your version. If you delete this
 *    exception statement from all source files in the program, then also delete
 *    it in the license file.
 */
package io.bastillion.manage.util;

import io.bastillion.common.util.AppConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility to validate password strength
 */
public class PasswordUtil {



        public static final String PASSWORD_REGEX= AppConfig.getProperty("passwordComplexityRegEx");
        public static final String PASSWORD_REQ_ERROR_MSG=AppConfig.getProperty("passwordComplexityMsg");

        private static Pattern pattern = Pattern.compile(PASSWORD_REGEX);

    private PasswordUtil() {
    }

    /**
         * Validation to ensure strong password
         *
         * @param password password 
         * @return true if strong password
         */
        public static boolean isValid(final String password){

            Matcher matcher = pattern.matcher(password);
            
            return matcher.matches();

        }
}
