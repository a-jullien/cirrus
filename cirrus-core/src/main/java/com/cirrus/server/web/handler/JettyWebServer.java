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

package com.cirrus.server.web.handler;


import com.cirrus.server.exception.StartWebServiceException;
import com.cirrus.server.exception.StopWebServiceException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class JettyWebServer {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final Server server;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public JettyWebServer(final int httpPort) {
        this.server = new Server(httpPort);
        final ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("");
        handler.addServlet(new ServletHolder(new ServletContainer(this.createResourceConfig())), "/*");
        this.server.setHandler(handler);
    }

    public void start() throws StartWebServiceException {
        try {
            this.server.start();
        } catch (final Exception e) {
            throw new StartWebServiceException(e);
        }
    }

    public void stop() throws StopWebServiceException {
        try {
            this.server.stop();
        } catch (final Exception e) {
            throw new StopWebServiceException(e);
        }
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private ResourceConfig createResourceConfig() {
        final ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(new WebApplicationBinder());
        return resourceConfig.packages("com.cirrus.server.web.resources");
    }

    public static void main(final String[] args) throws StartWebServiceException {
        final JettyWebServer jettyWebServer = new JettyWebServer(8080);
        jettyWebServer.start();
    }
}
