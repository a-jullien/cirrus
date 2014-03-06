package com.cirrus.persistence.service;

import com.cirrus.persistence.dao.meta.IMetaDataDAO;
import com.mongodb.DB;

public interface IMongoDBService {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    final static String CIRRUS_DATABASE_NAME = "cirrus";
    final static String CIRRUS_META_DATA_COLLECTION = "cirrusMetaData";


    /**
     * Returns the database provided by mongoDB service
     */
    DB getDatabase();

    /**
     * Returns the dao for cirrus meta data
     */
    IMetaDataDAO getMetaDataDAO();
}
