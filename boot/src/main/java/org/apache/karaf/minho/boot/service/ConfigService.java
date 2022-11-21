/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.karaf.minho.boot.service;

import org.apache.karaf.minho.boot.config.Config;
import org.apache.karaf.minho.boot.spi.Service;

/**
 * Core Karaf Config service holding the main Karaf configuration.
 */
public class ConfigService extends Config implements Service {
    @Override
    public String name() {
        return "minho-config-service";
    }

    @Override
    public int priority() {
        return Integer.MIN_VALUE + 1;
    }
}
