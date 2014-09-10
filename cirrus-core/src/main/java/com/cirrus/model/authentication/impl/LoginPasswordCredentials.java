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

package com.cirrus.model.authentication.impl;

import com.cirrus.server.http.client.AuthenticationProvider;
import com.cirrus.server.osgi.extension.AuthenticationException;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class LoginPasswordCredentials extends AbstractCredentials {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    private final String emailAddress;
    private final String password;


    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    @JsonCreator
    public LoginPasswordCredentials(@JsonProperty("emailAddress") final String emailAddress,
                                    @JsonProperty("password") final String password) {
        super("LoginPassword");
        this.emailAddress = emailAddress;
        this.password = password;
    }

    //==================================================================================================================
    // Getters
    //==================================================================================================================

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void authenticate(final AuthenticationProvider provider) throws AuthenticationException {
        provider.authenticate(this);
    }
}
