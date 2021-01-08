// Copyright 2017 Yahoo Holdings. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root. (rank 298) copied from https://github.com/vespa-engine/vespa/blob/f8dfd639931905a9d6dae95f5b4e0ae812042117/controller-api/src/main/java/com/yahoo/vespa/hosted/controller/api/identifiers/PropertyId.java
package com.yahoo.vespa.hosted.controller.api.identifiers;

import java.util.regex.Pattern;

/**
 * A business property ID.
 *
 * @author frodelu
 */
public class PropertyId extends Identifier {

    private static final Pattern PATTERN = Pattern.compile("\\d+");

    public PropertyId(String id) {
        super(id);
    }

    /** Returns this id as a long */
    public long value() { return Long.parseLong(id()); }
    
    @Override
    public void validate() {
        super.validate();
        if(!PATTERN.matcher(id()).matches()) {
            throwInvalidId(id(), "Property id must match pattern: " + PATTERN);
        }
    }
}
