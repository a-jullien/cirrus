package com.cirrus.persistence;

import com.cirrus.persistence.service.MongoDBService;
import com.cirrus.persistence.service.IMongoDBService;
import com.mongodb.DB;
import org.junit.Test;

import java.net.UnknownHostException;

import static com.mongodb.util.MyAsserts.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class TestMongoDBService {

    @Test
    public void shouldSuccessWhenGoodURIIsProvided() throws UnknownHostException {
        final IMongoDBService mongoDBService = new MongoDBService("127.0.0.1", 22222);
        final DB database = mongoDBService.getDatabase();
        assertNotNull(database);
        assertEquals(IMongoDBService.CIRRUS_DATABASE_NAME, database.getName());
    }
}
