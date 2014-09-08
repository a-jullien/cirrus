package com.cirrus.server.osgi.server;

import com.cirrus.agent.ICirrusAgent;
import com.cirrus.agent.ICirrusAgentBundleDescription;
import com.cirrus.agent.ICirrusAgentIdentifier;
import com.cirrus.agent.IStorageServiceVendor;
import com.cirrus.agent.impl.UUIDBasedCirrusAgentIdentifier;
import com.cirrus.persistence.dao.meta.IMetaDataDAO;
import com.cirrus.persistence.service.MongoDBService;
import com.cirrus.server.ICirrusAgentManager;
import com.cirrus.server.IGlobalContext;
import com.cirrus.server.configuration.CirrusProperties;
import com.cirrus.server.exception.*;
import com.cirrus.server.impl.CirrusAgentManager;
import com.cirrus.server.impl.GlobalContext;
import com.cirrus.server.osgi.extension.ICirrusStorageService;
import com.cirrus.utils.IOFileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static junit.framework.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CirrusAgentAdministrationTest {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private IMetaDataDAO metaDataDAO;
    private File tmpDir;
    private IGlobalContext context;

    @Before
    public void setUp() throws IOException {
        this.tmpDir = IOFileUtils.getTmpDirectory();
        this.context = GlobalContext.create(this.tmpDir.getPath());
        // global properties of the cirrus server
        final CirrusProperties cirrusProperties = new CirrusProperties();
        // create mongodb service
        final MongoDBService mongoDBService = new MongoDBService(cirrusProperties.getProperty(CirrusProperties.MONGODB_URL));
        this.metaDataDAO = mongoDBService.getMetaDataDAO();
    }

    @After
    public void tearDown() {
        this.metaDataDAO.dropCollection();
        IOFileUtils.deleteDirectory(this.tmpDir);
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Test
    public void shouldSuccessfullyCreateAndStartCirrusServer() throws StartCirrusServerException, StopCirrusServerException, IOException, ServerNotStartedException {
        final ICirrusAgentManager cirrusAgentAdministration = new CirrusAgentManager(this.context);
        cirrusAgentAdministration.start();

        assertTrue(cirrusAgentAdministration.isStarted());

        final List<ICirrusAgent> cirrusAgents = cirrusAgentAdministration.listCirrusAgents();
        assertThat(cirrusAgents).isNotNull();
        assertTrue(cirrusAgents.isEmpty());
    }

    @Test(expected = CirrusAgentInstallationException.class)
    public void shouldErrorForInstallationOfBadBundle() throws Exception {
        final ICirrusAgentManager cirrusAgentAdministration = new CirrusAgentManager(this.context);
        cirrusAgentAdministration.start();

        final URL bundleURL = this.getClass().getResource("/badBundle.jar");

        cirrusAgentAdministration.installCirrusAgent(bundleURL.toExternalForm());
    }

    @Test(expected = CirrusAgentAlreadyExistException.class)
    public void shouldErrorForInstallationOfExistingBundle() throws Exception {
        final ICirrusAgentManager cirrusAgentAdministration = new CirrusAgentManager(this.context);
        cirrusAgentAdministration.start();

        final URL bundleURL = this.getClass().getResource("/bundle.jar");

        cirrusAgentAdministration.installCirrusAgent(bundleURL.toExternalForm());
        // install two times
        cirrusAgentAdministration.installCirrusAgent(bundleURL.toExternalForm());
    }

    @Test
    public void shouldGetAgentByIdAfterSuccessfullyInstallation() throws Exception {
        final ICirrusAgentManager cirrusAgentAdministration = new CirrusAgentManager(this.context);
        cirrusAgentAdministration.start();

        final URL bundleURL = this.getClass().getResource("/bundle.jar");

        final ICirrusAgent cirrusAgent = cirrusAgentAdministration.installCirrusAgent(bundleURL.toExternalForm());
        assertThat(cirrusAgent).isNotNull();

        assertThat(cirrusAgentAdministration.getCirrusAgentById(cirrusAgent.getIdentifier())).isNotNull();
    }

    @Test
    public void shouldInstallSuccessfullyANewBundle() throws Exception {
        final ICirrusAgentManager cirrusAgentAdministration = new CirrusAgentManager(this.context);
        cirrusAgentAdministration.start();

        final URL bundleURL = this.getClass().getResource("/bundle.jar");

        cirrusAgentAdministration.installCirrusAgent(bundleURL.toExternalForm());

        final List<ICirrusAgent> cirrusAgents = cirrusAgentAdministration.listCirrusAgents();
        assertThat(cirrusAgents.size()).isEqualTo(1);

        final ICirrusAgent cirrusAgent = cirrusAgents.get(0);
        final ICirrusAgentIdentifier cirrusAgentIdentifier = cirrusAgent.getIdentifier();
        assertThat(cirrusAgentIdentifier).isNotNull();

        final ICirrusAgentBundleDescription bundleDescription = cirrusAgent.getCirrusAgentBundleDescription();
        assertThat(bundleDescription).isNotNull();
        assertThat(bundleDescription.getName()).isEqualTo("Local Cirrus Bundle");
        assertThat(bundleDescription.getDescription()).isEqualTo("cirrus bundle based on local file system");
        assertThat(bundleDescription.getVersion()).isEqualTo("1.0.0.SNAPSHOT");
        assertThat(bundleDescription.getVendor()).isEqualTo("Antoine Jullien");

        final IStorageServiceVendor storageServiceVendor = cirrusAgent.getStorageServiceVendor();
        assertThat(storageServiceVendor).isNotNull();
        assertThat(storageServiceVendor.getName()).isEqualTo("local-cirrus-agent");
        assertThat(storageServiceVendor.getVendor()).isEqualTo("Local");
        assertThat(storageServiceVendor.getVersion()).isEqualTo("1.0.0");

        final ICirrusStorageService storageService = cirrusAgent.getStorageService();
        assertNotNull(storageService);
    }

    @Test(expected = CirrusAgentNotExistException.class)
    public void shouldErrorWhenUninstallNonExistingBundle() throws Exception {
        final ICirrusAgentManager cirrusAgentAdministration = new CirrusAgentManager(this.context);
        cirrusAgentAdministration.start();

        cirrusAgentAdministration.uninstallCirrusAgent(new UUIDBasedCirrusAgentIdentifier());
    }

    @Test
    public void shouldSuccessWhenUninstallExistingBundle() throws Exception {
        final ICirrusAgentManager cirrusAgentAdministration = new CirrusAgentManager(this.context);
        cirrusAgentAdministration.start();

        final URL bundleURL = this.getClass().getResource("/bundle.jar");

        cirrusAgentAdministration.installCirrusAgent(bundleURL.toExternalForm());
        final List<ICirrusAgent> cirrusAgents = cirrusAgentAdministration.listCirrusAgents();

        cirrusAgentAdministration.uninstallCirrusAgent(cirrusAgents.get(0).getIdentifier());

        final List<ICirrusAgent> newList = cirrusAgentAdministration.listCirrusAgents();
        assertTrue(newList.isEmpty());
    }

    @Test
    public void shouldHaveCorrectStatusAfterStoppingServer() throws Exception {
        final ICirrusAgentManager cirrusAgentAdministration = new CirrusAgentManager(this.context);
        cirrusAgentAdministration.start();

        assertTrue(cirrusAgentAdministration.isStarted());
        cirrusAgentAdministration.stop();
        Thread.sleep(100);
        assertFalse(cirrusAgentAdministration.isStarted());
    }

    @Test(expected = ServerNotStartedException.class)
    public void shouldHaveErrorWhenInstallBundleWithNotStartedServer() throws Exception {
        final ICirrusAgentManager cirrusAgentAdministration = new CirrusAgentManager(this.context);


        final URL bundleURL = this.getClass().getResource("/bundle.jar");

        cirrusAgentAdministration.installCirrusAgent(bundleURL.toExternalForm());
    }

    @Test(expected = ServerNotStartedException.class)
    public void shouldHaveErrorWhenUninstallBundleWithNotStartedServer() throws Exception {
        final ICirrusAgentManager cirrusAgentAdministration = new CirrusAgentManager(this.context);

        cirrusAgentAdministration.uninstallCirrusAgent(new UUIDBasedCirrusAgentIdentifier());
    }
}
