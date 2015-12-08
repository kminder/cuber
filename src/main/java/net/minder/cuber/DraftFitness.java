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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DraftFitness {

  private static HashSet<String> OUTLIER_CANDIDATES = new HashSet<String>( Stack.METRICS );

  private static Map<String,Double> METRIC_WEIGHTING = new HashMap<String,Double>();
  static {
    METRIC_WEIGHTING.put( Pack.WEIGHT, Double.valueOf( 0.0f ) );
    METRIC_WEIGHTING.put( Pack.CREATURE, Double.valueOf( 2.0f ) );
    METRIC_WEIGHTING.put( Pack.COMMON, Double.valueOf( 0.0f ) );
    METRIC_WEIGHTING.put( Pack.UNCOMMON, Double.valueOf( 0.0f ) );
    METRIC_WEIGHTING.put( Pack.RARE, Double.valueOf( 0.0f ) );
    METRIC_WEIGHTING.put( Pack.MYTHIC, Double.valueOf( 0.0f ) );
  }

  private Draft draft;
  private Map<String,Metric> metrics;
  private double aggregate;

  public DraftFitness( Draft draft ) {
    this.draft = draft;
    this.metrics = new HashMap<String,Metric>();
    for( String n: Draft.METRIC_NAMES ) {
      Metric m = computeMetric( draft, n );
      this.metrics.put( n, m );
      double d = computeDeviation( m );
      this.aggregate += d;
    }
  }

  private static double getWeight( final String metric ) {
    Double d = METRIC_WEIGHTING.get( metric );
    return d == null ? 1.0d : d.doubleValue();
  }

  private static Metric computeMetric( Draft draft, String name ) {
    Metric metric;
    if( OUTLIER_CANDIDATES.contains( name ) ) {
      metric = draft.getMetricTotal( name );
    } else {
      metric = new Metric( name );
      if ( name.equals( Draft.DRAFT_DUP ) ) {
        metric.add( draft.getName(), draft.getDraftDuplication() );
      } else if ( name.equals( Draft.PACK_DUP ) ) {
        metric.add( draft.getName(), draft.getPackDuplication() );
      }
    }
    return metric;
  }

  private static double computeDeviation( Metric metric ) {
    double mul = getWeight( metric.getName() );
    double num = metric.getVar(); //metric.getStd();
    double den = metric.getAvg();
    double var;
    if( den == 0.0f ) {
      if( num == 0.0f ) {
        var = 0.0f;
      } else {
        var = Double.MAX_VALUE;
      }
    } else {
      var = num / den;
    }
    double dev = var * mul;
    return dev;
  }

  public double getAggregate() {
    return aggregate;
  }

  public Metric getOutlierMetric() {
    Metric m = null;
    for( Metric i: metrics.values() ) {
      if( m == null ) {
        m = i;
      } else {
        double md = computeDeviation( m );
        double id = computeDeviation( i );
        int c = Double.compare( id, md );
        if( c > 0 ) {
          m = i;
        }
      }
    }
    return m;
  }

  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append( Double.toString( getAggregate() ) );
    s.append( "[" );
    boolean first = true;
    for( Metric m: metrics.values() ) {
      if( !first ) {
        s.append( "," );
      } else {
        first = false;
      }
      s.append( m.getName() );
      s.append( "=" );
      s.append( String.format( "%.2f", computeDeviation( m ) ) );
    }
    s.append( "]" );
    return s.toString();
  }

}
