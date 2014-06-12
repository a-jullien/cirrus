package com.cirrus.persistence;

import com.cirrus.persistence.service.IMongoDBService;
import com.cirrus.persistence.service.MongoDBService;
import com.cirrus.server.configuration.CirrusProperties;
import com.mongodb.DB;
import org.junit.Test;

import java.io.IOException;

import static com.mongodb.util.MyAsserts.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class TestMongoDBService {

    @Test
    public void shouldSuccessWhenGoodURIIsProvided() throws IOException {
        final CirrusProperties cirrusProperties = new CirrusProperties();
        final String databaseURL = cirrusProperties.getProperty(CirrusProperties.MONGODB_URL);

        final IMongoDBService mongoDBService = new MongoDBService(databaseURL);
        final DB database = mongoDBService.getDatabase();
        assertNotNull(database);
        assertEquals(IMongoDBService.CIRRUS_DATABASE_NAME, database.getName());
    }
}
