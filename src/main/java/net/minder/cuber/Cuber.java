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

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Cuber {

  public static void main( String[] args ) throws IOException {

    if( args.length != 2 ) {
      System.err.println( "Usage: java -jar cuber.jar {config.cfg} {box.tsv} ");
      System.exit( 1 );
    }

    // Load the config.
    String configFileName = args[0];
    Config config = Config.load( new File( configFileName ) );

    Box cube = new Box( config.getName() );

    // Load cards from each box into the cube.
    for( int i=1; i<=args.length; i++ ) {
      String boxFileName = args[ 1 ];
      Box box = Box.load( new File( boxFileName ) );
      //System.out.println( box.toString() );
      cube.getCards().addAll( box.selectCards( config.getCardFilter() ) );
      //System.out.println( cube.toString() );
    }

    Draft draft = new Draft( cube.getName() );
    Map<Set<String>,Integer> comp = config.getPackComposition();

    for( int i=1, n=config.getPackCount(); i<=n; i++ ) {
      Pack pack = new Pack( String.format( "%02d", i ) );
      for( Map.Entry<Set<String>,Integer> pair: comp.entrySet() ) {
        Set<String> rarity = pair.getKey();
        int count = pair.getValue();
        // Select a stack of cards by rarity.
        List<Card> cards = cube.selectCardsByRarity( rarity );
        if( cards.size() < count ) {
          throw new IllegalStateException( String.format( "Ran out of %s cards.", StringUtils.join( rarity ) ) );
        }
        // Shuffle the stack.
        Collections.shuffle( cards, config.getRandom() );
        for( int j=0; j<count; j++ ) {
          Card card = cards.get( j );
          cube.getCards().remove( card );
          pack.getCards().add( card );
        }
      }
      draft.getPacks().add( pack );
    }

    System.out.println( "CONFIG" );
    System.out.println( config.toString() );

    System.out.println( "UNBALANCED" );
    Collections.sort( draft.getPacks(), Pack.WEIGHT_COMPARATOR );
    System.out.println( draft.toString() );

    for( int i=0; i<config.getBalanceIterations(); i++ ) {
      Collections.sort( draft.getPacks(), Pack.WEIGHT_COMPARATOR );

      Pack fixPack = draft.getFirstPack();
      Collections.sort( fixPack.getCards(), Card.WEIGHT_COMPARATOR );

      Card fixCard = fixPack.getFirstCard();
      Card replacement = selectReplacement( cube, fixCard, false );
      fixPack.getCards().remove( fixCard );
      cube.getCards().remove( replacement );
      fixPack.getCards().add( replacement );
      cube.getCards().add( fixCard );


      fixPack = draft.getLastPack();
      Collections.sort( fixPack.getCards(), Card.WEIGHT_COMPARATOR );
      fixCard = fixPack.getLastCard();
      replacement = selectReplacement( cube, fixCard, true );
      fixPack.getCards().remove( fixCard );
      cube.getCards().remove( replacement );
      fixPack.getCards().add( replacement );
      cube.getCards().add( fixCard );
    }

    System.out.println( "BALANCED" );
    Collections.sort( draft.getPacks(), Pack.WEIGHT_COMPARATOR );
    System.out.println( draft.toString() );

    System.out.println( "PICKLIST" );
    TreeMap<String,String> index = new TreeMap<String,String>();
    for( Pack pack: draft.getPacks() ) {
      for( Card card: pack.getCards() ) {
        index.put( card.getSetId(), pack.getName() );
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

    replacements = stack.selectCards( String.format( "rarity='%s' and type='%s'", original.rarity, original.type ) );
    replacement = selectReplacement( replacements, original, stronger );

    if( replacement == null ) {
      //System.err.println( "No replacements for " + original + ", relaxing type." );
      replacements = stack.selectCards( String.format( "rarity='%s'", original.rarity ) );
      replacement = selectReplacement( replacements, original, stronger );
    }

    if( replacement == null ) {
      throw new IllegalStateException( "No replacement for " + original );
    }

    return replacement;
  }

}
