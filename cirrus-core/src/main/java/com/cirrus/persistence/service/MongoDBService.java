package com.cirrus.persistence.service;

import com.cirrus.persistence.dao.IMetaDataDAO;
import com.cirrus.persistence.dao.MetaDataDAO;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.jongo.Jongo;

import java.net.UnknownHostException;

public class MongoDBService implements IMongoDBService {

    //==================================================================================================================
    // Constants
    //==================================================================================================================

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private final DB database;
    private final IMetaDataDAO metaDataDAO;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public MongoDBService(final String host, final int port) throws UnknownHostException {
        final MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://" + host + ":" + port));
        this.database = mongoClient.getDB(CIRRUS_DATABASE_NAME);
        final Jongo jongo = new Jongo(this.database);

        // dao creation
        this.metaDataDAO = new MetaDataDAO(jongo.getCollection(CIRRUS_META_DATA_COLLECTION));
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public DB getDatabase() {
        return this.database;
    }

    @Override
    public IMetaDataDAO getMetaDataDAO() {
        return this.metaDataDAO;
    }
}
