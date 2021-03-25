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
package org.apache.karaf.boot.module;

import lombok.extern.java.Log;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.UUID;
import java.util.jar.JarInputStream;

@Log
public class SpringBootModuleService implements ModuleService {

    @Override
    public boolean canHandle(String url) {
        try {
            try (JarInputStream jarInputStream = new JarInputStream(new URL(url).openStream())) {
                if (jarInputStream.getManifest().getMainAttributes().getValue("Spring-Boot-Version") != null) {
                    return true;
                }
            }
        } catch (Exception e) {
            // no-op
        }
        return false;
    }

    @Override
    public String add(String url, String ... args) throws Exception {
        final URLClassLoader classLoader = new URLClassLoader(new URL[]{ new URL(url) }, this.getClass().getClassLoader());
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            // disable tomcat stream handler
            final Method tomcat = classLoader.loadClass("org.apache.catalina.webresources.TomcatURLStreamHandlerFactory").getMethod("disable");
            if (!tomcat.isBridge()) {
                tomcat.setAccessible(true);
            }
            tomcat.invoke(null, null);
            // invoke spring boot main
            final Method main = classLoader.loadClass("org.springframework.boot.loader.JarLauncher").getMethod("main", String[].class);
            main.setAccessible(true);
            main.invoke(null, new Object[]{ args });
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
        String id;
        try (JarInputStream jarInputStream = new JarInputStream(new URL(url).openStream())) {
            id = jarInputStream.getManifest().getMainAttributes().getValue("Start-Class");
        }
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }

    @Override
    public void remove(String id) throws Exception {
        // TODO
    }

}
