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

package com.cirrus.agent.impl;

import com.cirrus.agent.ICirrusAgentBundleDescription;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class CirrusAgentBundleDescription implements ICirrusAgentBundleDescription {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final long serialVersionUID = 6023780763494571913L;

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private final String name;
    private final String description;
    private final String version;
    private final String vendor;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    @JsonCreator
    public CirrusAgentBundleDescription(@JsonProperty("name") final String name,
                                        @JsonProperty("description") final String description,
                                        @JsonProperty("version") final String version,
                                        @JsonProperty("vendor") final String vendor) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.vendor = vendor;
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public String getVendor() {
        return this.vendor;
    }

    @Override
    public String toString() {
        return "Bundle <" + this.name + ", " + this.version + ", " + this.description + ", " + this.vendor + ">";
    }
}
