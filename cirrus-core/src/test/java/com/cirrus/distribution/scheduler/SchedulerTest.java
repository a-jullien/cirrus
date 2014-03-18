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

package com.cirrus.distribution.scheduler;

import com.cirrus.agent.ICirrusAgent;
import com.cirrus.data.impl.CirrusFolderData;
import com.cirrus.distribution.scheduler.action.CreateDirectoryAction;
import com.cirrus.server.configuration.CirrusProperties;
import com.cirrus.server.exception.*;
import com.cirrus.server.impl.CirrusAgentManager;
import com.cirrus.utils.IOFileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.mongodb.util.MyAsserts.*;

public class SchedulerTest {

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private CirrusAgentManager cirrusAgentAdministration;
    private File tmpDirectory;


    @Before
    public void setUp() throws Exception {
        this.tmpDirectory = IOFileUtils.getTmpDirectory();

        // global properties of the cirrus server
        final CirrusProperties cirrusProperties = new CirrusProperties();
        // create mongodb service

        this.cirrusAgentAdministration = new CirrusAgentManager();
        this.cirrusAgentAdministration.start();

        final URL bundleURL = this.getClass().getResource("/bundle.jar");
        this.cirrusAgentAdministration.installCirrusAgent(bundleURL.toExternalForm());
        final List<ICirrusAgent> cirrusAgents = this.cirrusAgentAdministration.listCirrusAgents();
        assertEquals(1, cirrusAgents.size());
    }

    @After
    public void tearDown() throws StopCirrusServerException {
        try {
            this.cirrusAgentAdministration.stop();
        } finally {
            IOFileUtils.deleteDirectory(this.tmpDirectory);
        }
    }

    @Test(expected = ExecutionException.class)
    public void shouldHaveErrorCreatedDirectoryFromActionWithAlreadyExist() throws CirrusAgentAlreadyExistException, ServerNotStartedException, StartCirrusAgentException, CirrusAgentInstallationException, ExecutionException {
        final File file = new File(this.tmpDirectory, "hurt");
        assertTrue(file.mkdir());

        final CirrusScheduler cirrusScheduler = new CirrusScheduler(this.cirrusAgentAdministration);
        final CreateDirectoryAction action = new CreateDirectoryAction(cirrusScheduler.findAgent(), file.getPath());
        final CirrusFolderData createdFolder = cirrusScheduler.scheduleAction(action);
        assertNotNull(createdFolder);

        assertEquals("/tmp/hurt", createdFolder.getPath());

        assertTrue(IOFileUtils.deleteDirectory(file));
    }

    @Test
    public void shouldHaveSuccessfullyCreatedDirectoryFromAction() throws CirrusAgentAlreadyExistException, ServerNotStartedException, StartCirrusAgentException, CirrusAgentInstallationException, ExecutionException {
        final File file = new File(this.tmpDirectory, "hurt");

        final CirrusScheduler cirrusScheduler = new CirrusScheduler(this.cirrusAgentAdministration);
        final CreateDirectoryAction action = new CreateDirectoryAction(cirrusScheduler.findAgent(), file.getPath());
        final CirrusFolderData createdFolder = cirrusScheduler.scheduleAction(action);
        assertNotNull(createdFolder);

        assertEquals("cirrus" + file.getAbsolutePath(), createdFolder.getPath());

        IOFileUtils.deleteDirectory(file);
    }
}
