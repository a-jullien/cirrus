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

import com.cirrus.model.profile.IStorageServiceProfile;
import com.cirrus.model.profile.IUserProfile;
import org.jongo.marshall.jackson.oid.Id;

import java.util.ArrayList;
import java.util.List;

public class UserProfile implements IUserProfile {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    @Id
    private String emailAddress;
    private final List<IStorageServiceProfile> storageProfiles;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    public UserProfile() {
        super();
        this.storageProfiles = new ArrayList<>();
    }

    public UserProfile(final String emailAddress) {
        this();
        this.emailAddress = emailAddress;
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================


    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public void addStorageProfile(final IStorageServiceProfile profile) {
        this.storageProfiles.add(profile);
    }

    @Override
    public void removeStorageProfile(final IStorageServiceProfile profile) {
        this.storageProfiles.remove(profile);
    }

    @Override
    public List<IStorageServiceProfile> listStorageProfiles() {
        return this.storageProfiles;
    }


    //==================================================================================================================
    // Setters
    //==================================================================================================================


    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
