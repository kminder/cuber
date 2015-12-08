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
package net.minder.cuber.josql;

import org.josql.Query;
import org.josql.QueryExecutionException;
import org.josql.expressions.Expression;
import org.josql.internal.Utilities;

import java.util.List;

public class Utils {

  private static double[] EMPTY_ARRAY = new double[0];

  static double[] extractDoublesFromList( List list ) {
    if( list == null || list.isEmpty() ) {
      return EMPTY_ARRAY;
    } else {
      double[] array = new double[ list.size() ];
      for( int i = 0, n = list.size(); i < n; i++ ) {
        array[ i ] = convertToDouble( list.get( i ) );

      }
      return array;
    }
  }

  static double convertToDouble( Object o ) {
    if( o instanceof Number ) {
      return ((Number)o).doubleValue();
    } else if( o instanceof String ) {
      return Double.parseDouble( (String)o );
    } else {
      throw new IllegalArgumentException( "Cannot convert value to a double: " + o );
    }
  }


  static void checkExpressionIsNumeric( Query query, Expression exp ) throws QueryExecutionException {
    Class c;
    try {
      c = exp.getExpectedReturnType( query );
    } catch ( Exception e ) {
      throw new QueryExecutionException( "Unable to determine expected return type for expression: " + exp, e );
    }
    if( !Utilities.isNumber( c ) ) {
      throw new QueryExecutionException( "This function expects the expression " + exp + " to return a sub-class " + Number.class.getName() + " but it will return an instance of " + c.getName() ) ;
    }
  }

  static double evaluateNumericExpression( Query query, Expression exp, Object object ) throws QueryExecutionException {
    query.setCurrentObject( object );
    Object v;
    try {
      v = exp.getValue( object, query );
    } catch ( Exception e ) {
      throw new QueryExecutionException( "Unable to evaluate expression: " + exp + " on item " + object, e );
    }
    if( !Utilities.isNumber( v ) ) {
      throw new QueryExecutionException(
          "Expected expression " + exp + " to return a sub-class of " + Number.class.getName() + ", but returns " + object + " and instance of " + v.getClass().getName() );
    }
    double d = ((Number)v).doubleValue();
    return d;
  }

}
