package com.cirrus.server.utils;

import org.osgi.framework.Constants;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
public class ConfigUtil {

    //==================================================================================================================
    // Public
    //==================================================================================================================
    public static Map<String, String> createFrameworkConfiguration() {
        final Map<String, String> configuration = new HashMap<String, String>();
        configuration.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA,
                "com.cirrus.osgi.extension; version=1.0.0");

        final File cachedir = createCacheDir();
        if (cachedir != null) {
            configuration.put(Constants.FRAMEWORK_STORAGE, cachedir.getAbsolutePath());
        }

        return configuration;
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private static File createCacheDir() {
        final File cachedir;
        try {
            cachedir = File.createTempFile("cirrus.osgi", null);
            cachedir.delete();
            createShutdownHook(cachedir);
            return cachedir;
        } catch (IOException e) {
            // temp dir creation failed, return null
            return null;
        }
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
