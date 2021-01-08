/* (rank 266) copied from https://github.com/pockethub/PocketHub/blob/8228cb8f7197a7c2227e67ee48a2c8dc843fb553/app/src/main/java/com/github/pockethub/android/core/gist/GistUriMatcher.java
 * Copyright (c) 2015 PocketHub
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
package com.github.pockethub.android.core.gist;

import android.net.Uri;
import android.text.TextUtils;


import com.meisolsson.githubsdk.model.Gist;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Parses a {@link Gist} from a {@link Uri}
 */
public class GistUriMatcher {

    private static final Pattern PATTERN = Pattern.compile("[a-f0-9]{20}");

    /**
     * Parse a {@link Gist} from a non-null {@link Uri}
     *
     * @param uri
     * @return {@link Gist} or null if none found in given {@link Uri}
     */
    public static Gist getGist(final Uri uri) {
        List<String> segments = uri.getPathSegments();
        if (segments.size() != 1) {
            return null;
        }

        String gistId = segments.get(0);
        if (TextUtils.isEmpty(gistId)) {
            return null;
        }

        Gist gist = Gist.builder()
                .id(gistId)
                .build();

        if (TextUtils.isDigitsOnly(gistId)) {
            return gist;
        }

        if (PATTERN.matcher(gistId).matches()) {
            return gist;
        }

        return null;
    }
}
