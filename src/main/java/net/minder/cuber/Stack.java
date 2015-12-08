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

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Stack {

  public static final String MULTI = "multi";
  public static final String WHITE = "white";
  public static final String BLUE = "blue";
  public static final String BLACK = "black";
  public static final String RED = "red";
  public static final String GREEN = "green";
  public static final String COST = "cost";
  public static final String OFFENSE = "offense";
  public static final String DEFENSE = "defense";
  public static final String STRENGTH = "strength";
  public static final String CREATURE = "creature";
  public static final String SORCERY = "sorcery";
  public static final String ENCHANTMENT = "enchantment";
  public static final String INSTANT = "instant";
  public static final String ARTIFACT = "artifact";
  public static final String LAND = "land";
  public static final String COMMON = "common";
  public static final String UNCOMMON = "uncommon";
  public static final String RARE = "rare";
  public static final String MYTHIC = "mythic";
  public static final String WEIGHT = "weight";

  private static String[] METRIC_NAMES = {
      MULTI, WHITE, BLUE, BLACK, RED, GREEN, COST,
      OFFENSE, DEFENSE, STRENGTH,
      CREATURE, SORCERY, ENCHANTMENT, INSTANT, ARTIFACT, LAND,
      COMMON, UNCOMMON, RARE, MYTHIC,
      WEIGHT };

  public static HashSet<String> METRICS = new HashSet<String>( Arrays.asList( METRIC_NAMES ) );
  public static HashSet<String> TYPE_METRICS = new HashSet<String>( Arrays.asList(
      CREATURE, ARTIFACT, ENCHANTMENT, INSTANT, LAND ) );
  public static HashSet<String> MANA_METRICS = new HashSet<String>( Arrays.asList(
      MULTI, WHITE, BLACK, BLACK, RED, GREEN, COST ) );
  public static HashSet<String> STRENGTH_METRICS = new HashSet<String>( Arrays.asList(
      OFFENSE, DEFENSE, STRENGTH ) );
  public static HashSet<String> RARITY_METRICS = new HashSet<String>( Arrays.asList(
      COMMON, UNCOMMON, RARE, MYTHIC ) );

  private String name;
  private IndexedCollection<Card> cards;
  private MultiValuedMap<String,String> dups;
  private Map<String,Metric> metrics;


  public Stack( String name ) {
    this.name = name;
    this.cards = new ConcurrentIndexedCollection<Card>();
    this.cards.addIndex( NavigableIndex.onAttribute( Card.SET ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.ID ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.CID ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.NUM ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.COPY ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.NAME ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.TYPE ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.SUBTYPE ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.RARITY ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.MANA_ANY ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.MANA_WHITE ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.MANA_BLUE ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.MANA_BLACK ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.MANA_RED ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.MANA_GREEN ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.MANA_TOTAL ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.OFFENSE ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.DEFENSE ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.STRENGTH ));
    this.cards.addIndex( NavigableIndex.onAttribute( Card.SOURCE ));
    this.dups = new ArrayListValuedHashMap<String,String>();
    this.metrics = new HashMap<String,Metric>( 19 );
    for( String metric: METRICS ) {
      this.metrics.put( metric, new Metric( metric ) );
    }
  }

  public String getName() {
    return name;
  }

  public int getCount() {
    return cards.size();
  }

  public Collection<Card> getCards() {
    return cards;
  }

  public List<Card> getCardList() {
    return new ArrayList<Card>( cards );
  }

  public Card getCard( Query query ) {
    Card card = null;
    ResultSet<Card> results = cards.retrieve( query );
    if( !results.isEmpty() ) {
      if( results.size() == 1 ) {
        card = results.uniqueResult();
      } else {
        card = results.iterator().next();
      }
    }
    results.close();
    return card;
  }

  public Card getCard( Query query, QueryOptions options ) {
    Card card = null;
    ResultSet<Card> results = cards.retrieve( query, options );
    if( !results.isEmpty() ) {
      if( results.size() == 1 ) {
        card = results.uniqueResult();
      } else {
        card = results.iterator().next();
      }
    }
    results.close();
    return card;
  }

  public Card getRandomCard( Random random ) {
    if( cards.isEmpty() ) {
      throw new IllegalStateException( String.format( "Stack %s is empty.", getName() ) );
    }
    List<Card> list = getCardList();
    int i = random.nextInt( list.size() );
    Card card = list.get( i );
    return card;
  }

  public ResultSet<Card> getCards( Query query ) {
    return cards.retrieve( query );
  }

  public List<Card> getCardList( Query query ) {
    ResultSet<Card> results = getCards( query );
    List<Card> list = IteratorUtils.toList( results.iterator(), cards.size() );
    return list;
  }

  public ResultSet<Card> getCards( Query query, QueryOptions options ) {
    return cards.retrieve( query, options );
  }

  public List<Card> getCardList( Query query, QueryOptions options ) {
    ResultSet<Card> results = getCards( query, options );
    List<Card> list = IteratorUtils.toList( results.iterator(), cards.size() );
    return list;
  }

  public Map<String,Metric> getMetrics() {
    return metrics;
  }

  public void addCard( Card card ) {
    String id = card.id;
    cards.add( card );
    dups.put( card.cid, id );
    metrics.get( MULTI ).add( id, card.getAnyMana() );
    metrics.get( WHITE ).add( id, card.getWhiteMana() );
    metrics.get( BLUE ).add( id, card.getBlueMana() );
    metrics.get( BLACK ).add( id, card.getBlackMana() );
    metrics.get( RED ).add( id, card.getRedMana() );
    metrics.get( GREEN ).add( id, card.getGreenMana() );
    metrics.get( COST ).add( id, card.getTotalMana() );
    metrics.get( OFFENSE ).add( id, card.getOffensiveStrength() );
    metrics.get( DEFENSE ).add( id, card.getDefensiveStrength() );
    metrics.get( STRENGTH ).add( id, card.getTotalStrength() );
    metrics.get( CREATURE ).add( id, card.getCreature() );
    metrics.get( SORCERY ).add( id, card.getSourcery() );
    metrics.get( ENCHANTMENT ).add( id, card.getEnchantment() );
    metrics.get( INSTANT ).add( id, card.getInstant() );
    metrics.get( ARTIFACT ).add( id, card.getArtifact() );
    metrics.get( LAND ).add( id, card.getLand() );
    metrics.get( COMMON ).add( id, card.getCommon() );
    metrics.get( UNCOMMON ).add( id, card.getUncommon() );
    metrics.get( RARE ).add( id, card.getRare() );
    metrics.get( MYTHIC ).add( id, card.getMythic() );
    metrics.get( WEIGHT ).add( id, card.getTotalWeight() );
  }

  public void addCards( Iterable<Card> cards ) {
    for( Card card: cards ) {
      addCard( card );
    }
  }

  public void delCard( Card card ) {
    cards.remove( card );
    dups.removeMapping( card.cid, card.id );
    for( String metric : METRICS ) {
      metrics.get( metric ).del( card.id );
    }
  }

  public int getDuplication() {
    int d = 0;
    for( String key: dups.keys() ) {
      int s = dups.get( key ).size();
      if( s > 1 ) {
        d += ( s-1 );
      }
    }
    return d;
  }

  public String toString() {
    return String.format(
        "Stack[%s]: T=%2d, C=%.0f, U=%.0f, R=%.0f, c=%2.0f, i=%.0f, e=%.0f, a=%.0f, l=%.0f, M=%.0f/%.0f/%1.1f (%2.0f/%2.0f/%2.0f/%2.0f/%2.0f) S=%2.0f/%2.0f/%1.1f(%2.0f/%2.0f), W=%3.0f",
        getName(), cards.size(),
        metrics.get(COMMON).getSum(), metrics.get(UNCOMMON).getSum(), metrics.get(RARE).getSum(),
        metrics.get(CREATURE).getSum(), metrics.get(INSTANT).getSum(), metrics.get(ENCHANTMENT).getSum(), metrics.get(ARTIFACT).getSum(), metrics.get(LAND).getSum(),
        metrics.get(COST).getSum(), metrics.get(COST).getMax(), metrics.get(COST).getAvg(),
        metrics.get(WHITE).getSum(), metrics.get(BLUE).getSum(), metrics.get(BLACK).getSum(), metrics.get(RED).getSum(), metrics.get(GREEN).getSum(),
        metrics.get(STRENGTH).getSum(), metrics.get(STRENGTH).getMax(), metrics.get(STRENGTH).getAvg(),
        metrics.get(OFFENSE).getSum(), metrics.get(DEFENSE).getSum(), metrics.get(WEIGHT).getSum() );
  }

  public Stack copy() {
    Stack s = new Stack( getName() );
    s.addCards( this.getCards() );
    return s;
  }
}
