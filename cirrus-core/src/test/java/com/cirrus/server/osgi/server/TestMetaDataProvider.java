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

package com.cirrus.server.osgi.server;

import com.cirrus.data.ICirrusMetaData;
import com.cirrus.data.impl.CirrusFileData;
import com.cirrus.distribution.event.data.impl.CirrusDataCreatedEvent;
import com.cirrus.distribution.event.data.impl.CirrusDataRemovedEvent;
import com.cirrus.agent.impl.NameBasedCirrusAgentIdentifier;
import com.cirrus.server.exception.IllegalOperationException;
import com.cirrus.server.impl.MetaDataProvider;
import com.cirrus.persistence.dao.meta.IMetaDataDAO;
import com.cirrus.persistence.service.MongoDBService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.List;

import static com.mongodb.util.MyAsserts.assertEquals;
import static com.mongodb.util.MyAsserts.assertTrue;
import static junit.framework.Assert.assertNotNull;

public class TestMetaDataProvider {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private IMetaDataDAO metaDataDAO;

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Before
    public void setUp() throws UnknownHostException {
        final MongoDBService mongoDBService = new MongoDBService("localhost", 22222);
        this.metaDataDAO = mongoDBService.getMetaDataDAO();
    }

    @After
    public void tearDown() {
        this.metaDataDAO.dropCollection();
    }

    @Test
    public void shouldHaveSuccessfullyStoredMetaDataAfterReceiveCreatedEvent() throws UnknownHostException, IllegalOperationException {
        final MetaDataProvider metaDataProvider = new MetaDataProvider(this.metaDataDAO);
        final NameBasedCirrusAgentIdentifier cirrusAgentId = new NameBasedCirrusAgentIdentifier("myCirrusAgent");
        final CirrusFileData cirrusData = new CirrusFileData("/tmp/flunny");
        final CirrusDataCreatedEvent createdEvent = new CirrusDataCreatedEvent(cirrusAgentId, "/cirrus/project/A", cirrusData);
        metaDataProvider.handleCirrusDataEvent(createdEvent);

        final List<ICirrusMetaData> metaDataList = this.metaDataDAO.listMetaDataByCirrusAgentId(cirrusAgentId);
        assertNotNull(metaDataList);
        assertEquals(1, metaDataList.size());
        final ICirrusMetaData storedMetaData = metaDataList.get(0);
        assertNotNull(storedMetaData.getId());
        assertEquals("flunny", storedMetaData.getName());
        assertEquals("myCirrusAgent", storedMetaData.getCirrusAgentId());
        assertEquals("/tmp/flunny", storedMetaData.getLocalPath());
        assertEquals("", storedMetaData.getCirrusAgentType());
        assertEquals("", storedMetaData.getMediaType());
        assertEquals("/cirrus/project/A", storedMetaData.getVirtualPath());
        assertTrue(storedMetaData.getCreationDate() > 1);
    }

    @Test
    public void shouldHaveSuccessfullyRemovedMetaDataAfterReceiveRemovedEvent() throws UnknownHostException, IllegalOperationException {

        final MetaDataProvider metaDataProvider = new MetaDataProvider(this.metaDataDAO);
        final NameBasedCirrusAgentIdentifier cirrusAgentId = new NameBasedCirrusAgentIdentifier("myCirrusAgent");
        final CirrusFileData cirrusData = new CirrusFileData("/tmp/flunny");
        final CirrusDataCreatedEvent createdEvent = new CirrusDataCreatedEvent(cirrusAgentId, "/cirrus/project/A", cirrusData);
        metaDataProvider.handleCirrusDataEvent(createdEvent);

        final List<ICirrusMetaData> metaDataListAfterCreatedEvent = this.metaDataDAO.listMetaDataByCirrusAgentId(cirrusAgentId);
        assertNotNull(metaDataListAfterCreatedEvent);
        assertEquals(1, metaDataListAfterCreatedEvent.size());

        final CirrusDataRemovedEvent cirrusDataRemovedEvent = new CirrusDataRemovedEvent(cirrusAgentId, "/cirrus/project/A", cirrusData);
        metaDataProvider.handleCirrusDataEvent(cirrusDataRemovedEvent);

        final List<ICirrusMetaData> metaDataListAfterRemovedEvent = this.metaDataDAO.listMetaDataByCirrusAgentId(cirrusAgentId);
        assertNotNull(metaDataListAfterRemovedEvent);
        assertEquals(0, metaDataListAfterRemovedEvent.size());
    }
}
