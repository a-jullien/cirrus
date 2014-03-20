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

import com.cirrus.agent.ICirrusAgent;
import com.cirrus.agent.ICirrusAgentBundleDescription;
import com.cirrus.agent.authentication.impl.AccessKeyTrustedToken;
import com.cirrus.data.ICirrusData;
import com.cirrus.persistence.dao.meta.IMetaDataDAO;
import com.cirrus.persistence.service.MongoDBService;
import com.cirrus.server.ICirrusAgentManager;
import com.cirrus.server.ICirrusServer;
import com.cirrus.server.ICirrusUserOperationManager;
import com.cirrus.server.IGlobalContext;
import com.cirrus.server.configuration.CirrusProperties;
import com.cirrus.server.exception.StartCirrusServerException;
import com.cirrus.server.exception.StopCirrusServerException;
import com.cirrus.server.osgi.extension.ICirrusStorageService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

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

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public OSGIBasedCirrusServer() throws IOException {
        super();
        // global properties of the cirrus server
        final CirrusProperties cirrusProperties = new CirrusProperties();
        final String rootDirectory = cirrusProperties.getProperty(CirrusProperties.CIRRUS_ROOT_DIRECTORY);
        final IGlobalContext globalContext = GlobalContext.create(rootDirectory);
        // create mongodb service
        final MongoDBService mongoDBService = new MongoDBService(cirrusProperties.getProperty(CirrusProperties.MONGODB_URL));
        final IMetaDataDAO metaDataDAO = mongoDBService.getMetaDataDAO();
        // administration for cirrus bundles
        this.cirrusAgentManager = new CirrusAgentManager(globalContext);
        // user operations
        this.cirrusUserOperations = new CirrusUserOperationManager(this.cirrusAgentManager, metaDataDAO);
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

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
    public ICirrusAgentManager getCirrusAgentManager() {
        return this.cirrusAgentManager;
    }

    @Override
    public ICirrusUserOperationManager getCirrusUserOperations() {
        return this.cirrusUserOperations;
    }

    //==================================================================================================================
    // Main
    //==================================================================================================================
    @SuppressWarnings("unchecked")
    public static void main(final String[] args) throws Exception {
        final OSGIBasedCirrusServer osgiBasedCirrusServer = new OSGIBasedCirrusServer();
        osgiBasedCirrusServer.start();

        final ICirrusAgentManager agentAdministration = osgiBasedCirrusServer.getCirrusAgentManager();

        final String trustedToken = args[0];

        for (int i = 1; i < args.length; i++) {
            final String arg = args[i];
            agentAdministration.installCirrusAgent(arg);
        }

        final StringBuilder stringBuilder = new StringBuilder();
        final List<ICirrusAgent> existingAgents = agentAdministration.listCirrusAgents();
        for (final ICirrusAgent existingAgent : existingAgents) {
            final ICirrusAgentBundleDescription bundleDescription = existingAgent.getCirrusAgentBundleDescription();
            final ICirrusStorageService<AccessKeyTrustedToken> storageService = existingAgent.getStorageService();
            storageService.authenticate(new AccessKeyTrustedToken(trustedToken));

            final String accountName = storageService.getAccountName();
            final long totalSpace = storageService.getTotalSpace();
            final long usedSpace = storageService.getUsedSpace();

            stringBuilder.append(bundleDescription).append('\n')
                    .append("Account name: ").append(accountName).append('\n')
                    .append("Total space: ").append(totalSpace).append('\n')
                    .append("Used space: ").append(usedSpace).append('\n');

            final List<ICirrusData> rootData = storageService.list("/");
            stringBuilder.append("Children data on /:");
            for (final ICirrusData cirrusData : rootData) {
                stringBuilder.append(cirrusData);
            }
        }

        LOGGER.info(stringBuilder.toString());

        osgiBasedCirrusServer.stop();
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================

}
