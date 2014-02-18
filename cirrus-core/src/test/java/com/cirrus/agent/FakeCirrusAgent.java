package com.cirrus.agent;

import com.cirrus.agent.impl.CirrusAgent;

public class FakeCirrusAgent extends CirrusAgent {

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public FakeCirrusAgent(final IStorageServiceVendor storageServiceVendor) {
        super(storageServiceVendor);
    }
}
