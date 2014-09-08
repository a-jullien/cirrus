package com.cirrus.persistence.service;

import com.cirrus.persistence.dao.meta.IMetaDataDAO;
import com.cirrus.persistence.dao.meta.impl.MetaDataDAO;
import com.cirrus.persistence.dao.profile.IUserProfileDAO;
import com.cirrus.persistence.dao.profile.impl.UserProfileDAO;
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
    private final IUserProfileDAO profileDAO;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public MongoDBService(final String databaseURI) throws UnknownHostException {
        final MongoClientURI mongoClientURI = new MongoClientURI(databaseURI);
        final MongoClient mongoClient = new MongoClient(mongoClientURI);
        this.database = mongoClient.getDB(CIRRUS_DATABASE_NAME);
        final Jongo jongo = new Jongo(this.database);

        // dao creation
        this.metaDataDAO = new MetaDataDAO(jongo.getCollection(CIRRUS_META_DATA_COLLECTION));
        this.profileDAO = new UserProfileDAO(jongo.getCollection(CIRRUS_USER_PROFILE_COLLECTION));
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

    @Override
    public IUserProfileDAO getUserProfileDAO() {
        return this.profileDAO;
    }
}

