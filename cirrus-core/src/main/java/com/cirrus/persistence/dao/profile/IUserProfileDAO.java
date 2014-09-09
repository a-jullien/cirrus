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

package com.cirrus.persistence.dao.profile;

import com.cirrus.model.profile.IStorageServiceProfile;
import com.cirrus.model.profile.IUserProfile;
import com.cirrus.persistence.dao.ICirrusDAO;
import com.cirrus.persistence.exception.UserProfileNotFoundException;

import java.util.List;

public interface IUserProfileDAO extends ICirrusDAO {

    /**
     * Saves a new user profile
     *
     * @param profile the user profile
     */
    void save(IUserProfile profile);

    /**
     * Removes an existing user profile
     *
     * @param emailAddress the email address associated to the user profile
     */
    void delete(String emailAddress) throws UserProfileNotFoundException;

    /**
     * Add a new profile of storage service
     *
     * @param emailAddress          the email address of the user
     * @param storageServiceProfile the new storage service profile
     */
    IUserProfile addStorageService(final String emailAddress, final IStorageServiceProfile storageServiceProfile) throws UserProfileNotFoundException;

    /**
     * Remove an existing profile of storage service
     *
     * @param emailAddress          the email address
     * @param storageServiceProfile the existing storage service profile
     */
    IUserProfile removeStorageService(final String emailAddress, final IStorageServiceProfile storageServiceProfile) throws UserProfileNotFoundException;

    /**
     * List all user profiles
     */
    List<IUserProfile> listUserProfiles();

    /**
     * Returns the user profile associated to the specified email address
     *
     * @param emailAddress the email address
     * @return the user profile or <code>null</code> if the user profile doesn't exist
     */
    IUserProfile getUserProfileByEmailAddress(final String emailAddress);

}
