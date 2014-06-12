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

package com.cirrus.server.http.resources;

import com.cirrus.agent.ICirrusAgentBundleDescription;
import com.cirrus.agent.impl.CirrusAgentBundleDescription;
import com.cirrus.server.http.entity.CirrusAgents;
import com.cirrus.server.http.entity.CirrusServerInformation;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import static com.mongodb.util.MyAsserts.assertNotNull;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("unchecked")
public class CirrusServerManagementServiceTest extends AbstractJerseyTest {

    @Test
    public void shouldErrorWhenTryToListAgentsWhenServerIsNotStarted() {
        final WebTarget webTargetForPath = super.getWebTargetFor("admin", "agents");
        final Response response = webTargetForPath.request().accept(MediaType.APPLICATION_JSON).get();
        assertNotNull(response);
        assertEquals(406, response.getStatus());
    }

    @Test
    public void shouldCheckStatusWhenServerNotStarted() {
        final WebTarget webTargetForPath = super.getWebTargetFor("admin");
        final CirrusServerInformation serverInformation = webTargetForPath.request().accept(MediaType.APPLICATION_JSON).get(CirrusServerInformation.class);
        assertNotNull(serverInformation);
        assertEquals("Atlantis", serverInformation.getName());
        assertEquals(CirrusServerInformation.STATUS.STOPPED, serverInformation.getStatus());
    }

    @Test
    public void shouldSuccessfullyStartServer() {
        final CirrusServerInformation serverInformation = startServer();
        assertNotNull(serverInformation);
        assertEquals("Atlantis", serverInformation.getName());
        assertEquals(CirrusServerInformation.STATUS.STARTED, serverInformation.getStatus());
    }

    @Test
    public void shouldSuccessfullyStopServer() {
        // start first
        final CirrusServerInformation serverInformationAfterStart = startServer();
        assertNotNull(serverInformationAfterStart);
        assertEquals("Atlantis", serverInformationAfterStart.getName());
        assertEquals(CirrusServerInformation.STATUS.STARTED, serverInformationAfterStart.getStatus());

        // stop server
        final CirrusServerInformation serverInformationAfterStop = stopServer();
        assertNotNull(serverInformationAfterStop);
        assertEquals("Atlantis", serverInformationAfterStop.getName());
        assertEquals(CirrusServerInformation.STATUS.STOPPED, serverInformationAfterStop.getStatus());
    }

    @Test
    public void shouldErrorWhenInstallBundleWithNonStartedServer() throws IOException {
        final URL resource = this.getClass().getResource("/bundle.jar");
        final WebTarget startWebTargetForPath = super.getWebTargetFor("admin", "agents");
        try (final InputStream stream = resource.openStream()) {
            final Entity<InputStream> entity = Entity.entity(stream, MediaType.APPLICATION_OCTET_STREAM);
            final Response post = startWebTargetForPath.request().header("name", "myBundle").accept(MediaType.APPLICATION_OCTET_STREAM).post(entity);

            assertNotNull(post);
            assertEquals(406, post.getStatus());
        }
    }

    @Test
    public void shouldSuccessWhenInstallBundle() throws IOException {
        this.startServer();

        final URL resource = this.getClass().getResource("/bundle.jar");
        final WebTarget startWebTargetForPath = super.getWebTargetFor("admin", "agents");
        try (final InputStream stream = resource.openStream()) {
            final Invocation.Builder builder = startWebTargetForPath.request();
            final Response response = builder.
                    header("name", "myBundle").
                    method("POST", Entity.entity(stream, MediaType.APPLICATION_OCTET_STREAM));

            assertNotNull(response);
            assertEquals(204, response.getStatus());
        }

        final CirrusAgents agents = this.listAgents();
        assertNotNull(agents);
        final List<CirrusAgentBundleDescription> bundleDescriptions = agents.getAgents();
        assertEquals(1, bundleDescriptions.size());
        final ICirrusAgentBundleDescription cirrusAgentBundleDescription = bundleDescriptions.get(0);
        assertEquals("Dropbox Cirrus Bundle", cirrusAgentBundleDescription.getName());
        assertEquals("Dropbox integration", cirrusAgentBundleDescription.getDescription());
        assertEquals("Antoine Jullien", cirrusAgentBundleDescription.getVendor());
        assertEquals("1.0.0.SNAPSHOT", cirrusAgentBundleDescription.getVersion());
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================

    private CirrusAgents listAgents() {
        final WebTarget target = super.getWebTargetFor("admin", "agents");
        return target.request().accept(MediaType.APPLICATION_JSON).get(CirrusAgents.class);
    }

    private CirrusServerInformation startServer() {
        final WebTarget target = super.getWebTargetFor("test", "admin", "start");
        return target.request().accept(MediaType.APPLICATION_JSON).get(CirrusServerInformation.class);
    }

    private CirrusServerInformation stopServer() {
        final WebTarget target = super.getWebTargetFor("test", "admin", "stop");
        return target.request().accept(MediaType.APPLICATION_JSON).get(CirrusServerInformation.class);
    }
}
