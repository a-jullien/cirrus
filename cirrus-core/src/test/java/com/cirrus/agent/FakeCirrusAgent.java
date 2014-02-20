package com.cirrus.agent;

import com.cirrus.agent.impl.CirrusAgent;
import com.cirrus.osgi.extension.ICirrusStorageService;

public class FakeCirrusAgent extends CirrusAgent {

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public FakeCirrusAgent(final ICirrusStorageService storageService, final IStorageServiceVendor storageServiceVendor) {
        super(storageService, storageServiceVendor);
    }
}
