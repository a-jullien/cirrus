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

import com.cirrus.model.profile.IUserProfile;
import com.cirrus.persistence.dao.ICirrusDAO;

import java.util.List;

public interface IUserProfileDAO extends ICirrusDAO {

    /**
     * Saves a new user profile
     */
    void save(IUserProfile profile);

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
