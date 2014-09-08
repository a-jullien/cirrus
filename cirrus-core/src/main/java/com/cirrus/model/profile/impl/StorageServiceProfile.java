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

public class StorageServiceProfile implements IStorageServiceProfile {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    private String profileName;
    private String serviceName;
    private IStorageServiceAuthenticator storageServiceAuthenticator;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================


    public StorageServiceProfile() {
        super();
    }

    public StorageServiceProfile(final String profileName, final String serviceName) {
        this();
        this.profileName = profileName;
        this.serviceName = serviceName;
    }

    //==================================================================================================================
    // Getters
    //==================================================================================================================


    @Override
    public IStorageServiceAuthenticator getStorageServiceAuthenticator() {
        return this.storageServiceAuthenticator;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public String getProfileName() {
        return profileName;
    }

    //==================================================================================================================
    // Setters
    //==================================================================================================================


    public void setStorageServiceAuthenticator(final IStorageServiceAuthenticator storageServiceAuthenticator) {
        this.storageServiceAuthenticator = storageServiceAuthenticator;
    }

    public void setProfileName(final String profileName) {
        this.profileName = profileName;
    }

    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final StorageServiceProfile that = (StorageServiceProfile) o;

        if (profileName != null ? !profileName.equals(that.profileName) : that.profileName != null) return false;
        if (serviceName != null ? !serviceName.equals(that.serviceName) : that.serviceName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = profileName != null ? profileName.hashCode() : 0;
        result = 31 * result + (serviceName != null ? serviceName.hashCode() : 0);
        return result;
    }
}
