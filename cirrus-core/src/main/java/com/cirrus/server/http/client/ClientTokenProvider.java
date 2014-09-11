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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;

public class ClientTokenProvider {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    private final int sessionExpirationTimeInSecond;
    private final Map<Token, IClientSession> sessions;
    private final AuthenticationProvider authenticationProvider;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    public ClientTokenProvider(final IUserProfileDAO userProfileDAO, final int sessionExpirationTimeInSecond) {
        this.sessionExpirationTimeInSecond = sessionExpirationTimeInSecond;
        this.sessions = new ConcurrentHashMap<>();
        this.authenticationProvider = new AuthenticationProvider(userProfileDAO);
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new SessionChecker(), 0, sessionExpirationTimeInSecond, TimeUnit.SECONDS);
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    public Token authenticate(final ICredentials credentials) throws AuthenticationException {
        this.readWriteLock.writeLock().lock();

        try {
            credentials.authenticate(this.authenticationProvider);

            final Token token = new Token();

            final ClientSession clientSession = new ClientSession(token, this.sessionExpirationTimeInSecond * 1000);

            this.sessions.putIfAbsent(token, clientSession);

            return token;
        } finally {
            this.readWriteLock.writeLock().unlock();
        }
    }

    public void invalidateToken(final Token tokenValue) {
        this.readWriteLock.writeLock().lock();
        try {
            this.sessions.remove(tokenValue);
        } finally {
            this.readWriteLock.writeLock().unlock();
        }
    }

    public void validateToken(final Token token) throws AuthenticationException {
        this.readWriteLock.readLock().lock();
        try {
            final IClientSession clientSession = this.sessions.get(token);
            if (clientSession == null) {
                throw new AuthenticationException("No session for specified token");
            } else {
                if (!clientSession.isValid()) {
                    throw new AuthenticationException("A new authentication is necessary");
                }
            }
        } finally {
            this.readWriteLock.readLock().unlock();
        }
    }


    //==================================================================================================================
    // Private classes
    //==================================================================================================================

    private class SessionChecker implements Runnable {

        @Override
        public void run() {
            synchronized (sessions) {
                sessions.forEach(new BiConsumer<Token, IClientSession>() {
                    @Override
                    public void accept(final Token token, final IClientSession clientSession) {
                        if (!clientSession.isValid()) {
                            invalidateToken(token);
                            System.out.println("Session with token <" + token + "> invalidated");
                        }
                    }
                });
            }

            System.out.println("Active sessions:" + sessions.size());
        }
    }
}
