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

import com.cirrus.agent.ICirrusAgentIdentifier;
import com.cirrus.model.data.ICirrusData;
import com.cirrus.distribution.event.data.ICirrusDataEventVisitor;
import com.cirrus.utils.Try;

public class CirrusDataCreatedEvent extends AbstractCirrusDataEvent implements ICirrusDataCreatedEvent {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final long serialVersionUID = -3242620049985639823L;

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final ICirrusData cirrusData;
    private final String virtualPath;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public CirrusDataCreatedEvent(final ICirrusAgentIdentifier sourceAgentIdentifier, final String virtualPath, final ICirrusData cirrusData) {
        super(sourceAgentIdentifier);
        this.cirrusData = cirrusData;
        this.virtualPath = virtualPath;
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public Try<ICirrusData> accept(final ICirrusDataEventVisitor<ICirrusData> visitor) {
        return visitor.visit(this);
    }

    @Override
    public ICirrusData getCirrusData() {
        return this.cirrusData;
    }

    @Override
    public String getVirtualPath() {
        return this.virtualPath;
    }
}
