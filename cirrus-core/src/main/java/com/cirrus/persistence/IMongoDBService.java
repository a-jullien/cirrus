package com.cirrus.persistence;

import com.mongodb.DB;

public interface IMongoDBService {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    final static String CIRRUS_DATABASE_NAME = "cirrus";


    /**
     * Returns the database provided by mongoDB service
     */
    DB getDatabase();
}
