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

import com.cirrus.server.exception.*;
import com.cirrus.server.http.client.ISessionService;
import com.cirrus.server.http.client.impl.SessionService;

public interface ICirrusServer {

    /**
     * Returns the symbolical name of the cirrus server
     *
     * @return the server name
     */
    String getName();

    /**
     * start cirrus server
     */
    void start() throws StartCirrusServerException;

    /**
     * stop cirrus server
     */
    void stop() throws StopCirrusServerException;

    /**
     * Checks if the server is started or not
     *
     * @return true if the cirrus server is started, false otherwise
     */
    boolean isStarted();

    /**
     * Returns the administration part responsible for the bundle management
     */
    ICirrusAgentManager getCirrusAgentManager();

    /**
     * Returns the user operations
     */
    ICirrusUserOperationManager getCirrusUserOperations();

    /**
     * Returns the session service responsible of the token management
     */
    ISessionService getSessionService();
}
