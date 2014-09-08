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

import com.cirrus.agent.impl.NameBasedCirrusAgentIdentifier;
import com.cirrus.model.data.ICirrusMetaData;
import com.cirrus.model.data.impl.CirrusFileData;
import com.cirrus.model.data.impl.DataType;
import com.cirrus.distribution.event.data.impl.CirrusDataCreatedEvent;
import com.cirrus.distribution.event.data.impl.CirrusDataRemovedEvent;
import com.cirrus.persistence.dao.meta.IMetaDataDAO;
import com.cirrus.persistence.service.MongoDBService;
import com.cirrus.server.configuration.CirrusProperties;
import com.cirrus.server.impl.MetaDataNotifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.mongodb.util.MyAsserts.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

public class TestMetaDataProvider {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private IMetaDataDAO metaDataDAO;

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Before
    public void setUp() throws IOException {
        final CirrusProperties cirrusProperties = new CirrusProperties();
        final String databaseURL = cirrusProperties.getProperty(CirrusProperties.MONGODB_URL);

        final MongoDBService mongoDBService = new MongoDBService(databaseURL);
        this.metaDataDAO = mongoDBService.getMetaDataDAO();
    }

    @After
    public void tearDown() {
        this.metaDataDAO.dropCollection();
    }

    @Test
    public void shouldHaveSuccessfullyStoredMetaDataAfterReceiveCreatedEvent() throws UnknownHostException, ExecutionException {
        final MetaDataNotifier metaDataNotifier = new MetaDataNotifier(this.metaDataDAO);
        final NameBasedCirrusAgentIdentifier cirrusAgentId = new NameBasedCirrusAgentIdentifier("myCirrusAgent");
        final CirrusFileData cirrusData = new CirrusFileData("/tmp/flunny", 42);
        final CirrusDataCreatedEvent createdEvent = new CirrusDataCreatedEvent(cirrusAgentId, "/cirrus/project/A", cirrusData);
        metaDataNotifier.handleCirrusDataEvent(createdEvent);

        final List<ICirrusMetaData> metaDataList = this.metaDataDAO.listMetaDataByCirrusAgentId(cirrusAgentId);
        assertThat(metaDataList).isNotNull();
        assertThat(metaDataList.size()).isEqualTo(1);
        final ICirrusMetaData storedMetaData = metaDataList.get(0);
        assertThat(storedMetaData.getId()).isNotNull();
        assertThat(storedMetaData.getName()).isEqualTo("flunny");
        assertThat(storedMetaData.getCirrusAgentId()).isEqualTo("myCirrusAgent");
        assertThat(storedMetaData.getLocalPath()).isEqualTo("/tmp/flunny");
        assertThat(storedMetaData.getCirrusAgentType()).isEqualTo("");
        assertThat(storedMetaData.getDataType()).isEqualTo(DataType.FILE);
        assertThat(storedMetaData.getVirtualPath()).isEqualTo("/cirrus/project/A");
        assertThat(storedMetaData.getSize()).isEqualTo(42);
        assertTrue(storedMetaData.getCreationDate() > 1);
    }

    @Test
    public void shouldHaveSuccessfullyRemovedMetaDataAfterReceiveRemovedEvent() throws UnknownHostException, ExecutionException {

        final MetaDataNotifier metaDataNotifier = new MetaDataNotifier(this.metaDataDAO);
        final NameBasedCirrusAgentIdentifier cirrusAgentId = new NameBasedCirrusAgentIdentifier("myCirrusAgent");
        final CirrusFileData cirrusData = new CirrusFileData("/tmp/flunny", 42);
        final CirrusDataCreatedEvent createdEvent = new CirrusDataCreatedEvent(cirrusAgentId, "/cirrus/project/A", cirrusData);
        metaDataNotifier.handleCirrusDataEvent(createdEvent);

        final List<ICirrusMetaData> metaDataListAfterCreatedEvent = this.metaDataDAO.listMetaDataByCirrusAgentId(cirrusAgentId);
        assertThat(metaDataListAfterCreatedEvent).isNotNull();
        assertThat(metaDataListAfterCreatedEvent.size()).isEqualTo(1);

        final CirrusDataRemovedEvent cirrusDataRemovedEvent = new CirrusDataRemovedEvent(cirrusAgentId, "/cirrus/project/A", cirrusData);
        metaDataNotifier.handleCirrusDataEvent(cirrusDataRemovedEvent);

        final List<ICirrusMetaData> metaDataListAfterRemovedEvent = this.metaDataDAO.listMetaDataByCirrusAgentId(cirrusAgentId);
        assertThat(metaDataListAfterRemovedEvent).isNotNull();
        assertThat(metaDataListAfterRemovedEvent.size()).isEqualTo(0);
    }
}
