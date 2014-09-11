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

package com.cirrus.server.http.client;

import com.cirrus.model.authentication.ICredentials;
import com.cirrus.model.authentication.Token;
import com.cirrus.server.ICirrusServer;
import com.cirrus.server.http.client.impl.SessionService;
import com.cirrus.server.impl.OSGIBasedCirrusServer;
import com.cirrus.server.osgi.extension.AuthenticationException;

import java.io.IOException;

public class ClientServiceFactory {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    private final ICirrusServer cirrusServer;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    public ClientServiceFactory() throws IOException {
        this.cirrusServer = new OSGIBasedCirrusServer();
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    public Token authenticate(final ICredentials credentials) throws AuthenticationException {
        final ISessionService sessionService = this.cirrusServer.getSessionService();
        return sessionService.getClientTokenProvider().authenticate(credentials);
    }

    public void invalidateToken(final Token token) {
        final ISessionService sessionService = this.cirrusServer.getSessionService();
        sessionService.getClientTokenProvider().invalidateToken(token);
    }

    public ClientService createClientService(final Token token) throws AuthenticationException {
        final ISessionService sessionService = this.cirrusServer.getSessionService();
        final ClientTokenProvider clientTokenProvider = sessionService.getClientTokenProvider();
        clientTokenProvider.validateToken(token);
        return new ClientService(this.cirrusServer); // TODO optimize -> store client service to IClientSession

    }
}
