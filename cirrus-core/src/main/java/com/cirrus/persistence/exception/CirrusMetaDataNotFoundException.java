package com.cirrus.persistence.exception;

public class CirrusMetaDataNotFoundException extends Exception {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final long serialVersionUID = -4470236553063803757L;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    public CirrusMetaDataNotFoundException(final String cirrusMetaDataId) {
        super("The cirrus meta data identified by <" + cirrusMetaDataId + "> doesn't exist");
    }
}
