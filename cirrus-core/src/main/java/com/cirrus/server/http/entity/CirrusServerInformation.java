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

package com.cirrus.server.http.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CirrusServerInformation {

    public enum STATUS {
        STARTED,
        STOPPED
    }

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final String name;
    private final STATUS status;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    @JsonCreator
    public CirrusServerInformation(@JsonProperty("name") final String name, @JsonProperty("status") final STATUS status) {
        this.name = name;
        this.status = status;
    }

    //==================================================================================================================
    // Getters
    //==================================================================================================================

    public String getName() {
        return name;
    }

    public STATUS getStatus() {
        return status;
    }
}
