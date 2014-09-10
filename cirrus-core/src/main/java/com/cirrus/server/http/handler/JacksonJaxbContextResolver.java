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

package com.cirrus.server.http.handler;


import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@SuppressWarnings("ALL")
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JacksonJaxbContextResolver implements ContextResolver<ObjectMapper> {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    private ObjectMapper objectMapper;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    public JacksonJaxbContextResolver() {
        this.objectMapper = new ObjectMapper();
        final AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
        final AnnotationIntrospector secondary = new JaxbAnnotationIntrospector();
        final AnnotationIntrospector pair = new AnnotationIntrospector.Pair(primary, secondary);

        this.objectMapper.getDeserializationConfig().setAnnotationIntrospector(pair);
        this.objectMapper.getSerializationConfig().setAnnotationIntrospector(pair);
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    public ObjectMapper getContext(Class<?> objectType) {
        return objectMapper;
    }
}