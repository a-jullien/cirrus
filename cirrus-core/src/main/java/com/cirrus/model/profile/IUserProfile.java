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

package com.cirrus.model.profile;

import com.cirrus.model.profile.impl.UserProfile;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(as=UserProfile.class)
public interface IUserProfile {

    /**
     * Returns the email address of the user
     */
    String getEmailAddress();

    /**
     * Adds a new storage service profile
     * @param profile the storage service profile
     */
    void addStorageProfile(final IStorageServiceProfile profile);

    /**
     * Removes a storage service profile
     * @param profile the storage service profile
     */
    void removeStorageProfile(final IStorageServiceProfile profile);

    /**
     * Returns all storage service profiles
     */
    List<IStorageServiceProfile> listStorageProfiles();
}
