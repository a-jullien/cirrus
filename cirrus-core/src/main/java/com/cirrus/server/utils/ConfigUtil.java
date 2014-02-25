/**
 * Copyright (c) 2014 Antoine Jullien
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cirrus.server.utils;

import org.osgi.framework.Constants;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("ALL")
public class ConfigUtil {

    //==================================================================================================================
    // Public
    //==================================================================================================================
    public static Map<String, String> createFrameworkConfiguration() throws IOException {
        final Map<String, String> configuration = new HashMap<String, String>();

        final Properties p = new Properties();

        p.load(ConfigUtil.class.getResourceAsStream("/framework-config.properties"));
        final Set<String> propertyNames = p.stringPropertyNames();
        for (String propertyName : propertyNames) {
            final String propertyValue = p.getProperty(propertyName);
            configuration.put(propertyName, propertyValue);
        }

        final File cachedir = createCacheDir();
        if (cachedir != null) {
            configuration.put(Constants.FRAMEWORK_STORAGE, cachedir.getAbsolutePath());
        }

        return configuration;
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private static File createCacheDir() throws IOException {
        final File cachedir = File.createTempFile("cirrus.osgi", null);
        cachedir.delete();
        createShutdownHook(cachedir);
        return cachedir;
    }

    private static void createShutdownHook(final File cachedir) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                deleteFileOrDir(cachedir);
            }
        });
    }

    private static void deleteFileOrDir(File file) {
        if (file.isDirectory()) {
            File[] childs = file.listFiles();
            for (File child : childs) {
                deleteFileOrDir(child);
            }
        }
        file.delete();
    }
}
