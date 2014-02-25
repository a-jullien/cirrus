package com.cirrus.persistence;

import com.cirrus.persistence.impl.MongoDBService;
import com.mongodb.DB;
import org.junit.Test;

import java.net.UnknownHostException;

import static com.mongodb.util.MyAsserts.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class TestMongoDBService {

    @Test(expected = IllegalArgumentException.class)
    public void shouldErrorWhenBadURIIsProvided() throws UnknownHostException {
        new MongoDBService("badAddress");
    }

    @Test
    public void shouldSuccessWhenGoodURIIsProvided() throws UnknownHostException {
        final IMongoDBService mongoDBService = new MongoDBService("mongodb://127.0.0.1:33333/cirrus");
        final DB database = mongoDBService.getDatabase();
        assertNotNull(database);
        assertEquals(IMongoDBService.CIRRUS_DATABASE_NAME, database.getName());
    }
}
