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

package com.cirrus.server.web.resources;


import com.cirrus.server.exception.StartCirrusServerException;
import com.cirrus.server.exception.StopCirrusServerException;
import com.cirrus.server.impl.OSGIBasedCirrusServer;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@SuppressWarnings("UnusedDeclaration")
@Path("/admin")
public class CirrusServerManagementService {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    private OSGIBasedCirrusServer osgiBasedCirrusServer;

    @Inject
    public void setOSGIBasedServer(final OSGIBasedCirrusServer osgiBasedCirrusServer) {
        this.osgiBasedCirrusServer = osgiBasedCirrusServer;
    }

    @GET
    @Path("start")
    public String start() throws StartCirrusServerException {
        this.osgiBasedCirrusServer.start();
        return this.getCirrusServerStatus();
    }

    @GET
    @Path("stop")
    public String stop() throws StopCirrusServerException {
        this.osgiBasedCirrusServer.stop();
        return this.getCirrusServerStatus();
    }

    @GET
    @Path("status")
    @Produces("text/plain")
    public String getCirrusServerStatus() {
        final boolean started = this.osgiBasedCirrusServer.getCirrusAgentManager().isStarted();
        return "Cirrus server status:" + (started ? "STARTED": "STOPPED");
    }
}
