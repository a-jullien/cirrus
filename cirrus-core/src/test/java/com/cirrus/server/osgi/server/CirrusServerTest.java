/**
 * Copyright (c) 2014 Antoine Jullien
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cirrus.server.osgi.server;

import com.cirrus.server.ICirrusServer;
import com.cirrus.server.exception.StartCirrusServerException;
import com.cirrus.server.impl.OSGIBasedCirrusServer;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertNotNull;

public class CirrusServerTest {

    @Test
    public void shouldHaveCorrectInitializedObjectsAfterStartCirrusServer() throws IOException, StartCirrusServerException {
        final ICirrusServer cirrusServer = new OSGIBasedCirrusServer();
        cirrusServer.start();

        assertNotNull(cirrusServer.getCirrusAgentAdministration());
        assertNotNull(cirrusServer.getMetaDataProvider());
    }
}
