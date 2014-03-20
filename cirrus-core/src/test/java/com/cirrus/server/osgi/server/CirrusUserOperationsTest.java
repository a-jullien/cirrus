/*
 * *
 *  * Copyright (c) 2014 Antoine Jullien
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.cirrus.server.osgi.server;

import com.cirrus.data.ICirrusMetaData;
import com.cirrus.data.impl.DataType;
import com.cirrus.persistence.dao.meta.IMetaDataDAO;
import com.cirrus.persistence.service.MongoDBService;
import com.cirrus.server.configuration.CirrusProperties;
import com.cirrus.server.impl.CirrusAgentManager;
import com.cirrus.server.impl.CirrusUserOperationManager;
import com.cirrus.server.impl.GlobalContext;
import com.cirrus.utils.IOFileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.mongodb.util.MyAsserts.assertEquals;
import static com.mongodb.util.MyAsserts.assertTrue;
import static junit.framework.Assert.assertNotNull;

public class CirrusUserOperationsTest {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private CirrusUserOperationManager cirrusOperations;
    private IMetaDataDAO metaDataDAO;
    private File tmpDir;

    @Before
    public void setUp() throws Exception {
        this.tmpDir = IOFileUtils.getTmpDirectory();
        this.metaDataDAO = this.createMetadataDAO();

        final URL bundleURL = this.getClass().getResource("/bundle.jar");
        assertNotNull(bundleURL);

        final CirrusAgentManager cirrusAgentManager = new CirrusAgentManager(GlobalContext.create(this.tmpDir.getPath()));
        cirrusAgentManager.start();
        cirrusAgentManager.installCirrusAgent(bundleURL.toExternalForm());
        this.cirrusOperations = new CirrusUserOperationManager(cirrusAgentManager, this.metaDataDAO);
    }

    @After
    public void tearDown() {
        this.metaDataDAO.dropCollection();
        IOFileUtils.deleteDirectory(this.tmpDir);
    }

    @Test
    public void shouldCreateSuccessfullyANonExistingDirectory() throws ExecutionException {
        this.cirrusOperations.createDirectory("/A");
        final List<ICirrusMetaData> result = this.cirrusOperations.listCirrusData("/");
        assertNotNull(result);
        assertEquals(1, result.size());
        final ICirrusMetaData metaData = result.get(0);
        assertEquals("A", metaData.getName());
        assertEquals(DataType.DIRECTORY, metaData.getDataType());
        assertEquals("/", metaData.getVirtualPath());
        final File file = new File(this.tmpDir.getPath() + File.separatorChar + "A");
        assertTrue(file.exists());
        assertEquals(file.getPath(), metaData.getLocalPath());
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private IMetaDataDAO createMetadataDAO() throws IOException {
        // global properties of the cirrus server
        final CirrusProperties cirrusProperties = new CirrusProperties();
        // create mongodb service
        final MongoDBService mongoDBService = new MongoDBService(cirrusProperties.getProperty(CirrusProperties.MONGODB_URL));
        return mongoDBService.getMetaDataDAO();
    }
}
