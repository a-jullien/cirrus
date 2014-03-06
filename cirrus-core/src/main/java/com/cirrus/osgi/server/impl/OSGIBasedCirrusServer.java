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

package com.cirrus.osgi.server.impl;

import com.cirrus.data.ICirrusData;
import com.cirrus.osgi.agent.ICirrusAgent;
import com.cirrus.osgi.agent.ICirrusAgentBundleDescription;
import com.cirrus.osgi.extension.ICirrusStorageService;
import com.cirrus.osgi.server.ICirrusAgentAdministration;
import com.cirrus.osgi.server.ICirrusServer;
import com.cirrus.osgi.server.IMetaDataProvider;
import com.cirrus.osgi.server.configuration.CirrusProperties;
import com.cirrus.osgi.server.exception.StartCirrusServerException;
import com.cirrus.osgi.server.exception.StopCirrusServerException;
import com.cirrus.persistence.service.MongoDBService;
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

    private final ICirrusAgentAdministration cirrusAgentAdministration;
    private final IMetaDataProvider metaDataProvider;


    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public OSGIBasedCirrusServer() throws IOException {
        super();
        // global properties of the cirrus server
        final CirrusProperties cirrusProperties = new CirrusProperties();
        this.cirrusAgentAdministration = new CirrusAgentAdministration();
        // create mongodb service
        final MongoDBService mongoDBService = new MongoDBService(cirrusProperties.getProperty(CirrusProperties.MONGODB_URL));
        this.metaDataProvider = new MetaDataProvider(mongoDBService.getMetaDataDAO());
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Override
    public void start() throws StartCirrusServerException {
        // start cirrus agent manager
        this.cirrusAgentAdministration.start();
    }

    @Override
    public void stop() throws StopCirrusServerException {
        // stop cirrus agent manager
        this.cirrusAgentAdministration.stop();
    }

    @Override
    public ICirrusAgentAdministration getCirrusAgentAdministration() {
        return this.cirrusAgentAdministration;
    }

    @Override
    public IMetaDataProvider getMetaDataProvider() {
        return this.metaDataProvider;
    }

    //==================================================================================================================
    // Main
    //==================================================================================================================
    public static void main(final String[] args) throws Exception {
        final OSGIBasedCirrusServer osgiBasedCirrusServer = new OSGIBasedCirrusServer();
        osgiBasedCirrusServer.start();

        final ICirrusAgentAdministration agentAdministration = osgiBasedCirrusServer.getCirrusAgentAdministration();

        final String trustedToken = args[0];

        for (int i = 1; i < args.length; i++) {
            final String arg = args[i];
            agentAdministration.installCirrusAgent(arg);
        }

        final StringBuilder stringBuilder = new StringBuilder();
        final List<ICirrusAgent> existingAgents = agentAdministration.listCirrusAgents();
        for (final ICirrusAgent existingAgent : existingAgents) {
            final ICirrusAgentBundleDescription bundleDescription = existingAgent.getCirrusAgentBundleDescription();
            final ICirrusStorageService storageService = existingAgent.getStorageService();
            storageService.setAuthenticationToken(trustedToken);

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
