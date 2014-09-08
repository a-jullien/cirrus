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

package com.cirrus.agent.authentication.impl;

import com.cirrus.agent.authentication.AuthenticationMode;
import com.cirrus.agent.authentication.IStorageServiceAuthenticator;
import com.fasterxml.jackson.annotation.*;

public abstract class AbstractAuthenticator implements IStorageServiceAuthenticator {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    @JsonIgnore
    private AuthenticationMode mode;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================


    @JsonCreator
    public AbstractAuthenticator(@JsonProperty("mode") final AuthenticationMode mode) {
        this.mode = mode;
    }

    //==================================================================================================================
    // Getters
    //==================================================================================================================

    public AuthenticationMode getMode() {
        return this.mode;
    }

    //==================================================================================================================
    // Setters
    //==================================================================================================================

    public void setMode(final AuthenticationMode mode) {
        this.mode = mode;
    }
}
