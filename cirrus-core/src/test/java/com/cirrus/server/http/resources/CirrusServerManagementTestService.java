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

import com.cirrus.server.exception.StartCirrusServerException;
import com.cirrus.server.exception.StopCirrusServerException;
import com.cirrus.server.http.entity.CirrusServerInformation;
import com.cirrus.server.osgi.extension.AuthenticationException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@SuppressWarnings("UnusedDeclaration")
@Path("/test/admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CirrusServerManagementTestService extends CirrusServerManagementService {

    @GET
    @Path("start")
    public CirrusServerInformation startServer(@HeaderParam("Authorization") final String tokenValue) throws StartCirrusServerException, AuthenticationException {
        return super.start(tokenValue);
    }

    @GET
    @Path("stop")
    public CirrusServerInformation stopServer(@HeaderParam("Authorization") final String tokenValue) throws StopCirrusServerException, AuthenticationException {
        return super.stop(tokenValue);
    }
}
