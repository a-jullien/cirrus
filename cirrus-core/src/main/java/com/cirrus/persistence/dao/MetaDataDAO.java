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

package com.cirrus.persistence.dao;

import com.cirrus.data.ICirrusMetaData;
import com.cirrus.data.impl.CirrusMetaData;
import com.cirrus.osgi.agent.ICirrusAgentIdentifier;
import com.cirrus.persistence.exception.CirrusMetaDataNotFoundException;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import java.util.ArrayList;
import java.util.List;

public class MetaDataDAO implements IMetaDataDAO {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final MongoCollection metaDataCollection;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public MetaDataDAO(final MongoCollection metaDataCollection) {
        this.metaDataCollection = metaDataCollection;
        this.metaDataCollection.ensureIndex("{name: 1}");
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public void dropCollection() {
        this.metaDataCollection.drop();
    }

    @Override
    public ICirrusMetaData getMetaDataById(final String id) {
        return this.metaDataCollection.findOne(new ObjectId(id)).as(CirrusMetaData.class);
    }

    @Override
    public List<ICirrusMetaData> listMetaDataByCirrusAgentId(final ICirrusAgentIdentifier cirrusAgentId) {
        final List<ICirrusMetaData> result = new ArrayList<>();
        final String query = "{cirrusAgentId: '" + cirrusAgentId.toExternal() + "'}";
        final Iterable<CirrusMetaData> cirrusMetaDatas = this.metaDataCollection.find(query).as(CirrusMetaData.class);
        for (final CirrusMetaData cirrusMetaData : cirrusMetaDatas) {
            result.add(cirrusMetaData);
        }

        return result;
    }

    @Override
    public void save(final ICirrusMetaData metaData) {
        this.metaDataCollection.save(metaData);
    }

    @Override
    public void update(final ICirrusMetaData metaData) throws CirrusMetaDataNotFoundException {
        final String metaDataId = metaData.getId();
        if (!this.exists(metaDataId)) {
            throw new CirrusMetaDataNotFoundException(metaDataId);
        }

        this.metaDataCollection.update(new ObjectId(metaDataId)).with(metaData);
    }

    @Override
    public void delete(final String metaDataId) throws CirrusMetaDataNotFoundException {
        if (!this.exists(metaDataId)) {
            throw new CirrusMetaDataNotFoundException(metaDataId);
        } else {
            this.metaDataCollection.remove(new ObjectId(metaDataId));
        }
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================

    private boolean exists(final String metaDataId) {
        final CirrusMetaData cirrusMetaData = this.metaDataCollection.findOne(new ObjectId(metaDataId)).as(CirrusMetaData.class);
        return cirrusMetaData != null;
    }
}
