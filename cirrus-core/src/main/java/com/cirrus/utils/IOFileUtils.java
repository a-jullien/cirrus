/*
 * *
 *  * Copyright (c) 2014 Antoine Jullien
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.cirrus.utils;

import java.io.File;

public class IOFileUtils {

    public static File getTmpDirectory() {
        final String tmpPath = System.getProperty("java.io.tmpdir", "/tmp");
        final File file = new File(tmpPath + File.separatorChar + "cirrus-test-directory");
        if (file.isFile()) {
            throw new RuntimeException("The tmp folder <" + file.getAbsolutePath() + "> should not be a file !");
        }
        if (!file.isDirectory()) {
            final boolean isCreated = file.mkdirs();
            if (!isCreated) {
                throw new RuntimeException("The tmp folder <" + file.getAbsolutePath() + "> couldn't be created.");
            }
        }
        return file;
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    static public boolean deleteDirectory(final File path) {
        if (path.exists()) {
            final File[] files = path.listFiles();
            if (files != null) {
                for (final File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return path.delete();
    }

}
