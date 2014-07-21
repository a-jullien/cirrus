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

package com.cirrus.server.osgi.service.amazon.s3;

import com.cirrus.agent.authentication.impl.AccessKeyPasswordTrustedToken;
import com.cirrus.data.ICirrusData;
import com.cirrus.data.impl.CirrusFolderData;
import com.cirrus.server.osgi.extension.ServiceRequestFailedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.mongodb.util.MyAsserts.assertTrue;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class AmazonS3StorageServiceTest {

    //==================================================================================================================
    // Constants
    //==================================================================================================================

    private static final String AMAZON_ACCESS_KEY_PROPERTY = "amazon.access.key";
    private static final String AMAZON_ACCESS_PASSWORD_PROPERTY = "amazon.access.password";

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    private AmazonS3StorageService amazonS3StorageService;

    @Before
    public void setUp() {
        this.amazonS3StorageService = new AmazonS3StorageService();
        final String accessKey = "AKIAIVUAS3RS6CFFFDVA";//System.getProperty(AMAZON_ACCESS_KEY_PROPERTY);
        final String accessPassword = "iU6og3eAvxpzMTI8X0oGDM/JDbCn602lcOxRIVjH";//System.getProperty(AMAZON_ACCESS_PASSWORD_PROPERTY);
        assertNotNull(accessKey);
        assertNotNull(accessPassword);

        final AccessKeyPasswordTrustedToken trustedToken = new AccessKeyPasswordTrustedToken(accessKey, accessPassword);
        this.amazonS3StorageService.authenticate(trustedToken);
    }

    @After
    public void tearDown() {
        this.amazonS3StorageService.shutdown();
    }

    @Test
    public void shouldHaveACorrectAccountName() throws ServiceRequestFailedException {
        final String accountName = this.amazonS3StorageService.getAccountName();
        assertNotNull(accountName);
        assertEquals("antoine.jullien", accountName);
    }

    @Test
    public void shouldCreateSuccessfullyFolder() throws ServiceRequestFailedException {
        final CirrusFolderData directory = this.amazonS3StorageService.createDirectory("test1");
        assertNotNull(directory);
    }

    @Test
    public void shouldHaveAnEmptyContent() throws ServiceRequestFailedException {
        final List<ICirrusData> list = this.amazonS3StorageService.list("/test1");
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }
}
