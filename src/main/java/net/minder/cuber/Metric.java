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

import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class Metric {

  public static final double zero = new Double( 0.0D );
  public static final double one = new Double( 1.0D );

  private static final Double ZERO = Double.valueOf( zero );
  private static final Double ONE = Double.valueOf( one );

  private String name;
  private Map<String,Double> data;
  private SummaryStatistics stats;


  public Metric( String name ) {
    this.name = name;
    this.data = new LinkedHashMap<String, Double>( 24 );
    this.stats = null;
  }

  public void add( String id, double value ) {
    Double d = Double.valueOf( value );
    if( data.containsKey( id ) ) {
      del( id );
    }
    data.put( id, d );
    stats = null;
  }

  public void del( String id ) {
    data.remove( id );
    stats = null;
  }

  public Map<String,Double> getData() {
    return data;
  }

  public String getName() {
    return name;
  }

  public double getMin() {
    return getStats().getMin();
  }

  public double getMax() {
    return getStats().getMax();
  }

  public double getSum() {
    return getStats().getSum();
  }

  public Double getAvg() {
    return getStats().getMean();
  }

  public double getStd() {
    return getStats().getStandardDeviation();
  }

  public double getVar() {
    return getStats().getVariance();
  }

  public double getCov() {
    return getStd() / getAvg();
  }

  public double getRng() {
    return getMax() - getMin();
  }

  public StatisticalSummary getStats() {
    if( stats == null ) {
      SummaryStatistics s = new SummaryStatistics();
      for( Double d: data.values() ) {
        s.addValue( d.doubleValue() );
      }
      stats = s;
    }
    return stats;
  }

}
