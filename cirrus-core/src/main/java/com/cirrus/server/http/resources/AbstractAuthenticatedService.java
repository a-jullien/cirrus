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

import com.cirrus.model.authentication.Token;
import com.cirrus.server.http.client.ClientService;
import com.cirrus.server.http.client.ClientServiceFactory;
import com.cirrus.server.osgi.extension.AuthenticationException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public abstract class AbstractAuthenticatedService {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    @Inject
    private ClientServiceFactory clientServiceFactory;
    @HeaderParam("Authorization")
    private String token;

    //==================================================================================================================
    // Public
    //==================================================================================================================

    protected ClientService getClientService() throws AuthenticationException {
        return clientServiceFactory.createClientService(new Token(token));
    }
}
