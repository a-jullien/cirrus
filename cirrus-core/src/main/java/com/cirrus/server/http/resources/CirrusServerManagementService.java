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


import com.cirrus.agent.ICirrusAgent;
import com.cirrus.agent.ICirrusAgentBundleDescription;
import com.cirrus.agent.impl.UUIDBasedCirrusAgentIdentifier;
import com.cirrus.server.exception.*;
import com.cirrus.server.http.entity.CirrusAgents;
import com.cirrus.server.http.entity.CirrusServerInformation;
import com.cirrus.server.http.verb.START;
import com.cirrus.server.http.verb.STOP;
import com.cirrus.server.impl.OSGIBasedCirrusServer;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
@Path("/admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CirrusServerManagementService {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    @Inject
    private OSGIBasedCirrusServer cirrusServer;

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Path("/agents")
    @GET
    public CirrusAgents getInstalledAgents() throws ServerNotStartedException {
        final CirrusAgents agents = new CirrusAgents();
        final List<ICirrusAgent> cirrusAgents = this.cirrusServer.getCirrusAgentManager().listCirrusAgents();
        for (final ICirrusAgent cirrusAgent : cirrusAgents) {
            final ICirrusAgentBundleDescription agentDescription = cirrusAgent.getCirrusAgentBundleDescription();
            agents.addAgent((com.cirrus.agent.impl.CirrusAgentBundleDescription) agentDescription); // TODO remove cast by JsonDeserialize
        }

        return agents;
    }

    @Path("/agents")
    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public void installNewAgent(@HeaderParam("name") final String name,
                                final InputStream inputStream) throws CirrusAgentAlreadyExistException, ServerNotStartedException, StartCirrusAgentException, CirrusAgentInstallationException {
        this.cirrusServer.getCirrusAgentManager().installCirrusAgent(name, inputStream);
    }

    @DELETE
    @Path("/agents/{agentId}")
    public void uninstallExistingAgent(@PathParam("agentId") final String agentId) throws CirrusAgentNotExistException, UninstallCirrusAgentException, ServerNotStartedException, StopCirrusAgentException {
        this.cirrusServer.getCirrusAgentManager().uninstallCirrusAgent(new UUIDBasedCirrusAgentIdentifier(agentId));
    }

    @START
    public CirrusServerInformation start() throws StartCirrusServerException {
        this.cirrusServer.start();
        return this.getCirrusServerStatus();
    }

    @STOP
    public CirrusServerInformation stop() throws StopCirrusServerException {
        this.cirrusServer.stop();
        return this.getCirrusServerStatus();
    }

    @GET
    public CirrusServerInformation getCirrusServerStatus() {
        final boolean started = this.cirrusServer.isStarted();
        final CirrusServerInformation.STATUS status = started ?
                CirrusServerInformation.STATUS.STARTED : CirrusServerInformation.STATUS.STOPPED;
        return new CirrusServerInformation(this.cirrusServer.getName(), status);
    }
}
