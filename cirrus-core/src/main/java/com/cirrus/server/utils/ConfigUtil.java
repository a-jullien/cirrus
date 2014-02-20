/**
 * The MIT License (MIT)
 * Copyright (c) 2014 Antoine Jullien
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
