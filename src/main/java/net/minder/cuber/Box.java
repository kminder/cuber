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

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Box extends Stack {

  private static final String COLUMN_SEP = "\t";
  private static final String SOURCE_SEP = ",";

  private static final int COLUMN_NAME = 0;
  private static final int COLUMN_MANA_ANY = 1;
  private static final int COLUMN_MANA_WHITE = 2;
  private static final int COLUMN_MANA_BLUE = 3;
  private static final int COLUMN_MANA_BLACK = 4;
  private static final int COLUMN_MANA_RED = 5;
  private static final int COLUMN_MANA_GREEN = 6;
  private static final int COLUMN_MANA_TOTAL = 7;
  private static final int COLUMN_TYPE = 8;
  private static final int COLUMN_SUBTYPE = 9;
  private static final int COLUMN_OFFENSE = 10;
  private static final int COLUMN_DEFENSE = 11;
  private static final int COLUMN_STRENGTH = 12;
  private static final int COLUMN_SET = 13;
  private static final int COLUMN_CARD = 14;
  private static final int COLUMN_RARITY = 15;
  private static final int COLUMN_COUNT = 16;
  private static final int COLUMN_SOURCES = 17;

  public Box( String name ) {
    super( name );
  }

  public static Box load( File tsvFile ) throws IOException {
    Box box = new Box( FilenameUtils.getBaseName( tsvFile.getName() ) );
    BufferedReader reader = new BufferedReader( new FileReader( tsvFile ) );
    String line = "";
    while( line != null ) {
      line = reader.readLine();
      if( line != null ) {
        if( !line.isEmpty() && !line.startsWith( "Name" ) ) {
          String[] columns = line.split( COLUMN_SEP );
          String name = columns[COLUMN_NAME];
          String anyMana = columns[COLUMN_MANA_ANY];
          String whiteMana = columns[COLUMN_MANA_WHITE];
          String blueMana = columns[COLUMN_MANA_BLUE];
          String blackMana = columns[COLUMN_MANA_BLACK];
          String redMana = columns[COLUMN_MANA_RED];
          String greenMana = columns[COLUMN_MANA_GREEN];
          String totalMana = columns[COLUMN_MANA_TOTAL];
          String type = columns[COLUMN_TYPE];
          String subType = columns[COLUMN_SUBTYPE];
          String offense = columns[COLUMN_OFFENSE];
          String defense = columns[COLUMN_DEFENSE];
          String strength = columns[COLUMN_STRENGTH];
          String set = columns[COLUMN_SET];
          String num = columns[ COLUMN_CARD ];
          String rarity = columns[COLUMN_RARITY];
          String countStr = columns.length > COLUMN_COUNT ? columns[COLUMN_COUNT] : "1";
          String sourcesStr = columns.length > COLUMN_SOURCES ? columns[COLUMN_SOURCES] : "";
          int count = Utils.parseInt( countStr, 0 );
          int copy = 0;
          Set<String> sources = parseSources( sourcesStr );
          for( String source: sources ) {
            Card card = new Card(
                set, num, String.format( "%02d", copy++ ), name, type, subType, rarity,
                anyMana, whiteMana, blueMana, blackMana, redMana, greenMana, totalMana,
                offense, defense, strength, source );
            box.addCard( card );
          }
          for( int i=sources.size(); i < count; i++ ) {
            Card card = new Card(
                set, num, String.format( "%02d", copy++ ), name, type, subType, rarity,
                anyMana, whiteMana, blueMana, blackMana, redMana, greenMana, totalMana,
                offense, defense, strength, "" );
            box.addCard( card );
          }
        }
      }
    }
    reader.close();
    return box;
  }

//  public String toString() {
//    StringBuilder s = new StringBuilder();
//    s.append( getHeader( COLUMN_SEP ) );
//    s.append( System.lineSeparator() );
//    for( Card card: getCards() ) {
//      s.append( card.toString( COLUMN_SEP ) );
//      s.append( System.lineSeparator() );
//    }
//    return s.toString();
//  }

  private static String getHeader( String sep ) {
    return String.format(
        "Name%sX%sW%sU%sB%sR%sG%sT%sTyp%sSub%sOff%sDef%sStr%sSet%sCrd%sRar%sCnt%sSrc",
        sep, sep, sep, sep, sep, sep, sep, sep, sep, sep, sep, sep, sep, sep, sep, sep, sep );
  }

  private static Set<String> parseSources( String str ) {
    String[] parts = str.split( SOURCE_SEP );
    Set<String> set = new HashSet<String>();
    for( String source: parts ) {
      if( !source.trim().isEmpty() ) {
        set.add( source );
      }
    }
    return set;
  }

}
