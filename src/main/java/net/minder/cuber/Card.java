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

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Card {

  public static Set<String> CREATURE_TYPES = new HashSet( Arrays.asList( "C", "CL", "CA" ) );
  public static Set<String> LAND_TYPES = new HashSet( Arrays.asList( "L", "BL" ) );
  public static Set<String> TOKEN_TYPES = new HashSet( Arrays.asList( "T", "TC", "TCA" ) );

  public String set;
  public String id;
  public String name;
  public String type;
  public String subType;
  public String rarity;
  public String anyManaStr;
  public String whiteManaStr;
  public String blueManaStr;
  public String blackManaStr;
  public String redManaStr;
  public String greenManaStr;
  public String totalManaStr;
  public String offenseStr;
  public String defenseStr;
  public String strengthStr;
  public String source;

  public Card(
      String set,
      String id,
      String name,
      String type,
      String subType,
      String rarity,
      String anyMana,
      String whiteMana,
      String blueMana,
      String blackMana,
      String redMana,
      String greenMana,
      String totalMana,
      String offense,
      String defense,
      String strength,
      String source ) {
    this.set = set;
    this.id = id;
    this.name = name;
    this.type = type;
    this.subType = subType;
    this.rarity = rarity;
    this.anyManaStr = anyMana;
    this.whiteManaStr = whiteMana;
    this.blueManaStr = blueMana;
    this.blackManaStr = blackMana;
    this.redManaStr = redMana;
    this.greenManaStr = greenMana;
    this.totalManaStr = totalMana;
    this.offenseStr = offense;
    this.defenseStr = defense;
    this.strengthStr = strength;
    this.source = source;
  }

  public String getSetId() {
    return set + id;
  }

  public int getAnyMana() {
    return Utils.parseInt( anyManaStr, 0 );
  }

  public int getWhiteMana() {
    return Utils.parseInt( whiteManaStr, 0 );
  }

  public int getBlueMana() {
    return Utils.parseInt( blueManaStr, 0 );
  }

  public int getBlackMana() {
    return Utils.parseInt( blackManaStr, 0 );
  }

  public int getRedMana() {
    return Utils.parseInt( redManaStr, 0 );
  }

  public int getGreenMana() {
    return Utils.parseInt( greenManaStr, 0 );
  }

  public double getTotalMana() {
    return Utils.parseInt( totalManaStr, 0 );
  }

  public int getOffensiveStrength() {
    return Utils.parseInt( offenseStr, 0 );
  }

  public int getDefensiveStrength() {
    return Utils.parseInt( defenseStr, 0 );
  }

  public double getTotalStrength() {
    return Utils.parseInt( strengthStr, 0 );
  }

  public double getTotalWeight() {
    return getTotalMana() + getTotalStrength();
  }

  public String toString() {
    return toString( "," );
  }

  public String toString( String sep ) {
    return String.format(
        "%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s",
        name, sep, anyManaStr, sep, whiteManaStr, sep, blueManaStr, sep, blackManaStr, sep, redManaStr, sep, greenManaStr, sep, totalManaStr, sep,
        type, sep, subType, sep, offenseStr, sep, defenseStr, sep, strengthStr, sep, set, sep, id, sep, rarity, sep, 1, sep, source );
  }

  public int hashCode() {
    return (set+id).hashCode();
  }

  public boolean equals( Object that ) {
    boolean equal = false;
    if( that != null && that instanceof Card ) {
      Card thatCard = (Card)that;
      equal = this.set.equalsIgnoreCase( thatCard.set ) &&
          this.id.equalsIgnoreCase( thatCard.id ) &&
          this.source.equalsIgnoreCase( thatCard.source );
    }
    return equal;
  }

  public static Comparator<Card> SETID_COMPARATOR = new SetIdComparator();
  public static class SetIdComparator implements Comparator<Card> {
    public int compare( Card left, Card right ) {
      return left.getSetId().compareTo( right.getSetId() );
    }
  }

  public static Comparator<Card> WEIGHT_COMPARATOR = new WeightComparator();
  public static class WeightComparator implements Comparator<Card> {
    public int compare( Card left, Card right ) {
      return Double.compare( right.getTotalWeight(), left.getTotalWeight() );
    }
  }

}
