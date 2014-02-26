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

import com.cirrus.osgi.agent.ICirrusAgent;
import com.cirrus.osgi.agent.ICirrusAgentBundleDescription;
import com.cirrus.osgi.agent.ICirrusAgentIdentifier;
import com.cirrus.osgi.agent.impl.CirrusAgent;
import com.cirrus.data.ICirrusData;
import com.cirrus.osgi.extension.ICirrusStorageService;
import com.cirrus.osgi.server.ICirrusServer;
import com.cirrus.osgi.server.exception.*;
import com.cirrus.osgi.server.utils.ConfigUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class OSGIBasedCirrusServer implements ICirrusServer {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final Framework framework;
    private final List<ICirrusAgent> cirrusAgents;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public OSGIBasedCirrusServer() throws IOException {
        super();
        this.cirrusAgents = new ArrayList<>();
        this.framework = this.createFramework();
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Override
    public void start() throws StartCirrusServerException {

        try {
            this.framework.init();
            this.framework.start();
            this.framework.waitForStop(10);

        } catch (final BundleException | InterruptedException e) {
            throw new StartCirrusServerException(e);
        }

        System.out.println("Cirrus server started...");
    }

    @Override
    public void stop() throws StopCirrusServerException {
        try {
            // stop osgi framework
            this.framework.stop();
        } catch (final BundleException e) {
            throw new StopCirrusServerException(e);
        }

        System.out.println("Cirrus server stopped...");
    }

    @Override
    public boolean isStarted() {
        return this.framework.getState() == Framework.ACTIVE;
    }

    @Override
    public void installCirrusAgent(final String cirrusAgentPath) throws CirrusAgentInstallationException, StartCirrusAgentException, CirrusAgentAlreadyExistException, ServerNotStartedException {
        this.checkServerIdStarted();

        System.out.println("Try to install bundle '" + cirrusAgentPath + "'");
        final BundleContext bundleContext = this.framework.getBundleContext();
        try {
            final Bundle bundle = bundleContext.installBundle(cirrusAgentPath);

            final CirrusAgent cirrusAgent = new CirrusAgent(bundle);
            if (this.cirrusAgents.contains(cirrusAgent)) {
                throw new CirrusAgentAlreadyExistException(cirrusAgent.getIdentifier());
            } else {
                this.cirrusAgents.add(cirrusAgent);

                System.out.println("New bundle available: " + cirrusAgent);
                cirrusAgent.start();
                System.out.println("Bundle " + cirrusAgent + " successfully started");
            }

        } catch (final BundleException e) {
            throw new CirrusAgentInstallationException(e);
        }
    }

    @Override
    public void uninstallCirrusAgent(final ICirrusAgentIdentifier cirrusAgentIdentifier) throws StopCirrusAgentException, CirrusAgentNotExistException, UninstallCirrusAgentException, ServerNotStartedException {
        this.checkServerIdStarted();

        final ICirrusAgent cirrusAgentById = this.getCirrusAgentById(cirrusAgentIdentifier);
        if (cirrusAgentById == null) {
            throw new CirrusAgentNotExistException(cirrusAgentIdentifier);
        } else {
            cirrusAgentById.stop();
            cirrusAgentById.uninstall();

            this.cirrusAgents.remove(cirrusAgentById);
        }
    }

    @Override
    public List<ICirrusAgent> listCirrusAgents() {
        return this.cirrusAgents;
    }

    //==================================================================================================================
    // Main
    //==================================================================================================================
    public static void main(final String[] args) throws Exception {
        final OSGIBasedCirrusServer osgiBasedCirrusServer = new OSGIBasedCirrusServer();
        osgiBasedCirrusServer.start();

        final String trustedToken = args[0];

        for (int i = 1; i < args.length; i++) {
            final String arg = args[i];
            osgiBasedCirrusServer.installCirrusAgent(arg);

        }

        final StringBuilder stringBuilder = new StringBuilder();
        final List<ICirrusAgent> existingAgents = osgiBasedCirrusServer.listCirrusAgents();
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

        System.out.println(stringBuilder.toString());

        osgiBasedCirrusServer.stop();
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================

    private Framework createFramework() throws IOException {
        final ServiceLoader<FrameworkFactory> factoryLoader = ServiceLoader.load(FrameworkFactory.class);
        final Iterator<FrameworkFactory> iterator = factoryLoader.iterator();
        final FrameworkFactory next = iterator.next();
        return next.newFramework(ConfigUtil.createFrameworkConfiguration());
    }

    private ICirrusAgent getCirrusAgentById(final ICirrusAgentIdentifier cirrusAgentId) {
        for (final ICirrusAgent cirrusAgent : this.cirrusAgents) {
            if (cirrusAgent.getIdentifier().equals(cirrusAgentId)) {
                return cirrusAgent;
            }
        }

        return null;
    }

    private void checkServerIdStarted() throws ServerNotStartedException {
        if (!this.isStarted()) {
            throw new ServerNotStartedException();
        }
    }
}
