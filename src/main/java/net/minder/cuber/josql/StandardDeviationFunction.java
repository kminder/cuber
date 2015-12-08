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

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.josql.Query;
import org.josql.QueryExecutionException;
import org.josql.expressions.Expression;

import java.util.List;

public class StandardDeviationFunction extends BaseNumericFunction {

  public static StandardDeviationFunction INSTANCE = new StandardDeviationFunction();

  @Override
  double evaluate( double[] values ) {
    return (new StandardDeviation()).evaluate( values );
  }

  public Double stdev( List list, Expression exp, String save ) throws QueryExecutionException {
    return eval( list, exp, save );
  }

  public Double stdev( List list, Expression exp ) throws QueryExecutionException {
    return stdev( list, exp, null );
  }

  public Double stdev( Expression exp ) throws QueryExecutionException {
    return stdev( (List)q.getVariable( Query.ALL_OBJS_VAR_NAME ), exp );
  }

}
