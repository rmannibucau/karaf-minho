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
package org.apache.karaf.core.module;

import lombok.extern.java.Log;
import org.apache.karaf.core.module.microprofile.MicroprofileModule;
import org.apache.karaf.core.module.osgi.BundleModule;
import org.apache.karaf.core.module.springboot.SpringBootModule;

@Log
public class ModuleService {

    public static void add(String url, String ... args) throws Exception {
        log.info("Installing module from " + url);

        BundleModule bundleModule = new BundleModule();
        if (bundleModule.canHandle(url)) {
            log.info("Installing OSGi module " + url);
            bundleModule.add(url);
        } else {
            log.info("Not bundle: " + url);
        }

        SpringBootModule springBootModule = new SpringBootModule();
        if (springBootModule.canHandle(url)) {
            log.info("Installing Spring Boot module " + url);
            springBootModule.add(url);
        }

        MicroprofileModule microprofileModule = new MicroprofileModule();
        if (microprofileModule.canHandle(url)) {
            log.info("Installing Microprofile module " + url);
            microprofileModule.add(url);
        }
    }

    public static void remove(String id) throws Exception {
        log.info("Uninstalling module " + id);

        BundleModule bundleModule = new BundleModule();
        if (bundleModule.is(id)) {
            bundleModule.remove(id);
        }

        SpringBootModule springBootModule = new SpringBootModule();
        if (springBootModule.is(id)) {
            springBootModule.remove(id);
        }

        MicroprofileModule microprofileModule = new MicroprofileModule();
        if (microprofileModule.is(id)) {
            microprofileModule.remove(id);
        }
    }

}
