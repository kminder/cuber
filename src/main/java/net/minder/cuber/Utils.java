/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.minder.cuber;

import org.josql.QueryResults;

import java.util.ArrayList;
import java.util.List;

public class Utils {

  static int parseInt( String s, int d ) {
    int i = d;
    try {
      i = Integer.parseInt( s );
    } catch ( NumberFormatException e ) {
      // Ignore it and use the default.
    }
    return i;
  }

  static List column( int index, QueryResults result ) {
    List rows = result.getResults();
    List column = new ArrayList( rows.size() );
    for( Object row: rows ) {
      column.add( ((List)row).get( index ) );
    }
    return column;
  }

}
