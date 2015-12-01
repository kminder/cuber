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
import org.josql.Query;
import org.josql.QueryExecutionException;
import org.josql.QueryParseException;
import org.josql.QueryResults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Stack {

  private String name;
  private List<Card> cards;

  public Stack( String name ) {
    this.name = name;
    this.cards = new ArrayList<Card>();
  }

  public String getName() {
    return name;
  }

  public List<Card> getCards() {
    return cards;
  }

  public Card getFirstCard() {
    return cards.get( 0 );
  }

  public Card getLastCard() {
    return cards.get( cards.size()-1 );
  }

  public QueryResults query( String query ) {
    try {
      Query q = new Query();
      q.parse( query );
      QueryResults r = q.execute( getCards() );
      return r;
    } catch( QueryParseException e ) {
      throw new IllegalArgumentException( e );
    } catch( QueryExecutionException e ) {
      throw new RuntimeException( e );
    }
  }

  public List<Card> selectCards( String filter ) {
    return (List<Card>)query( String.format( "select * from %s where %s", Card.class.getName(), filter ) ).getResults();
  }

  public List<Card> selectCardsByRarity( String rarity ) {
    return selectCards( String.format( "rarity is '%s'", rarity ) );
  }

  public List<Card> selectCardsByRarity( Set<String> rarities ) {
    String rarity = "'" + StringUtils.join( rarities, "','" ) + "'";
    return selectCards( String.format( "rarity in (%s)", rarity ) );
  }

}
