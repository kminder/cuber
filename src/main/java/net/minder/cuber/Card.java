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

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Card {

  public static final Attribute<Card, String> ID = new SimpleAttribute<Card, String>("id") {
    public String getValue( Card card, QueryOptions queryOptions ) { return card.id; }
  };

  public static final Attribute<Card, String> CID = new SimpleAttribute<Card, String>("cid") {
    public String getValue( Card card, QueryOptions queryOptions ) { return card.cid; }
  };

  public static final Attribute<Card, String> SET = new SimpleAttribute<Card, String>("set") {
    public String getValue( Card card, QueryOptions queryOptions ) { return card.set; }
  };

  public static final Attribute<Card, String> NUM = new SimpleAttribute<Card, String>("num") {
    public String getValue( Card card, QueryOptions queryOptions ) { return card.card; }
  };

  public static final Attribute<Card, String> COPY = new SimpleAttribute<Card, String>("copy") {
    public String getValue( Card card, QueryOptions queryOptions ) { return card.copy; }
  };

  public static final Attribute<Card, String> NAME = new SimpleAttribute<Card, String>("name") {
    public String getValue( Card card, QueryOptions queryOptions ) { return card.name; }
  };

  public static final Attribute<Card, String> TYPE = new SimpleAttribute<Card, String>("type") {
    public String getValue( Card card, QueryOptions queryOptions ) { return card.type; }
  };

  public static final Attribute<Card, Double> CREATURE = new SimpleAttribute<Card, Double>(Stack.CREATURE) {
    public Double getValue( Card card, QueryOptions queryOptions ) { return card.getCreature(); }
  };

  public static final Attribute<Card, Double> ARTIFACT = new SimpleAttribute<Card, Double>(Stack.ARTIFACT) {
    public Double getValue( Card card, QueryOptions queryOptions ) {
      return card.getArtifact();
    }
  };

  public static final Attribute<Card, Double> ENCHANTMENT = new SimpleAttribute<Card, Double>(Stack.ENCHANTMENT) {
    public Double getValue( Card card, QueryOptions queryOptions ) {
      return card.getEnchantment();
    }
  };

  public static final Attribute<Card, Double> INSTANT = new SimpleAttribute<Card, Double>(Stack.INSTANT) {
    public Double getValue( Card card, QueryOptions queryOptions ) {
      return card.getInstant();
    }
  };

  public static final Attribute<Card, Double> SORCERY = new SimpleAttribute<Card, Double>(Stack.SORCERY) {
    public Double getValue( Card card, QueryOptions queryOptions ) {
      return card.getSourcery();
    }
  };

  public static final Attribute<Card, Double> LAND = new SimpleAttribute<Card, Double>(Stack.LAND) {
    public Double getValue( Card card, QueryOptions queryOptions ) {
      return card.getLand();
    }
  };

  public static final Attribute<Card, String> SUBTYPE = new SimpleAttribute<Card, String>("subtype") {
    public String getValue( Card card, QueryOptions queryOptions ) { return card.subType; }
  };

  public static final Attribute<Card, String> RARITY = new SimpleAttribute<Card, String>("rarity") {
    public String getValue( Card card, QueryOptions queryOptions ) { return card.rarity; }
  };

  public static final Attribute<Card, Double> COMMON = new SimpleAttribute<Card, Double>(Stack.COMMON) {
    public Double getValue( Card card, QueryOptions queryOptions ) {
      return card.getCommon();
    }
  };

  public static final Attribute<Card, Double> UNCOMMON = new SimpleAttribute<Card, Double>(Stack.UNCOMMON) {
    public Double getValue( Card card, QueryOptions queryOptions ) {
      return card.getUncommon();
    }
  };

  public static final Attribute<Card, Double> RARE = new SimpleAttribute<Card, Double>(Stack.RARE) {
    public Double getValue( Card card, QueryOptions queryOptions ) {
      return card.getRare();
    }
  };

  public static final Attribute<Card, Double> MYTHIC = new SimpleAttribute<Card, Double>(Stack.MYTHIC) {
    public Double getValue( Card card, QueryOptions queryOptions ) {
      return card.getMythic();
    }
  };

  public static final Attribute<Card, Double> MANA_ANY = new SimpleAttribute<Card, Double>("manaAny") {
    public Double getValue( Card card, QueryOptions queryOptions ) { return card.getAnyMana(); }
  };

  public static final Attribute<Card, Double> MANA_WHITE = new SimpleAttribute<Card, Double>("manaWhite") {
    public Double getValue( Card card, QueryOptions queryOptions ) { return card.getWhiteMana(); }
  };

  public static final Attribute<Card, Double> MANA_BLUE = new SimpleAttribute<Card, Double>("manaBlue") {
    public Double getValue( Card card, QueryOptions queryOptions ) { return card.getBlueMana(); }
  };

  public static final Attribute<Card, Double> MANA_BLACK = new SimpleAttribute<Card, Double>("manaBlack") {
    public Double getValue( Card card, QueryOptions queryOptions ) { return card.getBlackMana(); }
  };

  public static final Attribute<Card, Double> MANA_RED = new SimpleAttribute<Card, Double>("manaRed") {
    public Double getValue( Card card, QueryOptions queryOptions ) { return card.getRedMana(); }
  };

  public static final Attribute<Card, Double> MANA_GREEN = new SimpleAttribute<Card, Double>("manaGreen") {
    public Double getValue( Card card, QueryOptions queryOptions ) { return card.getGreenMana(); }
  };

  public static final Attribute<Card, Double> MANA_TOTAL = new SimpleAttribute<Card, Double>("manaTotal") {
    public Double getValue( Card card, QueryOptions queryOptions ) { return card.getTotalMana(); }
  };

  public static final Attribute<Card, Double> OFFENSE = new SimpleAttribute<Card, Double>("offense") {
    public Double getValue( Card card, QueryOptions queryOptions ) { return card.getOffensiveStrength(); }
  };

  public static final Attribute<Card, Double> DEFENSE = new SimpleAttribute<Card, Double>("defense") {
    public Double getValue( Card card, QueryOptions queryOptions ) { return card.getDefensiveStrength(); }
  };

  public static final Attribute<Card, Double> STRENGTH = new SimpleAttribute<Card, Double>("strength") {
    public Double getValue( Card card, QueryOptions queryOptions ) { return card.getTotalStrength(); }
  };

  public static final Attribute<Card, String> SOURCE = new SimpleAttribute<Card, String>("source") {
    public String getValue( Card card, QueryOptions queryOptions ) { return card.source; }
  };

  public static final Attribute<Card, Double> WEIGHT = new SimpleAttribute<Card, Double>("weight") {
    public Double getValue( Card card, QueryOptions queryOptions ) { return card.getTotalWeight(); }
  };

  public static final String COMMON_RARITY = "C";
  public static final String UNCOMMON_RARITY = "U";
  public static final String RARE_RARITY = "R";
  public static final String MYTHIC_RARITY = "M";

  public static Set<String> CREATURE_TYPES = new HashSet( Arrays.asList( "C", "CL", "CA" ) );
  public static Set<String> SORCERY_TYPES = new HashSet( Arrays.asList( "S" ) );
  public static Set<String> ENCHANTMENT_TYPES = new HashSet( Arrays.asList( "E" ) );
  public static Set<String> INSTANT_TYPES = new HashSet( Arrays.asList( "I" ) );
  public static Set<String> ARTIFACT_TYPES = new HashSet( Arrays.asList( "A" ) );
  public static Set<String> LAND_TYPES = new HashSet( Arrays.asList( "L", "BL" ) );
  public static Set<String> TOKEN_TYPES = new HashSet( Arrays.asList( "T", "TC", "TCA", "TC" ) );

  public String id;
  public String cid;
  public String set;
  public String card;
  public String copy;
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
      String card,
      String copy,
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
    this.cid = set + "-" + card;
    this.id = this.cid + "~" + copy;
    this.set = set;
    this.card = card;
    this.copy = copy;
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

  public double getCreature() {
    return CREATURE_TYPES.contains( type ) ? Metric.one : Metric.zero;
  }

  public double getArtifact() {
    return ARTIFACT_TYPES.contains( type ) ? Metric.one : Metric.zero;
  }

  public double getEnchantment() {
    return ENCHANTMENT_TYPES.contains( type ) ? Metric.one : Metric.zero;
  }

  public double getInstant() {
    return INSTANT_TYPES.contains( type ) ? Metric.one : Metric.zero;
  }

  public double getSourcery() {
    return SORCERY_TYPES.contains( type ) ? Metric.one : Metric.zero;
  }

  public double getLand() {
    return LAND_TYPES.contains( type ) ? Metric.one : Metric.zero;
  }

  public double getAnyMana() {
    return Utils.parseDbl( anyManaStr, Metric.zero );
  }

  public double getWhiteMana() {
    return Utils.parseDbl( whiteManaStr, Metric.zero );
  }

  public double getBlueMana() {
    return Utils.parseDbl( blueManaStr, Metric.zero );
  }

  public double getBlackMana() {
    return Utils.parseDbl( blackManaStr, Metric.zero );
  }

  public double getRedMana() {
    return Utils.parseDbl( redManaStr, Metric.zero );
  }

  public double getGreenMana() {
    return Utils.parseDbl( greenManaStr, Metric.zero );
  }

  public double getTotalMana() {
    return Utils.parseDbl( totalManaStr, Metric.zero );
  }

  public double getOffensiveStrength() {
    return Utils.parseDbl( offenseStr, Metric.zero );
  }

  public double getDefensiveStrength() {
    return Utils.parseDbl( defenseStr, Metric.zero );
  }

  public double getTotalStrength() {
    return Utils.parseDbl( strengthStr, Metric.zero );
  }

  public double getTotalWeight() {
    return getTotalMana() + getTotalStrength();
  }

  public double getCommon() {
    return Card.COMMON_RARITY.equals( rarity ) ? Metric.one : Metric.zero;
  }

  public double getUncommon() {
    return Card.UNCOMMON_RARITY.equals( rarity ) ? Metric.one : Metric.zero;
  }

  public double getRare() {
    return Card.RARE_RARITY.equals( rarity ) ? Metric.one : Metric.zero;
  }

  public double getMythic() {
    return Card.MYTHIC_RARITY.equals( rarity ) ? Metric.one : Metric.zero;
  }

  public String toString() {
    return toString( "," );
  }

  public String toString( String sep ) {
    return String.format(
        "%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s",
        set, sep, card, sep, copy, sep, name, sep, rarity, sep, type, sep, subType, sep,
        anyManaStr, sep, whiteManaStr, sep, blueManaStr, sep, blackManaStr, sep, redManaStr, sep, greenManaStr, sep, totalManaStr, sep,
        offenseStr, sep, defenseStr, sep, strengthStr, sep, source );
  }

  public int hashCode() {
    return id.hashCode();
  }

  public boolean equals( Object that ) {
    boolean equal = false;
    if( that != null && that instanceof Card ) {
      equal = this.id.equals( ((Card)that).id );
    }
    return equal;
  }

  //public static Comparator<Card> ID_COMPARATOR = new SetIdComparator();

  //public static class SetIdComparator implements Comparator<Card> {
  //  public int compare( Card left, Card right ) {
  //    return left.id.compareTo( right.id );
  //  }
  //}

  //public static Comparator<Card> WEIGHT_COMPARATOR = new WeightComparator();
  //public static class WeightComparator implements Comparator<Card> {
  //  public int compare( Card left, Card right ) {
  //    return Double.compare( right.getTotalWeight(), left.getTotalWeight() );
  //  }
  //}

}
