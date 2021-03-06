/*! ****************************************************************************** (rank 44) copied from https://github.com/pentaho/pentaho-kettle/blob/cfbd5a1a8d024a61efc7a1e147e86429725d076c/core/src/main/java/org/pentaho/di/connections/vfs/VFSLookupFilter.java
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2019 by Hitachi Vantara : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.connections.vfs;

import org.pentaho.di.connections.LookupFilter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bmorrise on 2/12/19.
 */
public class VFSLookupFilter implements LookupFilter {

  private ConcurrentHashMap<String, String> keyLookup = new ConcurrentHashMap<>();

  @Override public String filter( String input ) {
    Pattern pattern = Pattern.compile( "^([\\w]+)://" );
    Matcher matcher = pattern.matcher( input );
    if ( matcher.find() ) {
      input = matcher.group( 1 );
    }
    String lookup = keyLookup.get( input );
    if ( lookup != null ) {
      return lookup;
    }
    return input;
  }

  public void addKeyLookup( String from, String to ) {
    keyLookup.put( from, to );
  }
}
