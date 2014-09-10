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

package com.cirrus.server.http.client;

import com.cirrus.model.authentication.impl.LoginPasswordCredentials;
import com.cirrus.model.profile.IUserProfile;
import com.cirrus.persistence.dao.profile.IUserProfileDAO;
import com.cirrus.server.osgi.extension.AuthenticationException;
import com.cirrus.utils.EncryptionUtils;

public class AuthenticationProvider {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    private final IUserProfileDAO userProfileDAO;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    public AuthenticationProvider(final IUserProfileDAO userProfileDAO) {
        this.userProfileDAO = userProfileDAO;
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    public void authenticate(final LoginPasswordCredentials credential) throws AuthenticationException {
        final String userName = credential.getEmailAddress();
        final String password = credential.getPassword();

        final IUserProfile userProfile = this.userProfileDAO.getUserProfileByEmailAddress(userName);
        if (userProfile == null) {
            throw new AuthenticationException("Not authorized");
        } else {
            final String encryptedPassword = userProfile.getPassword();

            final boolean match = EncryptionUtils.match(password, encryptedPassword);
            if (!match) {
                throw new AuthenticationException("Not authorized");
            }
        }
    }
}
