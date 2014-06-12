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

package com.cirrus.server.http.exception.mapper;

import com.cirrus.server.exception.ServerNotStartedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@SuppressWarnings("UnusedDeclaration")
@Provider
public class ServerNotStartedExceptionMapper extends AbstractExceptionMapper<ServerNotStartedException> {
    @Override
    public Response toResponse(final ServerNotStartedException e) {
        return super.buildResponse(Response.Status.NOT_ACCEPTABLE, "Server not started");
    }
}
