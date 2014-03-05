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

import com.cirrus.osgi.server.exception.*;

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
     * Returns the administration part of the server
     */
    ICirrusAgentAdministration getCirrusAgentAdministration();

    /**
     * Returns the service responsible for the user data management
     */
    IUserDataService getUserDataService();
}