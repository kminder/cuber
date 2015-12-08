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

import java.util.Random;

public class DraftFactory extends org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory<Draft> {

  private String name;
  private Config config;
  private Stack box;

  public DraftFactory( String name, Config config, Stack box ) {
    this.name = name;
    this.config = config;
    this.box = box;
  }

  public Draft generateRandomCandidate( Random random ) {
    return new Draft( name, config, box.copy(), random );
  }

}
