package com.cirrus.data.impl;

public class CirrusFolderData extends AbstractCirrusData {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final long serialVersionUID = -3267931774542563900L;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public CirrusFolderData(final String localPath) {
        super(localPath);
    }

    @Override
    public String toString() {
        return "folder " + super.toString();
    }
}
