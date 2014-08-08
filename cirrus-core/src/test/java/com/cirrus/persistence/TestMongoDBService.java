package com.cirrus.persistence;

import com.cirrus.persistence.service.IMongoDBService;
import com.cirrus.persistence.service.MongoDBService;
import com.cirrus.server.configuration.CirrusProperties;
import com.mongodb.DB;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TestMongoDBService {

    @Test
    public void shouldSuccessWhenGoodURIIsProvided() throws IOException {
        final CirrusProperties cirrusProperties = new CirrusProperties();
        final String databaseURL = cirrusProperties.getProperty(CirrusProperties.MONGODB_URL);

        final IMongoDBService mongoDBService = new MongoDBService(databaseURL);
        final DB database = mongoDBService.getDatabase();
        assertThat(database).isNotNull();
        assertThat(database.getName()).isEqualTo(IMongoDBService.CIRRUS_DATABASE_NAME);
    }
}
