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

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.josql.Query;
import org.josql.QueryExecutionException;
import org.josql.expressions.Expression;

import java.util.List;

public class MaximumFunction extends BaseNumericFunction {

  public static MaximumFunction INSTANCE = new MaximumFunction();

  @Override
  double evaluate( double[] values ) {
    return StatUtils.max( values );
  }

  public Double max( List list, Expression exp, String save ) throws QueryExecutionException {
    return eval( list, exp, save );
  }

  public Double max( List list, Expression exp ) throws QueryExecutionException {
    return max( list, exp, null );
  }

  public Double max( Expression exp ) throws QueryExecutionException {
    return max( (List)q.getVariable( Query.ALL_OBJS_VAR_NAME ), exp );
  }

}
