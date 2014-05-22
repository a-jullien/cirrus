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

package com.cirrus.server.configuration;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class CirrusProperties {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    public static final String CIRRUS_SERVER_NAME = "cirrus.server.name";
    public static final String MONGODB_URL = "cirrus.mongodb.url";
    public static final String CIRRUS_ROOT_DIRECTORY = "cirrus.root.directory";

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final Properties properties;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public CirrusProperties() throws IOException {
        this.properties = new Properties();
        final URL resource = this.getClass().getResource("/cirrus.properties");
        this.properties.load(resource.openStream());
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    public String getProperty(final String key) {
        final String propertyValue = this.properties.getProperty(key);
        if (propertyValue == null) {
            throw new IllegalArgumentException("Property <" + key + "> doesn't exist");
        } else {
            return propertyValue;
        }
    }
}
