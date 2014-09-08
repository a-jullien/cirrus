/**
 * Copyright (c) 2014 Antoine Jullien
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cirrus.server.osgi.extension;

import com.cirrus.agent.authentication.IStorageServiceAuthenticator;
import com.cirrus.server.IGlobalContext;

public abstract class AbstractStorageService<TrustedToken extends IStorageServiceAuthenticator> implements ICirrusStorageService<TrustedToken> {

    //==================================================================================================================
    // Private
    //==================================================================================================================

    private IGlobalContext globalContext;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    protected AbstractStorageService() {
        super();
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Override
    public void initialize(final IGlobalContext globalContext) {
        this.globalContext = globalContext;
    }

    public IGlobalContext getGlobalContext() {
        return globalContext;
    }
}
