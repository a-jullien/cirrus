package com.cirrus.persistence.impl;

import com.cirrus.persistence.IMongoDBService;
import com.mongodb.*;

import java.net.UnknownHostException;

public class MongoDBService implements IMongoDBService {

    //==================================================================================================================
    // Constants
    //==================================================================================================================

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private final DB database;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public MongoDBService(final String externalURI) throws UnknownHostException {
        final MongoClient mongoClient = new MongoClient(new MongoClientURI(externalURI));
        this.database = mongoClient.getDB(CIRRUS_DATABASE_NAME);
    }

    @Override
    public DB getDatabase() {
        return this.database;
    }
}
