/**
 * The MIT License (MIT)
 * Copyright (c) 2014 Antoine Jullien
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.cirrus.server.impl;

import com.cirrus.agent.ICirrusAgent;
import com.cirrus.agent.impl.CirrusAgent;
import com.cirrus.agent.impl.StorageServiceVendor;
import com.cirrus.osgi.extension.ICirrusStorageService;
import com.cirrus.server.ICirrusServer;
import com.cirrus.server.exception.CirrusAgentInstallationException;
import com.cirrus.server.exception.StartCirrusServerException;
import com.cirrus.server.exception.StopCirrusServerException;
import com.cirrus.server.utils.ConfigUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import java.util.*;

public class OSGIBasedCirrusServer implements ICirrusServer {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final Framework framework;
    private final List<ICirrusAgent> cirrusAgents;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public OSGIBasedCirrusServer() {
        super();
        this.cirrusAgents = new ArrayList<ICirrusAgent>();
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

        } catch (final BundleException e) {
            throw new StartCirrusServerException(e);
        } catch (final InterruptedException e) {
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
    public void installCirrusAgent(final String cirrusAgentPath) throws CirrusAgentInstallationException {
        final BundleContext bundleContext = this.framework.getBundleContext();
        try {
            final Bundle bundle = bundleContext.installBundle(cirrusAgentPath);
            final Dictionary<String, String> dict = bundle.getHeaders();
            final String bundleName = dict.get(Constants.BUNDLE_NAME);
            final String bundleDescription = dict.get(Constants.BUNDLE_DESCRIPTION);
            final String bundleVersion = dict.get(Constants.BUNDLE_VERSION);
            final String bundleVendor = dict.get(Constants.BUNDLE_VENDOR);

            final String serviceName = dict.get(ICirrusStorageService.SERVICE_NAME_PROPERTY);
            final String serviceVersion = dict.get(ICirrusStorageService.SERVICE_VERSION_PROPERTY);
            final String serviceVendor = dict.get(ICirrusStorageService.SERVICE_VENDOR_PROPERTY);
            final String serviceClazz = dict.get(ICirrusStorageService.SERVICE_CLASS_PROPERTY);

            final StorageServiceVendor storageServiceVendor = new StorageServiceVendor(serviceName, serviceVersion, serviceVendor);
            try {


                final long bundleId = bundle.getBundleId();

                final CirrusAgent cirrusAgent = new CirrusAgent(storageServiceVendor);
                this.cirrusAgents.add(cirrusAgent);

                System.out.println("New bundle available: <" + bundleName + "> (version:" + bundleVersion + "|description:" + bundleDescription + "|vendor:" + bundleVendor);
                bundle.start();
                System.out.println("Bundle <" + bundleName + "> started");

                final Class<ICirrusStorageService> loadClass = (Class<ICirrusStorageService>) bundle.loadClass(serviceClazz);
                final ICirrusStorageService storageService = loadClass.newInstance();
                final long availableDiskSpace = storageService.getAvailableDiskSpace();
                System.out.println("available disk space = " + availableDiskSpace);


            } catch (final ClassNotFoundException e) {
                e.printStackTrace();
            } catch (final InstantiationException e) {
                e.printStackTrace();
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }



        } catch (final BundleException e) {
            throw new CirrusAgentInstallationException(e);
        }
    }

    @Override
    public void uninstallCirrusAgent(final String cirrusAgent) {
        final BundleContext bundleContext = this.framework.getBundleContext();
        // TODO
    }

    //==================================================================================================================
    // Main
    //==================================================================================================================
    public static void main(final String[] args) throws StartCirrusServerException, CirrusAgentInstallationException {
        final OSGIBasedCirrusServer osgiBasedCirrusServer = new OSGIBasedCirrusServer();
        osgiBasedCirrusServer.start();

        for (final String bundlePath : args) {
            osgiBasedCirrusServer.installCirrusAgent(bundlePath);
        }
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================

    private Framework createFramework() {
        final ServiceLoader<FrameworkFactory> factoryLoader = ServiceLoader.load(FrameworkFactory.class);
        final Iterator<FrameworkFactory> iterator = factoryLoader.iterator();
        final FrameworkFactory next = iterator.next();
        return next.newFramework(ConfigUtil.createFrameworkConfiguration());
    }
}
