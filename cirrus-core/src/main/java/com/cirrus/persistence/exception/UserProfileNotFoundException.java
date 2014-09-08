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

package com.cirrus.persistence.exception;

public class UserProfileNotFoundException extends Exception {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final long serialVersionUID = -4355631609661116256L;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    public UserProfileNotFoundException(final String emailAddress) {
        super("The profile for user <" + emailAddress + "> doesn't exist");
    }
}
