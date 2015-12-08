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

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.selection.TruncationSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.uncommons.watchmaker.framework.termination.Stagnation;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import static com.googlecode.cqengine.query.QueryFactory.and;
import static com.googlecode.cqengine.query.QueryFactory.equal;

public class Cuber {

  public static void main( String[] args ) throws IOException {

    if( args.length < 2 ) {
      System.err.println( "Usage: java -jar cuber.jar {config.cfg} {box.tsv} ");
      System.exit( 1 );
    }

    // Load the config.
    String configFileName = args[0];
    Config config = Config.load( new File( configFileName ) );

    System.out.println( "CONFIG" );
    System.out.println( config.toString() );

    Box cube = new Box( config.getName() );

    // Load cards from each box into the cube.
    for( int i=1; i<args.length; i++ ) {
      String boxFileName = args[ i ];
      Box box = Box.load( new File( boxFileName ) );
      //System.out.println( box.toString() );
      cube.addCards( box.getCards( config.getCardFilter() ) );
    }

    System.out.println( cube.toString() );
    System.out.println( "DUP=" + cube.getDuplication() );

    DraftFactory factory = new DraftFactory( cube.getName(), config, cube );
    DraftMutator mutator = new DraftMutator();
    DraftEvaluator evaluator = new DraftEvaluator();
    SelectionStrategy selection = new TruncationSelection(0.99d);
    //SelectionStrategy selection = new RouletteWheelSelection();

    //Draft draft = factory.generateRandomCandidate( config.getRandom() );
    //System.out.println( "UNBALANCED" );
    //Collections.sort( draft.getPacks(), Pack.WEIGHT_COMPARATOR );
    //System.out.println( draft.toString() );

    Random random = new MersenneTwisterRNG();

    EvolutionEngine<Draft> engine = new GenerationalEvolutionEngine<Draft>(
        factory,
        mutator,
        evaluator,
        selection,
        random );

    engine.addEvolutionObserver( new EvolutionObserver<Draft>() {
      public void populationUpdate( PopulationData<? extends Draft> data) {
        System.out.printf("Generation %d: %s\n",
            data.getGenerationNumber(),
            data.getBestCandidate());
      }
    });

    //Draft draft = engine.evolve( 10, 0, new TargetFitness( 11, true ) );
    //Draft draft = engine.evolve( 10, 5, new GenerationCount( config.getBalanceIterations() ) );
    Draft draft = engine.evolve( 100, 0, new Stagnation( config.getBalanceIterations(), false ) );

    //for( int i=0; i<config.getBalanceIterations(); i++ ) {
    //  draft = mutator.apply( draft, config.getRandom() );
    //}

    System.out.println( "EVOLVED" );
    Collections.sort( draft.getPacks(), Pack.WEIGHT_COMPARATOR );
    System.out.println( draft.toDescription() );

    System.out.println( "PICKLIST" );
    TreeMap<String,String> index = new TreeMap<String,String>();
    for( Pack pack: draft.getPacks() ) {
      for( Card card: pack.getCards() ) {
        index.put( card.id, pack.getName() );
      }
    }
    for( Map.Entry<String,String> entry: index.entrySet() ) {
      System.out.println( String.format( "%s -> %s", entry.getKey(), entry.getValue() ) );
    }

  }

  private static Card selectReplacement( List<Card> replacements, Card original, boolean stronger ) {
    double originalWeight = original.getTotalWeight();
    Card replacement = null;
    Collections.shuffle( replacements );
    for( int i=0; i<replacements.size(); i++ ) {
      replacement = replacements.get( i );
      double replacementWeight = replacement.getTotalWeight();
      if( stronger && replacementWeight > originalWeight ) {
        break;
      } else if( !stronger && replacementWeight < originalWeight ) {
        break;
      } else {
        replacement = null;
      }
    }
    return replacement;
  }

  private static Card selectReplacement( Stack stack, Card original, boolean stronger ) {
    List<Card> replacements;
    Card replacement;

    replacements = stack.getCardList( and( equal( Card.RARITY, original.rarity ), equal( Card.TYPE, original.type ) ) );
    replacement = selectReplacement( replacements, original, stronger );

    if( replacement == null ) {
      replacements = stack.getCardList( equal( Card.RARITY, original.rarity ) );
      replacement = selectReplacement( replacements, original, stronger );
    }

    if( replacement == null ) {
      throw new IllegalStateException( "No replacement for " + original );
    }

    return replacement;
  }

}
