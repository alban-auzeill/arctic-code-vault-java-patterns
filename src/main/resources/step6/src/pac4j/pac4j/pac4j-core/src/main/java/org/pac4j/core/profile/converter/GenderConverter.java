package org.pac4j.core.profile.converter; // (rank 390) copied from https://github.com/pac4j/pac4j/blob/c332d865141e9f3b0c998c2eee76853d6678afcb/pac4j-core/src/main/java/org/pac4j/core/profile/converter/GenderConverter.java

import java.util.regex.Pattern;

import org.pac4j.core.profile.Gender;

/**
 * This class converts a String to a Gender.
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
public final class GenderConverter extends AbstractAttributeConverter {

    private final Pattern maleText;
    private final Pattern femaleText;

    public GenderConverter() {
        super(Gender.class);
        this.maleText = Pattern.compile("(^m$)|(^male$)", Pattern.CASE_INSENSITIVE);
        this.femaleText = Pattern.compile("(^f$)|(^female$)", Pattern.CASE_INSENSITIVE);
    }

    public GenderConverter(final String maleText, final String femaleText) {
        super(Gender.class);
        this.maleText = Pattern.compile(maleText);
        this.femaleText = Pattern.compile(femaleText);
    }

    @Override
    protected Gender internalConvert(final Object attribute) {
        final String s = attribute.toString().toLowerCase();
        if (maleText.matcher(s).matches()) {
            return Gender.MALE;
        } else if (femaleText.matcher(s).matches()) {
            return Gender.FEMALE;
        } else {
            return Gender.UNSPECIFIED;
        }
    }
}
