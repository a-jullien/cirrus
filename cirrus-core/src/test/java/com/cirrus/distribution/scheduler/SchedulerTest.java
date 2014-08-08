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
import com.cirrus.distribution.scheduler.exception.CirrusAgentCannotBeFoundException;
import com.cirrus.server.IGlobalContext;
import com.cirrus.server.exception.*;
import com.cirrus.server.impl.CirrusAgentManager;
import com.cirrus.server.impl.GlobalContext;
import com.cirrus.utils.IOFileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.mongodb.util.MyAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SchedulerTest {

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private CirrusAgentManager cirrusAgentAdministration;
    private File tmpDirectory;
    private IGlobalContext globalContext;


    @Before
    public void setUp() throws Exception {
        this.tmpDirectory = IOFileUtils.getTmpDirectory();
        this.globalContext = GlobalContext.create(tmpDirectory.getPath());
        this.cirrusAgentAdministration = new CirrusAgentManager(this.globalContext);
        this.cirrusAgentAdministration.start();

        final URL bundleURL = this.getClass().getResource("/bundle.jar");
        this.cirrusAgentAdministration.installCirrusAgent(bundleURL.toExternalForm());
        final List<ICirrusAgent> cirrusAgents = this.cirrusAgentAdministration.listCirrusAgents();
        assertEquals(1, cirrusAgents.size());
    }

    @After
    public void tearDown() throws StopCirrusServerException {
        this.cirrusAgentAdministration.stop();
        IOFileUtils.deleteDirectory(this.tmpDirectory);
    }

    @Test(expected = CirrusAgentCannotBeFoundException.class)
    public void shouldHaveErrorWhenSchedulerHasNoAgents() throws Exception {
        final CirrusAgentManager emptyAgentManager = new CirrusAgentManager(this.globalContext);
        emptyAgentManager.start();

        final CirrusScheduler cirrusScheduler = new CirrusScheduler(emptyAgentManager);
        cirrusScheduler.findAgent();
    }

    @Test(expected = ExecutionException.class)
    public void shouldHaveErrorCreatedDirectoryFromActionWithAlreadyExist() throws CirrusAgentAlreadyExistException, ServerNotStartedException, StartCirrusAgentException, CirrusAgentInstallationException, ExecutionException, CirrusAgentCannotBeFoundException {
        final File directoryFile = new File(this.globalContext.getRootPath(), "hurt");
        assertTrue(directoryFile.mkdir());

        final CirrusScheduler cirrusScheduler = new CirrusScheduler(this.cirrusAgentAdministration);
        final CreateDirectoryAction action = new CreateDirectoryAction(cirrusScheduler.findAgent(), "/hurt");
        cirrusScheduler.scheduleAction(action);
    }

    @Test
    public void shouldHaveSuccessfullyCreatedDirectoryFromAction() throws CirrusAgentAlreadyExistException, ServerNotStartedException, StartCirrusAgentException, CirrusAgentInstallationException, ExecutionException, CirrusAgentCannotBeFoundException {
        final File directoryFile = new File(this.globalContext.getRootPath(), "hurt");
        final CirrusScheduler cirrusScheduler = new CirrusScheduler(this.cirrusAgentAdministration);
        final CreateDirectoryAction action = new CreateDirectoryAction(cirrusScheduler.findAgent(), "/hurt");
        final CirrusFolderData createdFolder = cirrusScheduler.scheduleAction(action);
        assertThat(createdFolder).isNotNull();

        assertTrue(directoryFile.exists());
        assertThat(directoryFile.getPath()).isEqualTo(createdFolder.getPath());
    }
}
