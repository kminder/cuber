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

import org.apache.commons.lang3.StringUtils;
import org.josql.QueryResults;

import java.util.Comparator;

public class Pack extends Stack {

  public Pack( String name ) {
    super( name );
  }

  public String toString() {
    String ids = StringUtils.join( query( String.format( "select setId from %s order by setId", Card.class.getName() ) ).getResults(), "," );
    return String.format(
        "Pack[%s]: T=%d, C=%d, U=%d, R=%d, M=%d/%d/%1.1f, S=%d/%d/%1.1f, W=%d: %s",
        getName(), getCards().size(), countCommon(), countUncommon(), countRare(), totalMana(), maxMana(), avgMana(), totalStrength(), maxStrength(), avgStrength(), totalWeight(), ids );
  }

  public int countCommon() {
    return selectCards( "rarity = 'C'" ).size();
  }

  public int countUncommon() {
    return selectCards( "rarity = 'U'" ).size();
  }

  public int countRare() {
    return selectCards( "rarity in ('R','M')" ).size();
  }

  public int totalMana() {
    QueryResults r = query( String.format( "select sum(:_allobjs,totalMana,'sum') from %s", Card.class.getName() ) );
    Double i = (Double)r.getSaveValue( "sum" );
    return i.intValue();
  }

  public int maxMana() {
    QueryResults r = query( String.format( "select max(:_allobjs,totalMana,'val') from %s", Card.class.getName() ) );
    Double i = (Double)r.getSaveValue( "val" );
    return i.intValue();
  }

  public double avgMana() {
    QueryResults r = query( String.format( "select avg(:_allobjs,totalMana,'avg') from %s", Card.class.getName() ) );
    Double i = (Double)r.getSaveValue( "avg" );
    return i.doubleValue();
  }

  public int totalStrength() {
    QueryResults r = query( String.format( "select sum(:_allobjs,totalStrength,'sum') from %s", Card.class.getName() ) );
    Double i = (Double)r.getSaveValue( "sum" );
    return i.intValue();
  }

  public int maxStrength() {
    QueryResults r = query( String.format( "select max(:_allobjs,totalStrength,'max') from %s", Card.class.getName() ) );
    Double i = (Double)r.getSaveValue( "max" );
    return i.intValue();
  }

  public double avgStrength() {
    QueryResults r = query( String.format( "select avg(:_allobjs,totalStrength,'avg') from %s", Card.class.getName() ) );
    Double i = (Double)r.getSaveValue( "avg" );
    return i.doubleValue();
  }

  public int totalWeight() {
    return totalMana() + totalStrength();
  }

  public static Comparator<Pack> NAME_COMPARATOR = new WeightComparator();
  public static class NameComparator implements Comparator<Pack> {
    public int compare( Pack left, Pack right ) {
      return left.getName().compareTo( right.getName() );
    }
  }

  public static Comparator<Pack> WEIGHT_COMPARATOR = new WeightComparator();
  public static class WeightComparator implements Comparator<Pack> {
    public int compare( Pack left, Pack right ) {
      return Integer.compare( right.totalWeight(), left.totalWeight() );
    }
  }

}
