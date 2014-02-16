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
import com.cirrus.server.ICirrusServer;
import com.cirrus.server.exception.StartCirrusServerException;
import com.cirrus.server.exception.StopCirrusServerException;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

public class OSGIBasedCirrusServer implements ICirrusServer {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final Framework framework;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public OSGIBasedCirrusServer() {
        super();
        this.framework = this.createFramework();
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Override
    public void start() throws StartCirrusServerException {

        try {
            framework.init();
            framework.start();
            framework.waitForStop(10);
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
    public void installCirrusAgent(final ICirrusAgent cirrusAgent) {
        // TODO
    }

    @Override
    public void uninstallCirrusAgent(final ICirrusAgent cirrusAgent) {
        // TODO
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================

    private Framework createFramework() {
        final ServiceLoader<FrameworkFactory> factoryLoader = ServiceLoader.load(FrameworkFactory.class);
        final Iterator<FrameworkFactory> iterator = factoryLoader.iterator();
        final FrameworkFactory next = iterator.next();
        return next.newFramework(this.createFrameworkConfiguration());
    }

    private Map<String, String> createFrameworkConfiguration() {
        final Map<String, String> configuration = new HashMap<String, String>();
        configuration.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA,
                "com.cirrus.osgi.service; version=1.0.0");

        return configuration;
    }
}
