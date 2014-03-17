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

package com.cirrus.distribution.event.data.impl;

import com.cirrus.distribution.event.data.ICirrusDataEvent;
import com.cirrus.agent.ICirrusAgentIdentifier;

public abstract class AbstractCirrusDataEvent implements ICirrusDataEvent{

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final long serialVersionUID = 7537082033994660851L;

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final long eventTimeStamp;
    private final ICirrusAgentIdentifier sourceAgentIdentifier;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    protected AbstractCirrusDataEvent(final ICirrusAgentIdentifier sourceAgentIdentifier) {
        super();
        this.sourceAgentIdentifier = sourceAgentIdentifier;
        this.eventTimeStamp = System.currentTimeMillis();
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public long getEventTimeStamp() {
        return this.eventTimeStamp;
    }

    @Override
    public ICirrusAgentIdentifier getSourceCirrusAgentId() {
        return this.sourceAgentIdentifier;
    }
}
