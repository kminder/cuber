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

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.stat.descriptive.AggregateSummaryStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.StatisticalSummaryValues;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static com.googlecode.cqengine.query.QueryFactory.in;

public class Draft {

  public static final String PACK_DUP = "pack-dupl";
  public static final String DRAFT_DUP = "draft-dupl";

  public static String[] METRIC_NAMES = {
      Stack.MULTI, Stack.WHITE, Stack.BLUE, Stack.BLACK, Stack.RED, Stack.GREEN, Stack.COST,
      Stack.OFFENSE, Stack.DEFENSE, Stack.STRENGTH,
      Stack.CREATURE, Stack.SORCERY, Stack.ENCHANTMENT, Stack.INSTANT, Stack.ARTIFACT, Stack.LAND,
      Stack.COMMON, Stack.UNCOMMON, Stack.RARE, Stack.MYTHIC,
      Stack.WEIGHT,
      PACK_DUP, DRAFT_DUP
      };

  private String name;
  private Config config;
  private Stack box;
  private Random rng;
  private Map<String,Pack> packs;
  private DraftFitness fitness;

  public Draft( String name, Config config, Stack box, Random rng ) {
    this.name = name;
    this.config = config;
    this.box = box;
    this.rng = rng;
    this.packs = new HashMap<String,Pack>();
    this.fitness = null;
    init();
  }

  private void init() {
    Map<Set<String>,Integer> comp = config.getPackComposition();

    for( int i=1, n=config.getPackCount(); i<=n; i++ ) {
      Pack pack = new Pack( String.format( "%02d", i ) );
      for( Map.Entry<Set<String>,Integer> pair: comp.entrySet() ) {
        Set<String> rarity = pair.getKey();
        int count = pair.getValue();
        // Select a stack of cards by rarity.
        List<Card> cards = box.getCardList( in( Card.RARITY, rarity ) );
        if( cards.size() < count ) {
          throw new IllegalStateException( String.format( "Ran out of %s cards.", StringUtils.join( rarity ) ) );
        }
        Collections.shuffle( cards, config.getRandom() );
        // Shuffle the stack.
        for( int j=0; j<count; j++ ) {
          Card card = cards.get( j );
          box.delCard( card );
          pack.addCard( card );
        }
      }
      packs.put( pack.getName(), pack );
    }
  }

  public String getName() {
    return name;
  }

  public Config getConfig() {
    return config;
  }

  public Stack getBox() {
    return box;
  }

  public DraftFitness getFitness() {
    return fitness;
  }

  public void setFitness( DraftFitness fitness ) {
    this.fitness = fitness;
  }

  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append( String.format( "Draft[%s]@%s", name, fitness == null ? "" : fitness.toString() ) );
    return s.toString();
  }

  public String toDescription() {
    StringBuilder s = new StringBuilder();
    s.append( String.format( "Draft[%s]@%f", name, fitness.getAggregate() ) );
    s.append( System.lineSeparator() );
    for( Pack pack: packs.values() ) {
      s.append( "\t" );
      s.append( pack.toString() );
      s.append( System.lineSeparator() );
      for( Card card: pack.getCards() ) {
        s.append( "\t\t" );
        s.append( card.toString() );
        s.append( System.lineSeparator() );
      }
    }
    return s.toString();
  }

  public Pack getPack( String name ) {
    return packs.get( name );
  }

  public List<Pack> getPacks() {
    return new ArrayList( packs.values() );
  }

  public double getDraftDuplication() {
    return 0;
  }

  public double getPackDuplication() {
    double d = 0.0d;
    double t = 0.0d;
    for( Pack pack: packs.values() ) {
      d += pack.getDuplication();
      t += pack.getCount();
    }
    return d/t;
  }

  public Metric getMetricTotal( String name ) {
    Metric draftMetric = new Metric( name );
    for( Pack pack: packs.values() ) {
      final Metric packMetric = pack.getMetrics().get( name );
      draftMetric.add( pack.getName(), packMetric.getSum() );
    }
    return draftMetric;
  }

}
