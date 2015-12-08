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
package net.minder.cuber.poc;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.Random;

public class Stats {

  public static void main( String[] args ) {

    SummaryStatistics s1 = new SummaryStatistics();
    SummaryStatistics s2 = new SummaryStatistics();
    SummaryStatistics s3 = new SummaryStatistics();
    SummaryStatistics s4 = new SummaryStatistics();
    Random r = new Random();
    for( int i=0; i<1000; i++ ) {
      double d = r.nextDouble();
      s1.addValue( d );
      s2.addValue( d * 10 );
      s3.addValue( d * 100 );
      s4.addValue( d * 1000 );
    }
    System.out.println( "s1=" + s1 );
    System.out.println( "s2=" + s2 );
    System.out.println( "s3=" + s3 );
    System.out.println( "s4=" + s4 );

    System.out.println( "cv1=" + s1.getStandardDeviation()/s1.getMean() );
    System.out.println( "cv2=" + s2.getStandardDeviation()/s2.getMean() );
    System.out.println( "cv3=" + s3.getStandardDeviation()/s3.getMean() );
    System.out.println( "cv4=" + s4.getStandardDeviation()/s4.getMean() );
  }

}
