package com.cirrus.osgi.server;

import com.cirrus.osgi.agent.ICirrusAgent;
import com.cirrus.osgi.agent.ICirrusAgentBundleDescription;
import com.cirrus.osgi.agent.ICirrusAgentIdentifier;
import com.cirrus.osgi.agent.IStorageServiceVendor;
import com.cirrus.osgi.agent.impl.UUIDBasedCirrusAgentIdentifier;
import com.cirrus.osgi.extension.ICirrusStorageService;
import com.cirrus.osgi.server.exception.*;
import com.cirrus.osgi.server.impl.OSGIBasedCirrusServer;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static junit.framework.Assert.*;

public class OSGICirrusServerTest {

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Test
    public void shouldSuccessfullyCreateAndStartCirrusServer() throws StartCirrusServerException, StopCirrusServerException, IOException {
        final ICirrusServer cirrusServer = new OSGIBasedCirrusServer();
        cirrusServer.start();

        assertTrue(cirrusServer.getCirrusAgentAdministration().isStarted());

        final List<ICirrusAgent> cirrusAgents = cirrusServer.getCirrusAgentAdministration().listCirrusAgents();
        assertNotNull(cirrusAgents);
        assertTrue(cirrusAgents.isEmpty());
    }

    @Test(expected = CirrusAgentInstallationException.class)
    public void shouldErrorForInstallationOfBadBundle() throws Exception {
        final ICirrusServer cirrusServer = new OSGIBasedCirrusServer();
        cirrusServer.start();

        final URL bundleURL = this.getClass().getResource("/badBundle.jar");

        cirrusServer.getCirrusAgentAdministration().installCirrusAgent(bundleURL.toExternalForm());
    }

    @Test(expected = CirrusAgentAlreadyExistException.class)
    public void shouldErrorForInstallationOfExistingBundle() throws Exception {
        final ICirrusServer cirrusServer = new OSGIBasedCirrusServer();
        cirrusServer.start();

        final URL bundleURL = this.getClass().getResource("/bundle.jar");

        cirrusServer.getCirrusAgentAdministration().installCirrusAgent(bundleURL.toExternalForm());
        // install two times
        cirrusServer.getCirrusAgentAdministration().installCirrusAgent(bundleURL.toExternalForm());
    }

    @Test
    public void shouldInstallSuccessfullyANewBundle() throws Exception {
        final ICirrusServer cirrusServer = new OSGIBasedCirrusServer();
        cirrusServer.start();

        final URL bundleURL = this.getClass().getResource("/bundle.jar");

        cirrusServer.getCirrusAgentAdministration().installCirrusAgent(bundleURL.toExternalForm());

        final List<ICirrusAgent> cirrusAgents = cirrusServer.getCirrusAgentAdministration().listCirrusAgents();
        assertEquals(1, cirrusAgents.size());

        final ICirrusAgent cirrusAgent = cirrusAgents.get(0);
        final ICirrusAgentIdentifier cirrusAgentIdentifier = cirrusAgent.getIdentifier();
        assertNotNull(cirrusAgentIdentifier);

        final ICirrusAgentBundleDescription bundleDescription = cirrusAgent.getCirrusAgentBundleDescription();
        assertNotNull(bundleDescription);
        assertEquals("Dropbox Cirrus Bundle", bundleDescription.getName());
        assertEquals("Dropbox integration", bundleDescription.getDescription());
        assertEquals("1.0.0.SNAPSHOT", bundleDescription.getVersion());
        assertEquals("Antoine Jullien", bundleDescription.getVendor());

        final IStorageServiceVendor storageServiceVendor = cirrusAgent.getStorageServiceVendor();
        assertNotNull(storageServiceVendor);
        assertEquals("dropbox-cirrus-agent", storageServiceVendor.getName());
        assertEquals("Dropbox", storageServiceVendor.getVendor());
        assertEquals("1.7.6", storageServiceVendor.getVersion());

        final ICirrusStorageService storageService = cirrusAgent.getStorageService();
        assertNotNull(storageService);
    }

    @Test(expected = CirrusAgentNotExistException.class)
    public void shouldErrorWhenUninstallNonExistingBundle() throws Exception {
        final ICirrusServer cirrusServer = new OSGIBasedCirrusServer();
        cirrusServer.start();

        cirrusServer.getCirrusAgentAdministration().uninstallCirrusAgent(new UUIDBasedCirrusAgentIdentifier());
    }

    @Test
    public void shouldSuccessWhenUninstallExistingBundle() throws Exception {
        final ICirrusServer cirrusServer = new OSGIBasedCirrusServer();
        cirrusServer.start();

        final URL bundleURL = this.getClass().getResource("/bundle.jar");

        cirrusServer.getCirrusAgentAdministration().installCirrusAgent(bundleURL.toExternalForm());
        final List<ICirrusAgent> cirrusAgents = cirrusServer.getCirrusAgentAdministration().listCirrusAgents();

        cirrusServer.getCirrusAgentAdministration().uninstallCirrusAgent(cirrusAgents.get(0).getIdentifier());

        final List<ICirrusAgent> newList = cirrusServer.getCirrusAgentAdministration().listCirrusAgents();
        assertTrue(newList.isEmpty());
    }

    @Test
    public void shouldHaveCorrectStatusAfterStoppingServer() throws Exception {
        final ICirrusServer cirrusServer = new OSGIBasedCirrusServer();
        cirrusServer.start();

        assertTrue(cirrusServer.getCirrusAgentAdministration().isStarted());
        cirrusServer.stop();
        Thread.sleep(100);
        assertFalse(cirrusServer.getCirrusAgentAdministration().isStarted());
    }

    @Test(expected = ServerNotStartedException.class)
    public void shouldHaveErrorWhenInstallBundleWithNotStartedServer() throws Exception {
        final ICirrusServer cirrusServer = new OSGIBasedCirrusServer();

        final URL bundleURL = this.getClass().getResource("/bundle.jar");

        cirrusServer.getCirrusAgentAdministration().installCirrusAgent(bundleURL.toExternalForm());
    }

    @Test(expected = ServerNotStartedException.class)
    public void shouldHaveErrorWhenUninstallBundleWithNotStartedServer() throws Exception {
        final ICirrusServer cirrusServer = new OSGIBasedCirrusServer();

        cirrusServer.getCirrusAgentAdministration().uninstallCirrusAgent(new UUIDBasedCirrusAgentIdentifier());
    }
}
