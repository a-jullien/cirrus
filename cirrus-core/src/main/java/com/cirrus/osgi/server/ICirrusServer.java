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

package com.cirrus.osgi.server;

import com.cirrus.osgi.agent.ICirrusAgent;
import com.cirrus.osgi.agent.ICirrusAgentIdentifier;
import com.cirrus.osgi.server.exception.*;

import java.util.List;

public interface ICirrusServer {

    /**
     * start cirrus server
     */
    void start() throws StartCirrusServerException;

    /**
     * stop cirrus server
     */
    void stop() throws StopCirrusServerException;

    /**
     * Returns true is the server is up and running, false otherwise
     */
    boolean isStarted();

    /**
     * install new cirrus agent as a new bundle in the current osgi platform
     */
    void installCirrusAgent(final String cirrusAgentPath) throws CirrusAgentInstallationException, StartCirrusAgentException, CirrusAgentAlreadyExistException, ServerNotStartedException;

    /**
     * uninstall existing cirrus agent from current osgi platform
     */
    void uninstallCirrusAgent(final ICirrusAgentIdentifier cirrusAgentIdentifier) throws StopCirrusAgentException, CirrusAgentNotExistException, UninstallCirrusAgentException, ServerNotStartedException;

    /**
     * Returns all available installed cirrus agents
     */
    List<ICirrusAgent> listCirrusAgents();
}
