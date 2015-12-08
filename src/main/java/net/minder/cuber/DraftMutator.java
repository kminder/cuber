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
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.googlecode.cqengine.query.QueryFactory.and;
import static com.googlecode.cqengine.query.QueryFactory.descending;
import static com.googlecode.cqengine.query.QueryFactory.equal;
import static com.googlecode.cqengine.query.QueryFactory.greaterThan;
import static com.googlecode.cqengine.query.QueryFactory.greaterThanOrEqualTo;
import static com.googlecode.cqengine.query.QueryFactory.lessThan;
import static com.googlecode.cqengine.query.QueryFactory.lessThanOrEqualTo;
import static com.googlecode.cqengine.query.QueryFactory.orderBy;
import static com.googlecode.cqengine.query.QueryFactory.queryOptions;

public class DraftMutator implements EvolutionaryOperator<Draft> {

  private static HashMap<String,Attribute<Card,? extends Comparable<?>>> CARD_ATTRIBUTES =
      new HashMap<String,Attribute<Card,? extends Comparable<?>>>();
  static {
    CARD_ATTRIBUTES.put( Stack.MULTI, Card.MANA_ANY );
    CARD_ATTRIBUTES.put( Stack.WHITE, Card.MANA_WHITE );
    CARD_ATTRIBUTES.put( Stack.BLUE, Card.MANA_BLUE );
    CARD_ATTRIBUTES.put( Stack.BLACK, Card.MANA_BLACK );
    CARD_ATTRIBUTES.put( Stack.RED, Card.MANA_RED );
    CARD_ATTRIBUTES.put( Stack.GREEN, Card.MANA_GREEN );
    CARD_ATTRIBUTES.put( Stack.COST, Card.MANA_TOTAL );

    CARD_ATTRIBUTES.put( Stack.CREATURE, Card.CREATURE );
    CARD_ATTRIBUTES.put( Stack.ARTIFACT, Card.ARTIFACT );
    CARD_ATTRIBUTES.put( Stack.ENCHANTMENT, Card.ENCHANTMENT );
    CARD_ATTRIBUTES.put( Stack.INSTANT, Card.INSTANT );
    CARD_ATTRIBUTES.put( Stack.SORCERY, Card.SORCERY );
    CARD_ATTRIBUTES.put( Stack.LAND, Card.LAND );

    CARD_ATTRIBUTES.put( Stack.COMMON, Card.COMMON );
    CARD_ATTRIBUTES.put( Stack.UNCOMMON, Card.UNCOMMON );
    CARD_ATTRIBUTES.put( Stack.RARE, Card.RARE );
    CARD_ATTRIBUTES.put( Stack.MYTHIC, Card.MYTHIC );

    CARD_ATTRIBUTES.put( Stack.OFFENSE, Card.OFFENSE );
    CARD_ATTRIBUTES.put( Stack.DEFENSE, Card.DEFENSE );
    CARD_ATTRIBUTES.put( Stack.STRENGTH, Card.STRENGTH );

    CARD_ATTRIBUTES.put( Stack.WEIGHT, Card.WEIGHT );
  }


  public List<Draft> apply( List<Draft> input, Random random ) {
    List output = new ArrayList( input.size() );
    for( Draft draft: input ) {
      output.add( apply( draft, random ) );
    }
    return output;
  }

  public Draft apply( Draft draft, Random random ) {
    Metric outlier = draft.getFitness().getOutlierMetric();
    Pack pack = pickOutlierPack( outlier, draft, random );
    if( Stack.METRICS.contains( outlier.getName() ) ) {
      applyGuidedMutation( outlier, draft, pack, random );
    } else {
      applyRandomMutation( draft, pack, random );
    }
    return draft;
  }

  private static Card getRandomCard( List<Card> cards, Random random ) {
    int s = cards.size();
    if( s == 0 ) {
      throw new IllegalStateException( String.format( "Card list is empty." ) );
    }
    int i = random.nextInt( s );
    Card card = cards.get( i );
    return card;
  }

  public Pack pickOutlierPack( Metric outlier, Draft draft, Random random ) {
    Pack pack = null;
    Map<String,Double> data = outlier.getData();
    String name = null;
    Double max = Double.MIN_VALUE;
    for( Map.Entry<String,Double> entry: data.entrySet() ) {
      Double v = entry.getValue();
      if( v.compareTo( max ) > 0 ) {
        name = entry.getKey();
        max = v;
      }
      pack = draft.getPack( name );
    }
    return pack;
  }

