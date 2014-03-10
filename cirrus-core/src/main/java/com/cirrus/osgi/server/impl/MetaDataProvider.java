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

import com.cirrus.data.ICirrusData;
import com.cirrus.data.ICirrusMetaData;
import com.cirrus.data.impl.CirrusMetaData;
import com.cirrus.distribution.event.data.ICirrusDataEvent;
import com.cirrus.distribution.event.data.ICirrusDataEventVisitor;
import com.cirrus.distribution.event.data.impl.ICirrusDataCreatedEvent;
import com.cirrus.distribution.event.data.impl.ICirrusDataRemovedEvent;
import com.cirrus.osgi.agent.ICirrusAgentIdentifier;
import com.cirrus.osgi.server.ICirrusDataListener;
import com.cirrus.osgi.server.IMetaDataProvider;
import com.cirrus.osgi.server.exception.IllegalOperationException;
import com.cirrus.persistence.dao.meta.IMetaDataDAO;
import com.cirrus.persistence.exception.CirrusMetaDataNotFoundException;

import java.net.UnknownHostException;

public class MetaDataProvider implements IMetaDataProvider, ICirrusDataListener {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final IMetaDataDAO metaDataDAO;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public MetaDataProvider(final IMetaDataDAO metaDataDAO) throws UnknownHostException {
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
            public void visit(final ICirrusDataCreatedEvent createdEvent) {
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

        final ICirrusMetaData metaData = this.metaDataDAO.findMetaData(sourceCirrusAgentId, name, realPath, virtualPath);
        if (metaData == null) {
            final String criteria = "cirrusAgentId:" + sourceCirrusAgentId + '\n' +
                    "name:" + name + '\n' + "localPath:" + realPath + '\n' + "virtualPath:" + virtualPath;
            throw new IllegalOperationException("Could not retrieve meta data for criteria <" + criteria + ">");
        } else {
            try {
                this.metaDataDAO.delete(metaData.getId());
            } catch (final CirrusMetaDataNotFoundException e) {
                throw new IllegalOperationException(e);
            }
        }
    }

    private void performCirrusDataCreatedEvent(final ICirrusDataCreatedEvent createdEvent) {
        final long eventTimeStamp = createdEvent.getEventTimeStamp();
        final ICirrusAgentIdentifier sourceCirrusAgentId = createdEvent.getSourceCirrusAgentId();
        final String virtualPath = createdEvent.getVirtualPath();
        final ICirrusData createdCirrusData = createdEvent.getCirrusData();
        // create meta data from created data
        final ICirrusMetaData metaData = createMetaDataFrom(eventTimeStamp, sourceCirrusAgentId, virtualPath, createdCirrusData);
        // save meta information
        this.metaDataDAO.save(metaData);
    }


    private static ICirrusMetaData createMetaDataFrom(final long creationTime, final ICirrusAgentIdentifier sourceId, final String virtualPath, final ICirrusData cirrusData) {
        final CirrusMetaData cirrusMetaData = new CirrusMetaData();
        cirrusMetaData.setName(cirrusData.getName());
        cirrusMetaData.setMediaType(""); // TODO media type
        cirrusMetaData.setLocalPath(cirrusData.getPath());
        cirrusMetaData.setCreationDate(creationTime);
        cirrusMetaData.setCirrusAgentId(sourceId.toExternal());
        cirrusMetaData.setVirtualPath(virtualPath);
        cirrusMetaData.setCirrusAgentType(""); // TODO

        return cirrusMetaData;
    }
}
