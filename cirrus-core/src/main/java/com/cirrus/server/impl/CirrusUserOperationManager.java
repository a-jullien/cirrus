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

package com.cirrus.server.impl;

import com.cirrus.agent.ICirrusAgent;
import com.cirrus.data.ICirrusMetaData;
import com.cirrus.data.impl.CirrusFolderData;
import com.cirrus.distribution.event.data.ICirrusDataEvent;
import com.cirrus.distribution.event.data.impl.CirrusDataCreatedEvent;
import com.cirrus.distribution.scheduler.CirrusScheduler;
import com.cirrus.distribution.scheduler.action.CreateDirectoryAction;
import com.cirrus.persistence.QueryBuilder;
import com.cirrus.persistence.dao.meta.IMetaDataDAO;
import com.cirrus.server.ICirrusAgentManager;
import com.cirrus.server.ICirrusUserOperationManager;
import com.cirrus.server.exception.IllegalOperationException;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CirrusUserOperationManager implements ICirrusUserOperationManager {

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private final CirrusScheduler scheduler;
    private final IMetaDataDAO metaDataDAO;
    private final MetaDataNotifier metaDataNotifier;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    public CirrusUserOperationManager(final ICirrusAgentManager agentAdministration, final IMetaDataDAO metaDataDAO) {
        super();
        this.metaDataDAO = metaDataDAO;
        this.scheduler = new CirrusScheduler(agentAdministration);
        this.metaDataNotifier = new MetaDataNotifier(this.metaDataDAO);
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Override
    public void createDirectory(final String completeVirtualPath) throws ExecutionException {
        final ICirrusAgent agent = this.scheduler.findAgent();
        final CirrusFolderData cirrusFolderData =
                this.scheduler.scheduleAction(new CreateDirectoryAction(agent, completeVirtualPath));

        final String virtualPath = this.extractPath(completeVirtualPath);

        final CirrusDataCreatedEvent cirrusDataEvent =
                new CirrusDataCreatedEvent(agent.getIdentifier(), virtualPath, cirrusFolderData);
        this.dispatchEvent(cirrusDataEvent);
    }

    @Override
    public List<ICirrusMetaData> listCirrusData(final String virtualPath) {
        final QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.appendCriteria("virtualPath", virtualPath);
        return this.metaDataDAO.findMetaData(queryBuilder.buildQuery());
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================

    private void dispatchEvent(final ICirrusDataEvent event) {
        try {
            this.metaDataNotifier.handleCirrusDataEvent(event);
        } catch (final IllegalOperationException e) {
            e.printStackTrace(); // TODO
        }
    }

    private String extractFileName(final String path) {
        final int index = path.lastIndexOf('/');
        if (index == -1) {
            return path;
        } else {
            return path.substring(index + 1, path.length());
        }
    }

    private String extractPath(final String path) {
        final int index = path.lastIndexOf('/');
        if (index == -1) {
            return "/";
        } else {
            return path.substring(0, index + 1);
        }
    }
}
