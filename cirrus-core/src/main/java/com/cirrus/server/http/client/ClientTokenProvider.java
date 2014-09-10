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

import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;

public class ClientTokenProvider {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    private final int sessionExpirationTimeInSecond;
    private final Map<String, IClientSession> sessions;
    private final AuthenticationProvider authenticationProvider;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    public ClientTokenProvider(final IUserProfileDAO userProfileDAO, final int sessionExpirationTimeInSecond) {
        this.sessionExpirationTimeInSecond = sessionExpirationTimeInSecond;
        this.sessions = new HashMap<>();
        this.authenticationProvider = new AuthenticationProvider(userProfileDAO);
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new SessionChecker(), 0, sessionExpirationTimeInSecond, TimeUnit.SECONDS);
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    public Token authenticate(final ICredentials credentials) throws AuthenticationException {
        credentials.authenticate(this.authenticationProvider);

        final UUID tokenValueUUID = UUID.randomUUID();
        final Token token = new Token(tokenValueUUID.toString());

        final ClientSession clientSession = new ClientSession(token, this.sessionExpirationTimeInSecond * 1000);

        synchronized (this.sessions) {
            this.sessions.put(token.getTokenValue(), clientSession);
            this.sessions.notify();
        }

        return token;
    }

    public void invalidateToken(final String tokenValue) {
        synchronized (this.sessions) {
            this.sessions.remove(tokenValue);
            this.sessions.notify();
        }
    }

    public void validateToken(final Token token) throws AuthenticationException {
        final IClientSession clientSession = this.sessions.get(token.getTokenValue());
        if (clientSession == null) {
            throw new AuthenticationException("No session for specified token");
        } else {
            if (!clientSession.isValid()) {
                throw new AuthenticationException("A new authentication is necessary");
            }
        }
    }


    //==================================================================================================================
    // Private classes
    //==================================================================================================================

    private class SessionChecker implements Runnable {

        @Override
        public void run() {
            System.out.println("SessionRunner check token validity, " + sessions.size());

            sessions.forEach(new BiConsumer<String, IClientSession>() {
                @Override
                public void accept(final String key, final IClientSession clientSession) {
                    System.out.println("handling token " + key);
                    if (!clientSession.isValid()) {
                        invalidateToken(key);
                        System.out.println("Token " + key + " invalidated");
                    }
                }
            });
        }
    }
}
