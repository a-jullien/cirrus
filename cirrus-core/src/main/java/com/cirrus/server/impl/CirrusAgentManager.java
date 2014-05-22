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
import com.cirrus.agent.ICirrusAgentIdentifier;
import com.cirrus.agent.impl.CirrusAgent;
import com.cirrus.server.ICirrusAgentManager;
import com.cirrus.server.IGlobalContext;
import com.cirrus.server.exception.*;
import com.cirrus.server.utils.ConfigUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class CirrusAgentManager implements ICirrusAgentManager {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final String COMPONENT_NAME = "<cirrus-osgi-manager>";

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final Framework framework;
    private final List<ICirrusAgent> cirrusAgents;
    private final IGlobalContext globalContext;
    private boolean isStarted;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public CirrusAgentManager(final IGlobalContext globalContext) throws IOException {
        super();
        this.globalContext = globalContext;
        this.cirrusAgents = new ArrayList<>();
        this.framework = this.createFramework();
        this.isStarted = false;
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public void start() throws StartCirrusServerException {

        if (!this.isStarted()) {
            try {
                this.framework.init();
                this.framework.start();
                this.isStarted = true;
                this.framework.waitForStop(10);

            } catch (final BundleException | InterruptedException e) {
                throw new StartCirrusServerException(e);
            }

            OSGIBasedCirrusServer.LOGGER.info(COMPONENT_NAME + " started.");
        } else {
            throw new StartCirrusServerException("The cirrus server is already started");
        }
    }

    @Override
    public void stop() throws StopCirrusServerException {
        if (this.isStarted()) {
            try {
                // stop osgi framework
                this.framework.stop();
                this.isStarted = false;
            } catch (final BundleException e) {
                throw new StopCirrusServerException(e);
            }

            OSGIBasedCirrusServer.LOGGER.info(COMPONENT_NAME + " stopped.");
        } else {
            throw new StopCirrusServerException("The cirrus server is not started");
        }
    }

    @Override
    public boolean isStarted() {
        return this.isStarted && (this.framework.getState() == Framework.ACTIVE);
    }

    @Override
    public ICirrusAgent installCirrusAgent(final String cirrusAgentLocation, final InputStream inputStream) throws CirrusAgentInstallationException, StartCirrusAgentException, CirrusAgentAlreadyExistException, ServerNotStartedException {
        this.checkServerIdStarted();

        OSGIBasedCirrusServer.LOGGER.info(COMPONENT_NAME + " Install bundle '" + cirrusAgentLocation + "'");
        final BundleContext bundleContext = this.framework.getBundleContext();
        try {
            final Bundle bundle = bundleContext.installBundle(cirrusAgentLocation, inputStream);

            final CirrusAgent cirrusAgent = new CirrusAgent(bundle);
            if (this.cirrusAgents.contains(cirrusAgent)) {
                throw new CirrusAgentAlreadyExistException(cirrusAgent.getIdentifier());
            } else {
                cirrusAgent.getStorageService().initialize(this.globalContext);
                this.cirrusAgents.add(cirrusAgent);

                OSGIBasedCirrusServer.LOGGER.info(COMPONENT_NAME + " New bundle available: " + cirrusAgent);
                cirrusAgent.start();
                OSGIBasedCirrusServer.LOGGER.info(COMPONENT_NAME + " Bundle " + cirrusAgent + " successfully started");

                return cirrusAgent;
            }

        } catch (final BundleException e) {
            throw new CirrusAgentInstallationException(e);
        }
    }

    @Override
    public ICirrusAgent installCirrusAgent(final String cirrusAgentLocation) throws CirrusAgentInstallationException, StartCirrusAgentException, CirrusAgentAlreadyExistException, ServerNotStartedException {
        return this.installCirrusAgent(cirrusAgentLocation, null);
    }

    @Override
    public void uninstallCirrusAgent(final ICirrusAgentIdentifier cirrusAgentIdentifier) throws StopCirrusAgentException, CirrusAgentNotExistException, UninstallCirrusAgentException, ServerNotStartedException {
        this.checkServerIdStarted();

        final ICirrusAgent cirrusAgentById = this.getCirrusAgentById(cirrusAgentIdentifier);
        cirrusAgentById.stop();
        cirrusAgentById.uninstall();
        this.cirrusAgents.remove(cirrusAgentById);

    }

    @Override
    public List<ICirrusAgent> listCirrusAgents() throws ServerNotStartedException {
        this.checkServerIdStarted();
        return this.cirrusAgents;
    }

    @Override
    public ICirrusAgent getCirrusAgentById(final ICirrusAgentIdentifier cirrusAgentId) throws CirrusAgentNotExistException, ServerNotStartedException {
        this.checkServerIdStarted();
        for (final ICirrusAgent cirrusAgent : this.cirrusAgents) {
            if (cirrusAgent.getIdentifier().equals(cirrusAgentId)) {
                return cirrusAgent;
            }
        }

        throw new CirrusAgentNotExistException(cirrusAgentId);
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

    private void checkServerIdStarted() throws ServerNotStartedException {
        if (!this.isStarted()) {
            throw new ServerNotStartedException();
        }
    }
}
