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

package com.cirrus.osgi.server.impl;

import com.cirrus.osgi.server.IUserDataService;
import com.cirrus.persistence.dao.IMetaDataDAO;
import com.cirrus.persistence.service.MongoDBService;

import java.net.UnknownHostException;

public class UserDataService implements IUserDataService {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final MongoDBService mongoDBService;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public UserDataService(final String databaseURI) throws UnknownHostException {
        super();

        // create mongo service
        this.mongoDBService = new MongoDBService(databaseURI);
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public IMetaDataDAO getMetaDataDAO() {
        return this.mongoDBService.getMetaDataDAO();
    }
}
