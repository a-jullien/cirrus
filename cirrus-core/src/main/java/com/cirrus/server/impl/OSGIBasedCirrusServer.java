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

package com.cirrus.server.impl;

import com.cirrus.persistence.dao.meta.IMetaDataDAO;
import com.cirrus.persistence.service.MongoDBService;
import com.cirrus.server.ICirrusAgentManager;
import com.cirrus.server.ICirrusServer;
import com.cirrus.server.ICirrusUserOperationManager;
import com.cirrus.server.IGlobalContext;
import com.cirrus.server.configuration.CirrusProperties;
import com.cirrus.server.exception.StartCirrusServerException;
import com.cirrus.server.exception.StopCirrusServerException;
import com.cirrus.server.http.client.ClientTokenProvider;
import com.cirrus.server.http.client.ISessionService;
import com.cirrus.server.http.client.impl.SessionService;
import org.apache.log4j.Logger;

import java.io.IOException;

public class OSGIBasedCirrusServer implements ICirrusServer {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final String LOGGER_NAME = "<cirrus-server>";

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    public static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);

    private final ICirrusAgentManager cirrusAgentManager;
    private final ICirrusUserOperationManager cirrusUserOperations;
    private final String name;
    private final SessionService sessionService;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public OSGIBasedCirrusServer() throws IOException {
        super();
        // global properties of the cirrus server
        final CirrusProperties cirrusProperties = new CirrusProperties();
        final String rootDirectory = cirrusProperties.getProperty(CirrusProperties.CIRRUS_ROOT_DIRECTORY);
        this.name = cirrusProperties.getProperty(CirrusProperties.CIRRUS_SERVER_NAME);
        final IGlobalContext globalContext = GlobalContext.create(rootDirectory);
        // create mongodb service
        final MongoDBService mongoDBService = new MongoDBService(cirrusProperties.getProperty(CirrusProperties.MONGODB_URL));
        final IMetaDataDAO metaDataDAO = mongoDBService.getMetaDataDAO();
        // administration for cirrus bundles
        this.cirrusAgentManager = new CirrusAgentManager(globalContext);
        // user operations
        this.cirrusUserOperations = new CirrusUserOperationManager(this.cirrusAgentManager, metaDataDAO);
        // session service
        final int defaultSessionExpirationTime = Integer.valueOf(cirrusProperties.getProperty(CirrusProperties.DEFAULT_SESSION_EXPIRATION_TIME));
        this.sessionService = new SessionService(new ClientTokenProvider(mongoDBService.getUserProfileDAO(), defaultSessionExpirationTime));
        LOGGER.info("cirrus server initialized");
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void start() throws StartCirrusServerException {
        // start cirrus agent manager
        this.cirrusAgentManager.start();
    }

    @Override
    public void stop() throws StopCirrusServerException {
        // stop cirrus agent manager
        this.cirrusAgentManager.stop();
    }

    @Override
    public boolean isStarted() {
        return this.cirrusAgentManager.isStarted();
    }

    @Override
    public ICirrusAgentManager getCirrusAgentManager() {
        return this.cirrusAgentManager;
    }

    @Override
    public ICirrusUserOperationManager getCirrusUserOperations() {
        return this.cirrusUserOperations;
    }

    @Override
    public ISessionService getSessionService() {
        return this.sessionService;
    }
}
