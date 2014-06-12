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

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public abstract class AbstractExceptionMapper<E extends Throwable> implements ExceptionMapper<E> {

    //==================================================================================================================
    // Public
    //==================================================================================================================

    protected Response buildResponse(final Response.Status httpErrorCode, final String message) {
        final Response.ResponseBuilder builder = Response.status(httpErrorCode).type(MediaType.TEXT_PLAIN_TYPE);
        builder.entity(message);
        return builder.build();
    }

}
