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

import java.util.Comparator;
import java.util.Map;

public class Pack extends Stack {

  public Pack( String name ) {
    super( name );
  }

  public String toString() {
    //String ids = StringUtils.join( Utils.column( 0, query( String.format( "select setId from %s order by setId", Card.class.getName() ) ) ), "," );
    Map<String,Metric> metrics = getMetrics();
    return String.format(
        "Pack[%s]: T=%2d, C=%.0f, U=%.0f, R=%.0f, c=%2.0f, i=%.0f, e=%.0f, s=%.0f, a=%.0f, l=%.0f, M=%.0f/%.0f/%1.1f (%2.0f/%2.0f/%2.0f/%2.0f/%2.0f) S=%2.0f/%2.0f/%1.1f(%2.0f/%2.0f), W=%3.0f",
        getName(), getCount(),
        metrics.get(COMMON).getSum(), metrics.get(UNCOMMON).getSum(), metrics.get(RARE).getSum(),
        metrics.get(CREATURE).getSum(), metrics.get(INSTANT).getSum(), metrics.get(ENCHANTMENT).getSum(), metrics.get(SORCERY).getSum(), metrics.get(ARTIFACT).getSum(), metrics.get(LAND).getSum(),
        metrics.get(COST).getSum(), metrics.get(COST).getMax(), metrics.get(COST).getAvg(),
        metrics.get(WHITE).getSum(), metrics.get(BLUE).getSum(), metrics.get(BLACK).getSum(), metrics.get(RED).getSum(), metrics.get(GREEN).getSum(),
        metrics.get(STRENGTH).getSum(), metrics.get(STRENGTH).getMax(), metrics.get(STRENGTH).getAvg(),
        metrics.get(OFFENSE).getSum(), metrics.get(DEFENSE).getSum(),
        metrics.get(WEIGHT).getSum() );
        //ids );
  }

  public static Comparator<Pack> NAME_COMPARATOR = new WeightComparator();
  public static class NameComparator implements Comparator<Pack> {
    public int compare( Pack left, Pack right ) {
      return left.getName().compareTo( right.getName() );
    }
  }

  public static Comparator<Pack> WEIGHT_COMPARATOR = new WeightComparator();
  public static class WeightComparator implements Comparator<Pack> {
    public int compare( Pack left, Pack right ) {
      return Double.compare( right.getMetrics().get( WEIGHT ).getSum(), left.getMetrics().get( WEIGHT ).getSum() );
    }
  }

}
