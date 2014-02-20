package com.cirrus.agent.impl;

import com.cirrus.agent.ICirrusAgentBundleDescription;

public class CirrusAgentBundleDescription implements ICirrusAgentBundleDescription {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final long serialVersionUID = 6023780763494571913L;

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private final String name;
    private final String description;
    private final String version;
    private final String vendor;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public CirrusAgentBundleDescription(final String name, final String description, final String version, final String vendor) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.vendor = vendor;
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public String getVendor() {
        return this.vendor;
    }
}
