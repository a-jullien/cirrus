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

package com.cirrus.persistence.dao.meta;

import com.cirrus.osgi.agent.ICirrusAgentIdentifier;
import com.cirrus.data.ICirrusMetaData;
import com.cirrus.persistence.exception.CirrusMetaDataNotFoundException;

import java.util.List;

public interface IMetaDataDAO {

    /**
     * Drop collection
     */
    void dropCollection();

    /**
     * Returns the cirrus meta data from identifier
     */
    ICirrusMetaData getMetaDataById(final String id);

    /**
     * Returns all meta data stored for a specified cirrus agent
     * @return list of all cirrus meta data associated to the cirrus agent. Never <code>null</code>
     */
    List<ICirrusMetaData> listMetaDataByCirrusAgentId(final ICirrusAgentIdentifier cirrusAgentId);

    /**
     * Saves a new meta data
     */
    void save(final ICirrusMetaData metaData);

    /**
     * Update existing meta data
     */
    void update(final ICirrusMetaData metaData) throws CirrusMetaDataNotFoundException;

    /**
     * Deletes existing meta data
     */
    void delete(final String metaDataId) throws CirrusMetaDataNotFoundException;
}
