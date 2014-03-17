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

import com.cirrus.data.ICirrusData;
import com.cirrus.distribution.event.data.ICirrusDataEventVisitor;
import com.cirrus.agent.ICirrusAgentIdentifier;
import com.cirrus.server.exception.IllegalOperationException;

public class CirrusDataRemovedEvent extends AbstractCirrusDataEvent implements ICirrusDataRemovedEvent {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final long serialVersionUID = 4728291016167276070L;

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final String virtualPath;
    private final ICirrusData cirrusData;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public CirrusDataRemovedEvent(final ICirrusAgentIdentifier sourceAgentIdentifier, final String virtualPath, final ICirrusData cirrusData) {
        super(sourceAgentIdentifier);
        this.virtualPath = virtualPath;
        this.cirrusData = cirrusData;
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public ICirrusData getCirrusData() {
        return this.cirrusData;
    }

    @Override
    public String getVirtualPath() {
        return this.virtualPath;
    }

    @Override
    public void accept(final ICirrusDataEventVisitor visitor) throws IllegalOperationException {
        visitor.visit(this);
    }
}
