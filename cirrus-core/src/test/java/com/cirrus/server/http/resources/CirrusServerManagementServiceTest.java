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
import com.cirrus.model.authentication.Token;
import com.cirrus.model.authentication.impl.LoginPasswordCredentials;
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
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
public class CirrusServerManagementServiceTest extends AbstractJerseyTest {

    @Test
    public void shouldErrorWhenTryToListAgentsWhenServerIsNotStarted() {
        final Token token = getToken();

        final WebTarget webTargetForPath = super.getWebTargetFor("admin", "agents");
        final Response response = webTargetForPath.request().
                header("Authorization", token.getTokenValue()).
                accept(MediaType.APPLICATION_JSON).get();
        assertNotNull(response);
        assertThat(response.getStatus()).isEqualTo(406);
    }

    @Test
    public void shouldCheckStatusWhenServerNotStarted() {
        final Token token = getToken();

        final WebTarget webTargetForPath = super.getWebTargetFor("admin");
        final CirrusServerInformation serverInformation = webTargetForPath.request().
                header("Authorization", token.getTokenValue()).
                accept(MediaType.APPLICATION_JSON).get(CirrusServerInformation.class);
        assertThat(serverInformation).isNotNull();
        assertThat(serverInformation.getName()).isEqualTo("Atlantis");
        assertThat(serverInformation.getStatus()).isEqualTo(CirrusServerInformation.STATUS.STOPPED);
    }

    @Test
    public void shouldSuccessfullyStartServer() {
        getToken();
        final CirrusServerInformation serverInformation = startServer();
        assertThat(serverInformation).isNotNull();
        assertThat(serverInformation.getName()).isEqualTo("Atlantis");
        assertThat(serverInformation.getStatus()).isEqualTo(CirrusServerInformation.STATUS.STARTED);
    }

    @Test
    public void shouldSuccessfullyStopServer() {
        getToken();
        // start first
        final CirrusServerInformation serverInformationAfterStart = startServer();
        assertThat(serverInformationAfterStart).isNotNull();
        assertThat(serverInformationAfterStart.getName()).isEqualTo("Atlantis");
        assertThat(serverInformationAfterStart.getStatus()).isEqualTo(CirrusServerInformation.STATUS.STARTED);

        // stop server
        final CirrusServerInformation serverInformationAfterStop = stopServer();
        assertThat(serverInformationAfterStop).isNotNull();
        assertThat(serverInformationAfterStop.getName()).isEqualTo("Atlantis");
        assertThat(serverInformationAfterStop.getStatus()).isEqualTo(CirrusServerInformation.STATUS.STOPPED);
    }

    @Test
    public void shouldErrorWhenInstallBundleWithNonStartedServer() throws IOException {
        getToken();
        final URL resource = this.getClass().getResource("/bundle.jar");
        final WebTarget startWebTargetForPath = super.getWebTargetFor("admin", "agents");
        try (final InputStream stream = resource.openStream()) {
            final Entity<InputStream> entity = Entity.entity(stream, MediaType.APPLICATION_OCTET_STREAM);
            final Response post = startWebTargetForPath.request().header("name", "myBundle").accept(MediaType.APPLICATION_OCTET_STREAM).post(entity);

            assertThat(post).isNotNull();
            assertThat(post.getStatus()).isEqualTo(406);
        }
    }

    @Test
    public void shouldSuccessWhenInstallBundle() throws IOException {
        final Token token = getToken();
        this.startServer();

        final URL resource = this.getClass().getResource("/bundle.jar");
        final WebTarget startWebTargetForPath = super.getWebTargetFor("admin", "agents");
        try (final InputStream stream = resource.openStream()) {
            final Invocation.Builder builder = startWebTargetForPath.request();
            final Response response = builder.
                    header("Authorization", token.getTokenValue()).
                    header("name", "myBundle").
                    method("POST", Entity.entity(stream, MediaType.APPLICATION_OCTET_STREAM));

            assertThat(response).isNotNull();
            assertThat(response.getStatus()).isEqualTo(204);
        }

        final CirrusAgents agents = this.listAgents();
        assertThat(agents).isNotNull();
        final List<ICirrusAgentBundleDescription> bundleDescriptions = agents.getAgents();
        assertThat(bundleDescriptions.size()).isEqualTo(1);
        final ICirrusAgentBundleDescription cirrusAgentBundleDescription = bundleDescriptions.get(0);
        assertThat(cirrusAgentBundleDescription.getName()).isEqualTo("Local Cirrus Bundle");
        assertThat(cirrusAgentBundleDescription.getDescription()).isEqualTo("cirrus bundle based on local file system");
        assertThat(cirrusAgentBundleDescription.getVendor()).isEqualTo("Antoine Jullien");
        assertThat(cirrusAgentBundleDescription.getVersion()).isEqualTo("1.0.0.SNAPSHOT");
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================

    private CirrusAgents listAgents() {
        final Token token = this.getToken();
        final WebTarget target = super.getWebTargetFor("admin", "agents");
        return target.request().
                header("Authorization", token.getTokenValue()).
                accept(MediaType.APPLICATION_JSON).get(CirrusAgents.class);
    }

    private CirrusServerInformation startServer() {
        final Token token = this.getToken();
        final WebTarget target = super.getWebTargetFor("test", "admin", "start");
        return target.request().
                header("Authorization", token.getTokenValue()).
                accept(MediaType.APPLICATION_JSON).get(CirrusServerInformation.class);
    }

    private CirrusServerInformation stopServer() {
        final Token token = this.getToken();
        final WebTarget target = super.getWebTargetFor("test", "admin", "stop");
        return target.request().
                header("Authorization", token.getTokenValue()).
                accept(MediaType.APPLICATION_JSON).get(CirrusServerInformation.class);
    }

    private Token getToken() {
        final WebTarget webTargetForPath = super.getWebTargetFor("authentication");

        final LoginPasswordCredentials credentials = new LoginPasswordCredentials("admin@admin.com", "admin");
        return webTargetForPath.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(credentials, MediaType.APPLICATION_JSON), Token.class);
    }
}
