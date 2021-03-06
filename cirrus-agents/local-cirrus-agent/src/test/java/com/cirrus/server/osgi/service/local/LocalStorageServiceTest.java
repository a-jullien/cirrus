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

package com.cirrus.server.osgi.service.local;

import com.cirrus.agent.authentication.impl.AnonymousAuthenticator;
import com.cirrus.model.data.ICirrusData;
import com.cirrus.model.data.impl.CirrusFileData;
import com.cirrus.model.data.impl.CirrusFolderData;
import com.cirrus.model.data.impl.DataType;
import com.cirrus.server.IGlobalContext;
import com.cirrus.server.impl.GlobalContext;
import com.cirrus.server.osgi.extension.ServiceRequestFailedException;
import com.cirrus.utils.IOFileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static com.mongodb.util.MyAsserts.assertFalse;
import static com.mongodb.util.MyAsserts.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

public class LocalStorageServiceTest {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private File tmpDirectory;
    private IGlobalContext context;

    @Before
    public void setUp() throws IOException {
        this.tmpDirectory = IOFileUtils.getTmpDirectory();
        this.context = GlobalContext.create(this.tmpDirectory.getPath());
        assertTrue(this.tmpDirectory.exists());
    }

    @After
    public void tearDown() {
        assertTrue(IOFileUtils.deleteDirectory(this.tmpDirectory));
        assertFalse(this.tmpDirectory.exists());
    }

    @Test(expected = ServiceRequestFailedException.class)
    public void shouldErrorWhenCreateExistingDirectory() throws ServiceRequestFailedException {
        final LocalStorageService localStorageService = createLocalStorageService();
        assertTrue(new File(this.tmpDirectory, "testDirectory").mkdirs());
        localStorageService.createDirectory("/testDirectory");
    }

    @Test
    public void shouldCreateSuccessfullyANewDirectory() throws ServiceRequestFailedException {
        final LocalStorageService localStorageService = createLocalStorageService();

        final File directory = new File(this.tmpDirectory, "testDirectory");
        final CirrusFolderData createdCirrusFolder = localStorageService.createDirectory("/testDirectory");
        assertThat(createdCirrusFolder).isNotNull();
        assertThat(createdCirrusFolder.getName()).isEqualTo("testDirectory");
        assertThat(createdCirrusFolder.getPath()).isEqualTo(directory.getPath());
    }

    @Test
    public void shouldDeleteSuccessfullyExistingDirectory() throws ServiceRequestFailedException {
        final LocalStorageService localStorageService = createLocalStorageService();

        final File directory = new File(this.tmpDirectory, "testDirectory");
        assertTrue(directory.mkdir());
        final ICirrusData delete = localStorageService.delete("/testDirectory");
        assertThat(delete).isNotNull();
        assertThat(delete.getName()).isEqualTo("testDirectory");
        assertThat(delete.getPath()).isEqualTo(directory.getPath());
        assertTrue(delete instanceof CirrusFolderData);
    }

    @Test
    public void shouldDeleteSuccessfullyExistingFile() throws ServiceRequestFailedException, IOException {
        final LocalStorageService localStorageService = createLocalStorageService();

        final File file = new File(this.tmpDirectory, "testFile");
        assertTrue(file.createNewFile());
        final ICirrusData delete = localStorageService.delete("/testFile");
        assertThat(delete).isNotNull();
        assertThat(delete.getName()).isEqualTo("testFile");
        assertThat(delete.getPath()).isEqualTo(file.getPath());
        assertThat(delete).isExactlyInstanceOf(CirrusFileData.class);
    }

    @Test(expected = ServiceRequestFailedException.class)
    public void shouldHaveErrorWhenDeleteNonExistingDirectory() throws ServiceRequestFailedException {
        final LocalStorageService localStorageService = createLocalStorageService();

        final File directory = new File(this.tmpDirectory, "nonExistingDirectory");
        localStorageService.delete(directory.getPath());
    }

    @Test(expected = ServiceRequestFailedException.class)
    public void shouldHaveErrorWhenDeleteNonExistingFile() throws ServiceRequestFailedException {
        final LocalStorageService localStorageService = createLocalStorageService();

        final File directory = new File(this.tmpDirectory, "nonExistingFile");
        localStorageService.delete(directory.getPath());
    }

    @Test
    public void shouldSuccessfullyTransferFile() throws IOException, ServiceRequestFailedException {
        final LocalStorageService localStorageService = createLocalStorageService();
        final File sourceFile = new File(this.tmpDirectory, "sourceFile");
        assertTrue(sourceFile.createNewFile());

        final File destinationFile = new File("destinationFile");
        assertFalse(destinationFile.exists());

        try (FileInputStream inputStream = new FileInputStream(sourceFile)) {
            final CirrusFileData cirrusFileData = localStorageService.transferFile(destinationFile.getPath(), 0, inputStream);
            assertThat(cirrusFileData).isNotNull();
            assertThat(destinationFile.getName()).isEqualTo("destinationFile");
        }
    }

    @Test(expected = ServiceRequestFailedException.class)
    public void shouldErrorWhenListNonExistingContent() throws ServiceRequestFailedException {
        final LocalStorageService localStorageService = this.createLocalStorageService();
        localStorageService.list("/nonExisting");
    }

    @Test(expected = ServiceRequestFailedException.class)
    public void shouldErrorWhenListFile() throws ServiceRequestFailedException, IOException {
        final LocalStorageService localStorageService = this.createLocalStorageService();
        assertTrue(new File(this.tmpDirectory, "file1").createNewFile());

        localStorageService.list("/file1");
    }

    @Test
    public void shouldSuccessfullyListContent() throws ServiceRequestFailedException {
        final LocalStorageService localStorageService = this.createLocalStorageService();
        final List<ICirrusData> emptyList = localStorageService.list("/");
        assertThat(emptyList).isNotNull();
        assertThat(emptyList.size()).isEqualTo(0);

        localStorageService.createDirectory("/directory1");

        final List<ICirrusData> newList = localStorageService.list("/");
        assertThat(newList.size()).isEqualTo(1);
        final ICirrusData data = newList.get(0);
        assertThat(data.getDataType()).isEqualTo(DataType.DIRECTORY);
        assertThat(data.getName()).isEqualTo("directory1");
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private LocalStorageService createLocalStorageService() {
        final LocalStorageService localStorageService = new LocalStorageService();
        localStorageService.authenticate(new AnonymousAuthenticator());
        localStorageService.initialize(this.context);
        return localStorageService;
    }
}
