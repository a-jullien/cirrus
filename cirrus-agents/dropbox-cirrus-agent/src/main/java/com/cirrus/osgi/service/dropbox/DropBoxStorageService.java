package com.cirrus.osgi.service.dropbox;

import com.cirrus.osgi.extension.ICirrusStorageService;

public class DropBoxStorageService implements ICirrusStorageService {

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public DropBoxStorageService() {
        super();
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public long getAvailableDiskSpace() {
        return 666;
    }
}
