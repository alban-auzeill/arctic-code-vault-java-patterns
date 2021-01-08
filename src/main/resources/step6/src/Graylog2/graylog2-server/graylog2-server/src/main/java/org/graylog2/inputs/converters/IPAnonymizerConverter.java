/* (rank 208) copied from https://github.com/Graylog2/graylog2-server/blob/f35df42e165ac570b8b27de3f8eeac85e74ed610/graylog2-server/src/main/java/org/graylog2/inputs/converters/IPAnonymizerConverter.java
 * Copyright (C) 2020 Graylog, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by MongoDB, Inc.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Server Side Public License for more details.
 *
 * You should have received a copy of the Server Side Public License
 * along with this program. If not, see
 * <http://www.mongodb.com/licensing/server-side-public-license>.
 */
package org.graylog2.inputs.converters;

import org.graylog2.plugin.inputs.Converter;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Lennart Koopmann <lennart@torch.sh>
 */
public class IPAnonymizerConverter extends Converter {

    public static final String REPLACEMENT = "$1.$2.$3.xxx";
    public static final Pattern p = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");

    public IPAnonymizerConverter(Map<String, Object> config) {
        super(Type.IP_ANONYMIZER, config);
    }

    @Override
    public Object convert(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        return p.matcher(value).replaceAll(REPLACEMENT);
    }

    @Override
    public boolean buildsMultipleFields() {
        return false;
    }

}
