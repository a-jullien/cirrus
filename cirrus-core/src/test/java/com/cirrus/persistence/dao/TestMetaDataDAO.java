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

import com.cirrus.agent.ICirrusAgentIdentifier;
import com.cirrus.agent.impl.NameBasedCirrusAgentIdentifier;
import com.cirrus.agent.impl.UUIDBasedCirrusAgentIdentifier;
import com.cirrus.model.data.ICirrusMetaData;
import com.cirrus.model.data.impl.CirrusMetaData;
import com.cirrus.model.data.impl.DataType;
import com.cirrus.persistence.IQuery;
import com.cirrus.persistence.QueryBuilder;
import com.cirrus.persistence.dao.meta.IMetaDataDAO;
import com.cirrus.persistence.exception.CirrusMetaDataNotFoundException;
import com.cirrus.persistence.service.MongoDBService;
import com.cirrus.server.configuration.CirrusProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

public class TestMetaDataDAO {

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private IMetaDataDAO metaDataDAO;

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
    public void shouldHaveNoRecordsWhenListMetaDataByUnknownCirrusAgentId() {
        final List<ICirrusMetaData> cirrusMetaDatas = this.metaDataDAO.listMetaDataByCirrusAgentId(new UUIDBasedCirrusAgentIdentifier());
        assertNotNull(cirrusMetaDatas);
        assertThat(cirrusMetaDatas.size()).isEqualTo(0);
    }

    @Test
    public void shouldSuccessfullySaveMetaData() {
        this.metaDataDAO.save(this.createCirrusFileMetaData());
        final String uuid = "1f553132-43e0-4fd3-9e50-fcf1d0adc978";
        final List<ICirrusMetaData> metaDataFromDatabase =
                this.metaDataDAO.listMetaDataByCirrusAgentId(new NameBasedCirrusAgentIdentifier(uuid));
        assertThat(metaDataFromDatabase).isNotNull();
        assertThat(metaDataFromDatabase.size()).isEqualTo(1);

        final ICirrusMetaData cirrusMetaData = metaDataFromDatabase.get(0);
        checkData(cirrusMetaData);
    }

    @Test
    public void shouldSuccessfullyRemoveExistingMetaData() throws CirrusMetaDataNotFoundException {
        final ICirrusMetaData cirrusMetaData = this.createCirrusFileMetaData();
        this.metaDataDAO.save(cirrusMetaData);
        this.metaDataDAO.delete(cirrusMetaData.getId());

        final String uuid = "1f553132-43e0-4fd3-9e50-fcf1d0adc978";
        assertThat(this.metaDataDAO.listMetaDataByCirrusAgentId(new NameBasedCirrusAgentIdentifier(uuid)).size()).isEqualTo(0);
    }

    @Test
    public void shouldSuccessfullyUpdateExistingMetaData() throws CirrusMetaDataNotFoundException {
        final CirrusMetaData cirrusMetaData = this.createCirrusFileMetaData();
        this.metaDataDAO.save(cirrusMetaData);

        cirrusMetaData.setName("myFile.txt.copy");

        this.metaDataDAO.update(cirrusMetaData);
        final String uuid = "1f553132-43e0-4fd3-9e50-fcf1d0adc978";
        final List<ICirrusMetaData> reloadedMetaData = this.metaDataDAO.listMetaDataByCirrusAgentId(new NameBasedCirrusAgentIdentifier(uuid));
        assertThat(reloadedMetaData.size()).isEqualTo(1);
        final ICirrusMetaData metaData = reloadedMetaData.get(0);
        assertThat(metaData.getName()).isEqualTo("myFile.txt.copy");
    }

    @Test
    public void shouldRetrieveSuccessfullyMetaDataFromIdentifier() {
        final CirrusMetaData cirrusMetaData = this.createCirrusFileMetaData();
        this.metaDataDAO.save(cirrusMetaData);

        final ICirrusMetaData dataById = this.metaDataDAO.getMetaDataById(cirrusMetaData.getId());
        assertNotNull(dataById);

        checkData(dataById);
    }

    @Test(expected = CirrusMetaDataNotFoundException.class)
    public void shouldHaveErrorWhenTryToUpdateNonExistingMetaData() throws CirrusMetaDataNotFoundException {
        final CirrusMetaData cirrusFileMetaData = this.createCirrusFileMetaData();
        cirrusFileMetaData.setId("5317109c58126fa0941fb57f");
        this.metaDataDAO.update(cirrusFileMetaData);
    }

    @Test(expected = CirrusMetaDataNotFoundException.class)
    public void shouldHaveErrorWhenTryToRemoveNonExistingMetaData() throws CirrusMetaDataNotFoundException {
        this.metaDataDAO.delete("5317109c58126fa0941fb57f");
    }

    @Test
    public void shouldRetrieveSuccessfullyExistingMetaDataWithOtherCriteria() {
        final CirrusMetaData cirrusMetaData = this.createCirrusFileMetaData();
        this.metaDataDAO.save(cirrusMetaData);

        final NameBasedCirrusAgentIdentifier sourceCirrusAgentId = new NameBasedCirrusAgentIdentifier("1f553132-43e0-4fd3-9e50-fcf1d0adc978");
        final IQuery query = this.createQuery(sourceCirrusAgentId);
        final List<ICirrusMetaData> metaDataList = this.metaDataDAO.findMetaData(query);
        assertThat(metaDataList).isNotNull();
        assertThat(metaDataList.size()).isEqualTo(1);
    }

    @Test
    public void shouldFailOnNonExistingMetaDataWithOtherCriteria() {
        final CirrusMetaData cirrusMetaData = this.createCirrusFileMetaData();
        this.metaDataDAO.save(cirrusMetaData);

        final UUIDBasedCirrusAgentIdentifier sourceCirrusAgentId = new UUIDBasedCirrusAgentIdentifier();
        final IQuery query = this.createQuery(sourceCirrusAgentId);
        final List<ICirrusMetaData> cirrusMetaDataList = this.metaDataDAO.findMetaData(query);
        assertThat(cirrusMetaDataList).isNotNull();
        assertThat(cirrusMetaDataList.size()).isEqualTo(0);
    }
    //==================================================================================================================
    // Private
    //==================================================================================================================
    private CirrusMetaData createCirrusFileMetaData() {
        final CirrusMetaData cirrusMetaData = new CirrusMetaData();
        cirrusMetaData.setName("myFile.txt");
        cirrusMetaData.setDataType(DataType.FILE);
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
        assertEquals(DataType.FILE, cirrusMetaData.getDataType());
        assertEquals("text/plain", cirrusMetaData.getMediaType());
    }

    private IQuery createQuery(final ICirrusAgentIdentifier sourceCirrusAgentId) {
        final QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.appendCriteria("cirrusAgentId", sourceCirrusAgentId.toExternal())
                .appendCriteria("name", "myFile.txt")
                .appendCriteria("localPath", "/tmp/cirrus")
                .appendCriteria("virtualPath", "/home/test");

        return queryBuilder.buildQuery();
    }
}
