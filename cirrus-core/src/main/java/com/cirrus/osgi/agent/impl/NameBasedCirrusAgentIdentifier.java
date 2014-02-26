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

package com.cirrus.osgi.agent.impl;

import com.cirrus.osgi.agent.ICirrusAgentIdentifier;

public class NameBasedCirrusAgentIdentifier implements ICirrusAgentIdentifier {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final long serialVersionUID = -4402557258206383693L;

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final String name;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public NameBasedCirrusAgentIdentifier(final String name) {
        this.name = name;
    }

    @Override
    public String toExternal() {
        return this.name;
    }

    //==================================================================================================================
    // Override
    //==================================================================================================================
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final NameBasedCirrusAgentIdentifier that = (NameBasedCirrusAgentIdentifier) o;

        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}

