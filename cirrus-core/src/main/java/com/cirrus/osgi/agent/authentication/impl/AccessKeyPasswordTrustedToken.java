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

package com.cirrus.osgi.agent.authentication.impl;

import com.cirrus.osgi.agent.authentication.IStorageServiceTrustedToken;

public class AccessKeyPasswordTrustedToken implements IStorageServiceTrustedToken {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final String login;
    private final String password;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public AccessKeyPasswordTrustedToken(final String login, final String password) {
        this.login = login;
        this.password = password;
    }

    //==================================================================================================================
    // Getters
    //==================================================================================================================
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
