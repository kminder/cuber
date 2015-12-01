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

import java.util.ArrayList;
import java.util.List;

public class Draft {

  private String name;
  private List<Pack> packs;

  public Draft( String name ) {
    this.name = name;
    this.packs = new ArrayList();
  }

  public List<Pack> getPacks() {
    return packs;
  }

  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append( String.format( "Draft[%s]", name ) );
    s.append( System.lineSeparator() );
    for( Pack pack: packs ) {
      s.append( pack.toString() );
      s.append( System.lineSeparator() );
    }
    return s.toString();
  }

  public Pack getFirstPack() {
    return packs.get( 0 );
  }

  public Pack getLastPack() {
    return packs.get( packs.size()-1 );
  }

}
