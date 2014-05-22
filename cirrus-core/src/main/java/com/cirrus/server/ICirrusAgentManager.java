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

package com.cirrus.server;

import com.cirrus.agent.ICirrusAgent;
import com.cirrus.agent.ICirrusAgentIdentifier;
import com.cirrus.server.exception.*;

import java.io.InputStream;
import java.util.List;

public interface ICirrusAgentManager {

    /**
     * start cirrus bundle handler
     */
    void start() throws StartCirrusServerException;

    /**
     * stop cirrus bundle handler
     */
    void stop() throws StopCirrusServerException;

    /**
     * Returns true is the bundle manager is up and running, false otherwise
     */
    boolean isStarted();

    /**
     * install new cirrus agent as a new bundle in the current osgi platform from location
     */
    ICirrusAgent installCirrusAgent(final String cirrusAgentLocation) throws CirrusAgentInstallationException, StartCirrusAgentException, CirrusAgentAlreadyExistException, ServerNotStartedException;

    /**
     * install new cirrus agent as a new bundle in the current osgi platform from input stream
     */
    ICirrusAgent installCirrusAgent(final String cirrusAgentLocation, final InputStream inputStream) throws CirrusAgentInstallationException, StartCirrusAgentException, CirrusAgentAlreadyExistException, ServerNotStartedException;

    /**
     * uninstall existing cirrus agent from current osgi platform
     */
    void uninstallCirrusAgent(final ICirrusAgentIdentifier cirrusAgentIdentifier) throws StopCirrusAgentException, CirrusAgentNotExistException, UninstallCirrusAgentException, ServerNotStartedException;

    /**
     * Returns all available installed cirrus agents
     */
    List<ICirrusAgent> listCirrusAgents() throws ServerNotStartedException;

    /**
     * Returns the cirrus agent from the specified identifier
     *
     * @param cirrusAgentIdentifier the unique identifier
     * @return the {ICirrusAgent} agent. Never <code>null</code>
     */
    ICirrusAgent getCirrusAgentById(final ICirrusAgentIdentifier cirrusAgentIdentifier) throws CirrusAgentNotExistException, ServerNotStartedException;
}
