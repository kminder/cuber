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

import org.josql.QueryExecutionException;
import org.josql.expressions.Expression;
import org.josql.functions.AbstractFunctionHandler;

import java.util.List;

abstract class BaseNumericFunction extends AbstractFunctionHandler {

  protected Double eval( List list, Expression exp, String save ) throws QueryExecutionException {
    if( save != null ) {
      Object prev = q.getSaveValue( save );
      if( prev != null ) {
        return (Double)prev;
      }
    }

    Utils.checkExpressionIsNumeric( q, exp );

    Object co = this.q.getCurrentObject();
    double[] a = new double[list.size()];
    for( int i=0, n=list.size(); i<n; i++) {
      Object o = list.get( i );
      a[ i ] = Utils.evaluateNumericExpression( q, exp, o );
    }

    double d = evaluate( a );
    Double r = new Double( d );
    if( save != null ) {
      q.setSaveValue( save, r );
    }

    this.q.setCurrentObject( co );
    return r;
  }

  abstract double evaluate( double[] values );

}
