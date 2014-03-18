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

package com.cirrus.server.impl;

import com.cirrus.agent.ICirrusAgentIdentifier;
import com.cirrus.data.ICirrusData;
import com.cirrus.data.ICirrusMetaData;
import com.cirrus.data.impl.CirrusMetaData;
import com.cirrus.distribution.event.data.ICirrusDataEvent;
import com.cirrus.distribution.event.data.ICirrusDataEventVisitor;
import com.cirrus.distribution.event.data.impl.ICirrusDataCreatedEvent;
import com.cirrus.distribution.event.data.impl.ICirrusDataRemovedEvent;
import com.cirrus.persistence.IQuery;
import com.cirrus.persistence.QueryBuilder;
import com.cirrus.persistence.dao.meta.IMetaDataDAO;
import com.cirrus.persistence.exception.CirrusMetaDataNotFoundException;
import com.cirrus.server.ICirrusDataListener;
import com.cirrus.server.exception.IllegalOperationException;

import java.util.List;

public class MetaDataNotifier implements ICirrusDataListener {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final IMetaDataDAO metaDataDAO;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public MetaDataNotifier(final IMetaDataDAO metaDataDAO) {
        super();
        this.metaDataDAO = metaDataDAO;
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Override
    public void handleCirrusDataEvent(final ICirrusDataEvent cirrusDataEvent) throws IllegalOperationException {
        cirrusDataEvent.accept(new ICirrusDataEventVisitor() {
            @Override
            public void visit(final ICirrusDataCreatedEvent createdEvent) throws IllegalOperationException {
                performCirrusDataCreatedEvent(createdEvent);
            }

            @Override
            public void visit(final ICirrusDataRemovedEvent removedEvent) throws IllegalOperationException {
                performCirrusDataRemovedEvent(removedEvent);
            }
        });
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private void performCirrusDataRemovedEvent(final ICirrusDataRemovedEvent removedEvent) throws IllegalOperationException {
        final ICirrusAgentIdentifier sourceCirrusAgentId = removedEvent.getSourceCirrusAgentId();
        final String virtualPath = removedEvent.getVirtualPath();
        final ICirrusData cirrusData = removedEvent.getCirrusData();
        final String name = cirrusData.getName();
        final String realPath = cirrusData.getPath();

        final QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.appendCriteria("cirrusAgentId", sourceCirrusAgentId.toExternal())
                .appendCriteria("name", name)
                .appendCriteria("localPath", realPath)
                .appendCriteria("virtualPath", virtualPath);

        final IQuery query = queryBuilder.buildQuery();
        final List<ICirrusMetaData> metaDataList = this.metaDataDAO.findMetaData(query);
        if (metaDataList.size() == 0) {
            throw new IllegalOperationException("Could not retrieve meta data for query <" + query + ">");
        } else {
            try {
                this.metaDataDAO.delete(metaDataList.get(0).getId());
            } catch (final CirrusMetaDataNotFoundException e) {
                throw new IllegalOperationException(e);
            }
        }
    }

    private void performCirrusDataCreatedEvent(final ICirrusDataCreatedEvent createdEvent) throws IllegalOperationException {
        final long eventTimeStamp = createdEvent.getEventTimeStamp();
        final ICirrusAgentIdentifier sourceCirrusAgentId = createdEvent.getSourceCirrusAgentId();
        final String virtualPath = createdEvent.getVirtualPath();
        final ICirrusData createdCirrusData = createdEvent.getCirrusData();
        // create meta data from created data
        final ICirrusMetaData metaData = createMetaDataFrom(eventTimeStamp, sourceCirrusAgentId, virtualPath, createdCirrusData);
        // save meta information
        this.metaDataDAO.save(metaData);
    }


    private static ICirrusMetaData createMetaDataFrom(final long creationTime, final ICirrusAgentIdentifier sourceId, final String virtualPath, final ICirrusData cirrusData) throws IllegalOperationException {
        final CirrusMetaData metaData = new CirrusMetaData();
        metaData.setDataType(cirrusData.getDataType());
        metaData.setName(cirrusData.getName());
        metaData.setLocalPath(cirrusData.getPath());
        metaData.setCreationDate(creationTime);
        metaData.setCirrusAgentId(sourceId.toExternal());
        metaData.setVirtualPath(virtualPath);
        metaData.setCirrusAgentType(""); // TODO
        metaData.setMediaType(""); // TODO media type
        return metaData;

    }
}
