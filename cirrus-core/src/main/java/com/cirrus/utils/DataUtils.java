package com.cirrus.utils;

public class DataUtils {

    /**
     * Returns the file name from the path
     * Never <code>null</code>
     */
    public static String extractFileNameFromPath(final String path) {
        final int lastIndex = path.lastIndexOf('/');
        if (lastIndex == -1) {
            throw new IllegalArgumentException("The specified path '" + path + "' doesn't contain '/'");
        } else {
            return path.substring(lastIndex + 1, path.length());
        }
    }
}
