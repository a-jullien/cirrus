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

import com.cirrus.agent.ICirrusAgentIdentifier;

import java.util.UUID;

public class UUIDBasedCirrusAgentIdentifier implements ICirrusAgentIdentifier {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final long serialVersionUID = 44549044499056945L;

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private final UUID uuid;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public UUIDBasedCirrusAgentIdentifier() {
        this.uuid = UUID.randomUUID();
    }

    public UUIDBasedCirrusAgentIdentifier(final String externalRepresentation) {
        this.uuid = UUID.nameUUIDFromBytes(externalRepresentation.getBytes());
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public String toExternal() {
        return this.uuid.toString();
    }

    //==================================================================================================================
    // Override
    //==================================================================================================================
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final UUIDBasedCirrusAgentIdentifier that = (UUIDBasedCirrusAgentIdentifier) o;

        return !(uuid != null ? !uuid.equals(that.uuid) : that.uuid != null);

    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
