package com.cirrus.persistence.service;

import com.cirrus.persistence.service.IMongoDBService;
import com.mongodb.*;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.net.UnknownHostException;

public class MongoDBService implements IMongoDBService {

    //==================================================================================================================
    // Constants
    //==================================================================================================================

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private final DB database;
    private final MongoCollection cirrusMetaDataCollection;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public MongoDBService(final String host, final int port) throws UnknownHostException {
        final MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://" + host + ":" + port));
        this.database = mongoClient.getDB(CIRRUS_DATABASE_NAME);

        final Jongo jongo = new Jongo(database);
        this.cirrusMetaDataCollection = jongo.getCollection(CIRRUS_META_DATA_COLLECTION);
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public DB getDatabase() {
        return this.database;
    }

    @Override
    public MongoCollection getMetaDataCollection() {
        return this.cirrusMetaDataCollection;
    }
}
