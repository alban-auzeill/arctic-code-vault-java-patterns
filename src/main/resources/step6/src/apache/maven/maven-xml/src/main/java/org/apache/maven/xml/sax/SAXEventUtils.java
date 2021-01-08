package org.apache.maven.xml.sax; // (rank 225) copied from https://github.com/apache/maven/blob/8eda091d4e2231eee3c33b4e9d417d62971d0762/maven-xml/src/main/java/org/apache/maven/xml/sax/SAXEventUtils.java

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.regex.Pattern;

/**
 * Utility class for SAXEvents
 *
 * @author Robert Scholte
 * @since 4.0.0
 */
public final class SAXEventUtils
{
    private static final Pattern PATTERN = Pattern.compile( "[^:]+$" );

    private SAXEventUtils()
    {
    }

    /**
     * Returns the newLocalName prefixed with the namespace of the oldQName if present
     *
     * @param oldQName the QName, used for its namespace
     * @param newLocalName the preferred localName
     * @return the new QName
     */
    public static String renameQName( String oldQName, String newLocalName )
    {
        return PATTERN.matcher( oldQName ).replaceFirst( newLocalName );
    }
}
