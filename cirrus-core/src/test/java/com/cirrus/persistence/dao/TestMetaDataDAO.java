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
import com.cirrus.osgi.agent.impl.NameBasedCirrusAgentIdentifier;
import com.cirrus.osgi.agent.impl.UUIDBasedCirrusAgentIdentifier;
import com.cirrus.persistence.exception.CirrusMetaDataNotFoundException;
import com.cirrus.persistence.service.MongoDBService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class TestMetaDataDAO {

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private IMetaDataDAO metaDataDAO;

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
    public void shouldHaveNoRecordsWhenListMetaDataByUnknownCirrusAgentId() {
        final List<ICirrusMetaData> cirrusMetaDatas = this.metaDataDAO.listMetaDataByCirrusAgentId(new UUIDBasedCirrusAgentIdentifier());
        assertNotNull(cirrusMetaDatas);
        assertEquals(0, cirrusMetaDatas.size());
    }

    @Test
    public void shouldSuccessfullySaveMetaData() {
        this.metaDataDAO.save(this.createCirrusMetaData());
        final String uuid = "1f553132-43e0-4fd3-9e50-fcf1d0adc978";
        final List<ICirrusMetaData> metaDataFromDatabase =
                this.metaDataDAO.listMetaDataByCirrusAgentId(new NameBasedCirrusAgentIdentifier(uuid));
        assertNotNull(metaDataFromDatabase);
        assertEquals(1, metaDataFromDatabase.size());

        final ICirrusMetaData cirrusMetaData = metaDataFromDatabase.get(0);
        checkData(cirrusMetaData);
    }

    @Test
    public void shouldSuccessfullyRemoveExistingMetaData() throws CirrusMetaDataNotFoundException {
        final ICirrusMetaData cirrusMetaData = this.createCirrusMetaData();
        this.metaDataDAO.save(cirrusMetaData);
        this.metaDataDAO.delete(cirrusMetaData.getId());

        final String uuid = "1f553132-43e0-4fd3-9e50-fcf1d0adc978";
        assertEquals(0, this.metaDataDAO.listMetaDataByCirrusAgentId(new NameBasedCirrusAgentIdentifier(uuid)).size());
    }

    @Test
    public void shouldSuccessfullyUpdateExistingMetaData() throws CirrusMetaDataNotFoundException {
        final CirrusMetaData cirrusMetaData = this.createCirrusMetaData();
        this.metaDataDAO.save(cirrusMetaData);

        cirrusMetaData.setName("myFile.txt.copy");

        this.metaDataDAO.update(cirrusMetaData);
        final String uuid = "1f553132-43e0-4fd3-9e50-fcf1d0adc978";
        final List<ICirrusMetaData> reloadedMetaData = this.metaDataDAO.listMetaDataByCirrusAgentId(new NameBasedCirrusAgentIdentifier(uuid));
        assertEquals(1, reloadedMetaData.size());
        final ICirrusMetaData metaData = reloadedMetaData.get(0);
        assertEquals("myFile.txt.copy", metaData.getName());
    }

    @Test
    public void shouldRetrieveSuccessfullyMetaDataFromIdentifier() {
        final CirrusMetaData cirrusMetaData = this.createCirrusMetaData();
        this.metaDataDAO.save(cirrusMetaData);

        final ICirrusMetaData dataById = this.metaDataDAO.getMetaDataById(cirrusMetaData.getId());
        assertNotNull(dataById);

        checkData(dataById);
    }

    @Test(expected = CirrusMetaDataNotFoundException.class)
    public void shouldHaveErrorWhenTryToUpdateNonExistingMetaData() throws CirrusMetaDataNotFoundException {
        final CirrusMetaData cirrusMetaData = this.createCirrusMetaData();
        cirrusMetaData.setId("5317109c58126fa0941fb57f");
        this.metaDataDAO.update(cirrusMetaData);
    }

    @Test(expected = CirrusMetaDataNotFoundException.class)
    public void shouldHaveErrorWhenTryToRemoveNonExistingMetaData() throws CirrusMetaDataNotFoundException {
        this.metaDataDAO.delete("5317109c58126fa0941fb57f");
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private CirrusMetaData createCirrusMetaData() {
        final CirrusMetaData cirrusMetaData = new CirrusMetaData();
        cirrusMetaData.setName("myFile.txt");
        cirrusMetaData.setCirrusAgentId("1f553132-43e0-4fd3-9e50-fcf1d0adc978");
        cirrusMetaData.setCirrusAgentType("dropbox");
        cirrusMetaData.setCreationDate(1393367761472L);
        cirrusMetaData.setLocalPath("/tmp/cirrus");
        cirrusMetaData.setVirtualPath("/home/test");
        cirrusMetaData.setMediaType("text/plain");
        return cirrusMetaData;
    }

    private static void checkData(final ICirrusMetaData cirrusMetaData) {
        assertEquals("myFile.txt", cirrusMetaData.getName());
        assertEquals("/home/test", cirrusMetaData.getVirtualPath());
        assertEquals("/tmp/cirrus", cirrusMetaData.getLocalPath());
        assertEquals(1393367761472L, cirrusMetaData.getCreationDate());
        assertEquals("1f553132-43e0-4fd3-9e50-fcf1d0adc978", cirrusMetaData.getCirrusAgentId());
        assertEquals("dropbox", cirrusMetaData.getCirrusAgentType());
        assertEquals("text/plain", cirrusMetaData.getMediaType());
    }
}
