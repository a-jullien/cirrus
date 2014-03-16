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

package com.cirrus.osgi.service.local;

import com.cirrus.data.ICirrusData;
import com.cirrus.data.impl.CirrusFileData;
import com.cirrus.data.impl.CirrusFolderData;
import com.cirrus.osgi.agent.authentication.impl.AnonymousTrustedToken;
import com.cirrus.osgi.extension.ServiceRequestFailedException;
import com.cirrus.utils.IOFileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.mongodb.util.MyAsserts.assertFalse;
import static com.mongodb.util.MyAsserts.assertTrue;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class LocalStorageServiceTest {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private File cirrusRootFile;

    @Before
    public void setUp() {
        final File tmpDir = IOFileUtils.getTmpDir();
        this.cirrusRootFile = new File(tmpDir, "cirrus");
        assertTrue(this.cirrusRootFile.mkdir());
    }

    @After
    public void tearDown() {
        IOFileUtils.deleteDirectory(this.cirrusRootFile);
    }

    @Test
    public void shouldCreateSuccessfullyANewDirectory() throws ServiceRequestFailedException {
        final LocalStorageService localStorageService = createLocalStorageService();

        final File directory = new File(this.cirrusRootFile, "testDirectory");
        final CirrusFolderData createdCirrusFolder = localStorageService.createDirectory(directory.getPath());
        assertNotNull(createdCirrusFolder);
        assertEquals(createdCirrusFolder.getName(), "testDirectory");
        assertEquals(createdCirrusFolder.getPath(), directory.getPath());
    }

    @Test
    public void shouldDeleteSuccessfullyExistingDirectory() throws ServiceRequestFailedException {
        final LocalStorageService localStorageService = createLocalStorageService();

        final File directory = new File(this.cirrusRootFile, "testDirectory");
        assertTrue(directory.mkdir());
        final ICirrusData delete = localStorageService.delete(directory.getPath());
        assertNotNull(delete);
        assertEquals(delete.getName(), "testDirectory");
        assertEquals(delete.getPath(), directory.getPath());
        assertTrue(delete instanceof CirrusFolderData);
    }

    @Test
    public void shouldDeleteSuccessfullyExistingFile() throws ServiceRequestFailedException, IOException {
        final LocalStorageService localStorageService = createLocalStorageService();

        final File file = new File(this.cirrusRootFile, "testFile");
        assertTrue(file.createNewFile());
        final ICirrusData delete = localStorageService.delete(file.getPath());
        assertNotNull(delete);
        assertEquals(delete.getName(), "testFile");
        assertEquals(delete.getPath(), file.getPath());
        assertTrue(delete instanceof CirrusFileData);
    }

    @Test(expected = ServiceRequestFailedException.class)
    public void shouldHaveErrorWhenDeleteNonExistingDirectory() throws ServiceRequestFailedException {
        final LocalStorageService localStorageService = createLocalStorageService();

        final File directory = new File(this.cirrusRootFile, "nonExistingDirectory");
        localStorageService.delete(directory.getPath());
    }

    @Test(expected = ServiceRequestFailedException.class)
    public void shouldHaveErrorWhenDeleteNonExistingFile() throws ServiceRequestFailedException {
        final LocalStorageService localStorageService = createLocalStorageService();

        final File directory = new File(this.cirrusRootFile, "nonExistingFile");
        localStorageService.delete(directory.getPath());
    }

    @Test
    public void shouldSuccessfullyTransferFile() throws IOException, ServiceRequestFailedException {
        final LocalStorageService localStorageService = createLocalStorageService();
        final File sourceFile = new File(this.cirrusRootFile, "sourceFile");
        assertTrue(sourceFile.createNewFile());

        final File destinationFile = new File(this.cirrusRootFile, "destinationFile");
        assertFalse(destinationFile.exists());

        try (FileInputStream inputStream = new FileInputStream(sourceFile)) {
            final CirrusFileData cirrusFileData = localStorageService.transferFile(destinationFile.getPath(), 0, inputStream);
            assertNotNull(cirrusFileData);
            assertEquals("destinationFile", destinationFile.getName());
        }
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private LocalStorageService createLocalStorageService() {
        final LocalStorageService localStorageService = new LocalStorageService();
        localStorageService.authenticate(new AnonymousTrustedToken());
        return localStorageService;
    }
}
