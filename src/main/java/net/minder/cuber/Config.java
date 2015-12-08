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

import com.googlecode.cqengine.query.Query;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import static com.googlecode.cqengine.query.QueryFactory.and;
import static com.googlecode.cqengine.query.QueryFactory.equal;
import static com.googlecode.cqengine.query.QueryFactory.in;

public class Config extends Properties {

  public static final String NAME_NAME = "name";
  public static final String SETS_NAME = "sets";
  public static final String SETS_DEFAULT = "ORI";
  public static final String TYPES_NAME = "types";
  public static final String TYPES_DEFAULT = "C,CL,CA,I,E,S,A,L";
  public static final String PLAYERS_NAME = "players";
  public static final String PLAYERS_DEFAULT = "8";
  public static final String ROUNDS_NAME = "rounds";
  public static final String ROUNDS_DEFAULT = "3";
  public static final String PACK_NAME = "pack";
  public static final String PACK_DEFAULT = "C:10,U:3,R+M:1";

  private static Properties DEFAULTS = new Properties();
  static {
    DEFAULTS.setProperty( SETS_NAME, SETS_DEFAULT );
    DEFAULTS.setProperty( TYPES_NAME, TYPES_DEFAULT );
    DEFAULTS.setProperty( PLAYERS_NAME, PLAYERS_DEFAULT );
    DEFAULTS.setProperty( ROUNDS_NAME, ROUNDS_DEFAULT );
    DEFAULTS.setProperty( PACK_NAME, PACK_DEFAULT );
  }

  private Config( Properties defaults ) {
    super( defaults );
  }

  public static Config load( File propFile ) throws IOException {
    Config config = new Config( DEFAULTS );
    config.setProperty( NAME_NAME, FilenameUtils.getBaseName( propFile.getName() ) );
    FileReader reader = new FileReader( propFile );
    config.load( reader );
    reader.close();
    return config;
  }

  private Set<String> sets = null;
  public Set<String> getSets() {
    if( sets == null ) {
      sets = new HashSet( Arrays.asList( getProperty( SETS_NAME ).split( "," ) ) );
    }
    return sets;
  }

  private Set<String> types = null;
  public Set<String> getTypes() {
    if( types == null ) {
      types = new HashSet( Arrays.asList( getProperty( "types" ).split( "," ) ) );
    }
    return types;
  }

  private Query cardFilter = null;
  public Query getCardFilter() {
    if( cardFilter == null ) {
      cardFilter = and( in( Card.SET, getSets() ), Utils.in( Card.TYPE, getTypes() ) );
    }
    return cardFilter;
  }

  public String getName() {
    return getProperty( NAME_NAME );
  }

  private Integer players = null;
  public int getPlayers() {
    if( players == null ) {
      players = Utils.parseInt( getProperty( PLAYERS_NAME, PLAYERS_DEFAULT ), Integer.parseInt( PLAYERS_DEFAULT ) );
    }
    return players.intValue();
  }

  private Integer rounds = null;
  public int getRounds() {
    if( rounds == null ) {
      rounds = Utils.parseInt( getProperty( ROUNDS_NAME, ROUNDS_DEFAULT ), Integer.parseInt( ROUNDS_DEFAULT ) );
    }
    return rounds.intValue();
  }

  private Integer balance = null;
  public int getBalanceIterations() {
    if( balance == null ) {
      balance = Utils.parseInt( getProperty( "balance", "100" ), Integer.parseInt( "100" ) );
    }
    return balance.intValue();
  }

  private Integer packCount = null;
  public int getPackCount() {
    if( packCount == null ) {
      packCount = getPlayers() * getRounds();
    }
    return packCount.intValue();
  }

  private Map<Set<String>,Integer> packComposition = null;
  public Map<Set<String>,Integer> getPackComposition() {
    if( packComposition == null ) {
      Map<Set<String>, Integer> comp = new HashMap();
      String pack = getProperty( PACK_NAME );
      String[] rarityCountPairArray = pack.split( "," );
      for( String rarityCountPair : rarityCountPairArray ) {
        String[] raritiesCount = rarityCountPair.split( ":" );
        String rarity = raritiesCount[ 0 ];
        String[] rarityArray = rarity.split( "\\+" );
        Set<String> raritySet = new HashSet<String>( Arrays.asList( rarityArray ) );
        String count = raritiesCount[ 1 ];
        comp.put( raritySet, Utils.parseInt( count, 1 ) );
      }
      packComposition = comp;
    }
    return packComposition;
  }

  private Random random = null;
  public Random getRandom() {
    if( random == null ) {
      long seed = System.currentTimeMillis();
      String str = getProperty( "seed" );
      if( str != null ) {
        try {
          seed = Long.parseLong( str );
        } catch( NumberFormatException e ) {
          // Ignore it and use the default time based seed.
        }
      } else {
        setProperty( "seed", Long.toString( seed ) );
      }
      random = new Random( seed );
    }
    return random;
  }

  @Override
  public String toString() {
    StringWriter writer = new StringWriter();
    try {
      store( writer, null );
    } catch( IOException e ) {
      // Not likely to happen with an in memory writer.
    }
    return writer.toString();
  }

}
