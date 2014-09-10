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

import com.cirrus.model.authentication.ICredentials;
import com.cirrus.model.authentication.Token;
import com.cirrus.server.http.client.ClientService;
import com.cirrus.server.http.client.ClientServiceFactory;
import com.cirrus.server.http.handler.JacksonJaxbContextResolver;
import com.cirrus.server.http.handler.JettyWebServer;
import com.cirrus.server.http.handler.WebApplicationBinder;
import com.cirrus.server.osgi.extension.AuthenticationException;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import java.io.IOException;

import static org.mockito.Mockito.mock;

public class AbstractJerseyTest extends JerseyTest {

    @Override
    protected Application configure() {
        final ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages(true, JettyWebServer.RESOURCES_PACKAGE, JettyWebServer.EXCEPTION_MAPPER_PACKAGE);
        resourceConfig.register(new JacksonJaxbContextResolver());
        resourceConfig.register(new WebApplicationBinder());
        resourceConfig.register(JacksonFeature.class);
        resourceConfig.register(MultiPartFeature.class);
        return resourceConfig;
    }


    protected WebTarget getWebTargetFor(final String... paths) {
        final Client client = ClientBuilder.newClient();
        client.register(new JacksonJaxbContextResolver());
        client.register(new WebApplicationBinder());
        client.register(JacksonFeature.class);
        client.register(MultiPartFeature.class);
        WebTarget target = client.target(getBaseUri());
        for (final String path : paths) {
            target = target.path(path);
        }
        return target;
    }
}
