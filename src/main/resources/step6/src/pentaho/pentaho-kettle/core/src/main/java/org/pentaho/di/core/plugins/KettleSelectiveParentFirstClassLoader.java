/*! ****************************************************************************** (rank 44) copied from https://github.com/pentaho/pentaho-kettle/blob/cfbd5a1a8d024a61efc7a1e147e86429725d076c/core/src/main/java/org/pentaho/di/core/plugins/KettleSelectiveParentFirstClassLoader.java
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Hitachi Vantara : http://www.pentaho.com
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

package org.pentaho.di.core.plugins;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class KettleSelectiveParentFirstClassLoader extends KettleURLClassLoader {
  private List<Pattern> patterns = new ArrayList<>();

  public KettleSelectiveParentFirstClassLoader( URL[] url, ClassLoader classLoader, String[] patterns ) {
    super( url, classLoader );
    addPatterns( patterns );
  }

  public KettleSelectiveParentFirstClassLoader( URL[] url, ClassLoader classLoader, String name, String[] patterns ) {
    super( url, classLoader, name );
    addPatterns( patterns );
  }

  public void addPatterns( String[] patterns ) {
    if ( patterns != null ) {
      this.patterns.addAll( Arrays.stream( patterns )
        .map( Pattern::compile )
        .collect( Collectors.toList() )
      );
    }
  }

  private Class<?> loadClassParentFirst( String arg0, boolean arg1 ) throws ClassNotFoundException {
    try {
      return loadClassFromParent( arg0, arg1 );
    } catch ( ClassNotFoundException | NoClassDefFoundError e ) {
      // ignore
    }

    return loadClassFromThisLoader( arg0, arg1 );
  }

  @Override
  protected synchronized Class<?> loadClass( String arg0, boolean arg1 ) throws ClassNotFoundException {
    for ( Pattern pattern : patterns ) {
      if ( pattern.matcher( arg0 ).matches() ) {
        return loadClassParentFirst( arg0, arg1 );
      }
    }
    return super.loadClass( arg0, arg1 );
  }
}
