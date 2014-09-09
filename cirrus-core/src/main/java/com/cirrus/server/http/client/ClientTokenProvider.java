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
import com.cirrus.persistence.dao.profile.IUserProfileDAO;
import com.cirrus.server.http.client.impl.ClientSession;
import com.cirrus.server.osgi.extension.AuthenticationException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientTokenProvider {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    private final int sessionExpirationTime;
    private final Map<String, IClientSession> sessions;
    private final AuthenticationProvider authenticationProvider;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    public ClientTokenProvider(final IUserProfileDAO userProfileDAO, final int sessionExpirationTimeInSecond) {
        this.sessionExpirationTime = sessionExpirationTimeInSecond;
        this.sessions = new HashMap<>();
        this.authenticationProvider = new AuthenticationProvider(userProfileDAO);
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    public Token authenticate(final ICredentials credentials) throws AuthenticationException {
        credentials.authenticate(this.authenticationProvider);

        final UUID tokenValueUUID = UUID.randomUUID();
        final Token token = new Token(tokenValueUUID.toString());

        final ClientSession clientSession = new ClientSession(token, this.sessionExpirationTime);
        this.sessions.put(token.getTokenValue(), clientSession);

        return token;
    }

}
