package com.cirrus.persistence;

import com.cirrus.data.ICirrusMetaData;
import com.cirrus.data.impl.CirrusMetaData;
import com.cirrus.persistence.service.MongoDBService;
import com.cirrus.persistence.service.IMongoDBService;
import com.mongodb.DB;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class TestMetaData {

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private MongoCollection cirrusMetaDataCollection;

    @Before
    public void setUp() throws UnknownHostException {
        final MongoDBService mongoDBService = new MongoDBService("localhost", 22222);
        final DB database = mongoDBService.getDatabase();
        final Jongo jongo = new Jongo(database);

        this.cirrusMetaDataCollection = jongo.getCollection(IMongoDBService.CIRRUS_META_DATA_COLLECTION);
    }

    @After
    public void tearDown() {
        this.cirrusMetaDataCollection.drop();
    }

    //==================================================================================================================
    // Tests
    //==================================================================================================================
    @Test
    public void shouldSuccessfullySaveCirrusMetaDataIntoMongoDB() throws IOException {
        assertEquals(0, this.cirrusMetaDataCollection.count());

        final ICirrusMetaData cirrusMetaData = this.createCirrusMetaData();

        this.cirrusMetaDataCollection.save(cirrusMetaData);

        assertEquals(1, this.cirrusMetaDataCollection.count());

        final ICirrusMetaData metaData = this.cirrusMetaDataCollection.findOne().as(CirrusMetaData.class);
        assertNotNull(metaData);
        assertEquals("myFile.txt", metaData.getName());
        assertEquals("/home/test", metaData.getVirtualPath());
        assertEquals("/tmp/cirrus", metaData.getLocalPath());
        assertEquals(1393367761472L, metaData.getCreationDate());
        assertEquals("1f553132-43e0-4fd3-9e50-fcf1d0adc978", metaData.getCirrusAgentId());
        assertEquals("dropbox", metaData.getCirrusAgentType());
        assertEquals("text/plain", metaData.getMediaType());
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private ICirrusMetaData createCirrusMetaData() {
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
}
