package com.cirrus.server.impl;

import com.cirrus.agent.ICirrusAgent;
import com.cirrus.server.ICirrusServer;
import com.cirrus.server.exception.StartCirrusServerException;
import com.cirrus.server.exception.StopCirrusServerException;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.*;

public class OSGIBasedCirrusServer implements ICirrusServer {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
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
        final Future<StartResult> future = this.executorService.submit(new Callable<StartResult>() {
            @Override
            public StartResult call() throws Exception {
                try {
                    framework.init();
                    framework.start();
                    framework.waitForStop(10);

                    return StartResult.success();

                } catch (final BundleException e) {
                    return StartResult.failure(e);
                } catch (final InterruptedException e) {
                    return StartResult.failure(e);
                }
            }
        });

        try {
            final StartResult startResult = future.get();
            if (!startResult.isSuccess()) {
                throw startResult.getReason();
            }
        } catch (final InterruptedException e) {
            throw new StartCirrusServerException(e);
        } catch (final ExecutionException e) {
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
        } finally {
            // finally stop main thread
            this.executorService.shutdown();
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
