package com.cirrus.server;

import com.cirrus.agent.ICirrusAgent;
import com.cirrus.agent.ICirrusAgentBundleDescription;
import com.cirrus.agent.ICirrusAgentIdentifier;
import com.cirrus.agent.IStorageServiceVendor;
import com.cirrus.agent.impl.UUIDBasedCirrusAgentIdentifier;
import com.cirrus.osgi.extension.ICirrusStorageService;
import com.cirrus.server.exception.*;
import com.cirrus.server.impl.OSGIBasedCirrusServer;
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

        final List<ICirrusAgent> cirrusAgents = cirrusServer.listCirrusAgents();
        assertNotNull(cirrusAgents);
        assertTrue(cirrusAgents.isEmpty());
    }

    @Test(expected = CirrusAgentInstallationException.class)
    public void shouldErrorForInstallationOfBadBundle() throws CirrusAgentAlreadyExistException, CirrusAgentInstallationException, StartCirrusAgentException, IOException, StartCirrusServerException {
        final ICirrusServer cirrusServer = new OSGIBasedCirrusServer();
        cirrusServer.start();

        final URL bundleURL = this.getClass().getResource("/badBundle.jar");

        cirrusServer.installCirrusAgent(bundleURL.toExternalForm());
    }

    @Test(expected = CirrusAgentAlreadyExistException.class)
    public void shouldErrorForInstallationOfExistingBundle() throws CirrusAgentInstallationException, StartCirrusAgentException, IOException, StartCirrusServerException, CirrusAgentAlreadyExistException {
        final ICirrusServer cirrusServer = new OSGIBasedCirrusServer();
        cirrusServer.start();

        final URL bundleURL = this.getClass().getResource("/bundle.jar");

        cirrusServer.installCirrusAgent(bundleURL.toExternalForm());
        // install two times
        cirrusServer.installCirrusAgent(bundleURL.toExternalForm());
    }

    @Test
    public void shouldInstallSuccessfullyANewBundle() throws IOException, StartCirrusServerException, CirrusAgentInstallationException, StartCirrusAgentException, CirrusAgentAlreadyExistException {
        final ICirrusServer cirrusServer = new OSGIBasedCirrusServer();
        cirrusServer.start();

        final URL bundleURL = this.getClass().getResource("/bundle.jar");

        cirrusServer.installCirrusAgent(bundleURL.toExternalForm());

        final List<ICirrusAgent> cirrusAgents = cirrusServer.listCirrusAgents();
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
    public void shouldErrorWhenUninstallNonExistingBundle() throws IOException, StartCirrusServerException, StopCirrusAgentException, CirrusAgentNotExistException, UninstallCirrusAgentException {
        final ICirrusServer cirrusServer = new OSGIBasedCirrusServer();
        cirrusServer.start();

        cirrusServer.uninstallCirrusAgent(new UUIDBasedCirrusAgentIdentifier());
    }

    @Test
    public void shouldSuccessWhenUninstallExistingBundle() throws IOException, StartCirrusServerException, StopCirrusAgentException, CirrusAgentNotExistException, CirrusAgentAlreadyExistException, CirrusAgentInstallationException, StartCirrusAgentException, UninstallCirrusAgentException {
        final ICirrusServer cirrusServer = new OSGIBasedCirrusServer();
        cirrusServer.start();

        final URL bundleURL = this.getClass().getResource("/bundle.jar");

        cirrusServer.installCirrusAgent(bundleURL.toExternalForm());
        final List<ICirrusAgent> cirrusAgents = cirrusServer.listCirrusAgents();

        cirrusServer.uninstallCirrusAgent(cirrusAgents.get(0).getIdentifier());

        final List<ICirrusAgent> newList = cirrusServer.listCirrusAgents();
        assertTrue(newList.isEmpty());
    }
}
