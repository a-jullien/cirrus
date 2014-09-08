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

package com.cirrus.model.profile.impl;

import com.cirrus.agent.authentication.IStorageServiceAuthenticator;
import com.cirrus.model.profile.IStorageServiceProfile;
import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;

public class StorageServiceProfile implements IStorageServiceProfile {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    @Id
    @ObjectId // auto
    private String id;
    private String serviceName;
    private IStorageServiceAuthenticator storageServiceAuthenticator;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================


    public StorageServiceProfile() {
        super();
    }

    public StorageServiceProfile(final String serviceName) {
        this();
        this.serviceName = serviceName;
    }

    //==================================================================================================================
    // Getters
    //==================================================================================================================

    public String getId() {
        return id;
    }

    @Override
    public IStorageServiceAuthenticator getStorageServiceAuthenticator() {
        return this.storageServiceAuthenticator;
    }

    public String getServiceName() {
        return serviceName;
    }

    //==================================================================================================================
    // Setters
    //==================================================================================================================

    public void setId(final String id) {
        this.id = id;
    }

    public void setStorageServiceAuthenticator(final IStorageServiceAuthenticator storageServiceAuthenticator) {
        this.storageServiceAuthenticator = storageServiceAuthenticator;
    }
}
