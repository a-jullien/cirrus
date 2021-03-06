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

package com.cirrus.server.http.entity;

import com.cirrus.agent.ICirrusAgentBundleDescription;
import com.cirrus.agent.impl.CirrusAgentBundleDescription;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class CirrusAgents {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    private final List<ICirrusAgentBundleDescription> agents;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    public CirrusAgents() {
        this.agents = new ArrayList<>();
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    public void addAgent(final ICirrusAgentBundleDescription agentDescription) {
        this.agents.add(agentDescription);
    }

    //==================================================================================================================
    // Getters
    //==================================================================================================================

    public List<ICirrusAgentBundleDescription> getAgents() {
        return agents;
    }
}
