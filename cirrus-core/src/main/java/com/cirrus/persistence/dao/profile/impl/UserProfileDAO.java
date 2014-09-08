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

package com.cirrus.persistence.dao.profile.impl;

import com.cirrus.model.profile.IUserProfile;
import com.cirrus.persistence.dao.profile.IUserProfileDAO;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UserProfileDAO implements IUserProfileDAO {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    private final MongoCollection profileCollection;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    public UserProfileDAO(final MongoCollection profileCollection) {
        this.profileCollection = profileCollection;
        this.profileCollection.ensureIndex("{emailAddress: 1}");
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Override
    public void dropCollection() {
        this.profileCollection.dropIndexes();
        this.profileCollection.drop();
    }

    @Override
    public void save(final IUserProfile profile) {
        this.profileCollection.save(profile);
    }

    @Override
    public List<IUserProfile> listUserProfiles() {
        final List<IUserProfile> profiles = new ArrayList<>();
        final Iterable<IUserProfile> userProfiles = this.profileCollection.find().as(IUserProfile.class);
        userProfiles.forEach(new Consumer<IUserProfile>() {
            @Override
            public void accept(final IUserProfile userProfile) {
                profiles.add(userProfile);
            }
        });

        return profiles;
    }

    @Override
    public IUserProfile getUserProfileByEmailAddress(final String emailAddress) {
        final String query = "{_id: '" + emailAddress + "'}";
        return this.profileCollection.findOne(query).as(IUserProfile.class);
    }
}