  private Card pickOutlierCard( Metric metric, Pack pack, Random random ) {
    String name = metric.getName();
    double draftAvg = metric.getAvg();
    double packAvg = pack.getMetrics().get( name ).getAvg();
    Attribute attr = CARD_ATTRIBUTES.get( name );
    int dir = Double.compare( draftAvg, packAvg );
    Card card;
    if( dir > 0 ) {
      card = pack.getCard( greaterThanOrEqualTo( attr, packAvg ), queryOptions( orderBy( descending( attr ) ) ) );
    } else {
      card = pack.getCard( lessThanOrEqualTo( attr, packAvg ), queryOptions( orderBy( descending( attr ) ) ) );
    }
//    if( card == null ) {
//      card = getRandomCard( pack.getCardList( equal( attr, packAvg ) ), random );
//    }
//    if( card == null ) {
//      card = pack.getRandomCard( random );
//    }
    return card;
  }

  private void swapCards( Stack stackA, Card cardA, Stack stackB, Card cardB ) {
    stackA.delCard( cardA );
    stackB.delCard( cardB );
    stackB.addCard( cardA );
    stackA.addCard( cardB );
  }

  public int compareMetricToCard( Metric metric, Card card ) {
    Attribute<Card,? extends Comparable> a = CARD_ATTRIBUTES.get( metric.getName() );
    Iterable i = a.getValues( card, null );
    Object o = i.iterator().next();
    Double d = (Double)o;
    int c = Double.compare( d.doubleValue(), metric.getAvg().doubleValue() );
    return c;
  }

  public void applyGuidedMutation( Metric outlier, Draft draft, Pack pack, Random random ) {
    Stack box = draft.getBox();
    // Find the card within the pack that is the worst offender of the outlier metric.
    Card packCard = pickOutlierCard( outlier, pack, random );
    //TODO Pick a similar random card with an increased or decreased level of the correct metric.
    // Iterate while relaxing criteria until we find one.
    int dir = compareMetricToCard( outlier, packCard );
    Card boxCard = selectReplacement( box, packCard, -dir, random );
    //Card boxCard = box.getRandomCard( random );
    swapCards( pack, packCard, box, boxCard );
  }

  public void applyRandomMutation( Draft draft, Pack pack, Random random ) {
    Stack box = draft.getBox();
    Card packCard = pack.getRandomCard( random );
    Card boxCard = box.getRandomCard( random );
    swapCards( pack, packCard, box, boxCard );
  }

//  public Draft apply2( Draft draft, Random random ) {
//    Stack box = draft.getBox();
//    Collections.sort( draft.getPacks(), Pack.WEIGHT_COMPARATOR );
//
//    Pack fixPack = draft.getFirstPack();
//    List<Card> fixCards = fixPack.getCardList();
//    Collections.sort( fixCards, Card.WEIGHT_COMPARATOR );
//
//    Card fixCard = fixCards.get( 0 );
//    Card replacement = selectReplacement( box, fixCard, false );
//    fixPack.delCard( fixCard );
//    box.delCard( replacement );
//    fixPack.addCard( replacement );
//    box.addCard( fixCard );
//
//    fixPack = draft.getLastPack();
//    fixCards = fixPack.getCardList();
//    Collections.sort( fixCards, Card.WEIGHT_COMPARATOR );
//    fixCard = fixCards.get( fixCards.size()-1 );
//    replacement = selectReplacement( box, fixCard, true );
//    fixPack.delCard( fixCard );
//    box.delCard( replacement );
//    fixPack.addCard( replacement );
//    box.addCard( fixCard );
//    return draft;
//  }

  private static Card selectReplacement( List<Card> replacements, Card original, int dir, Random random ) {
    Card replacement = null;
    if( replacements != null && !replacements.isEmpty() ) {
      replacement = getRandomCard( replacements, random );
    }
//    Card replacement = null;
//    double originalWeight = original.getTotalWeight();
//    Collections.shuffle( replacements );
//    for( int i=0; i<replacements.size(); i++ ) {
//      replacement = replacements.get( i );
//      double replacementWeight = replacement.getTotalWeight();
//      if( dir > 0 && replacementWeight >= originalWeight ) {
//        break;
//      } else if( dir < 0 && replacementWeight <= originalWeight ) {
//        break;
//      } else {
//        replacement = null;
//      }
//    }
    return replacement;
  }

  private static Card selectReplacement( Stack stack, Card original, int dir, Random random ) {
    List<Card> replacements;
    Card replacement;

    replacements = stack.getCardList( and( equal( Card.RARITY, original.rarity ), equal( Card.TYPE, original.type ) ) );
    replacement = selectReplacement( replacements, original, dir, random );

    if( replacement == null ) {
      replacements = stack.getCardList( equal( Card.RARITY, original.rarity ) );
      replacement = selectReplacement( replacements, original, dir, random );
    }

    if( replacement == null ) {
      System.out.println( "No replacement for " + original );
      replacement = original;
    }

    return replacement;
  }

}
