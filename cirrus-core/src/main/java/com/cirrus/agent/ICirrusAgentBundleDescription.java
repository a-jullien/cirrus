package com.cirrus.agent;

import java.io.Serializable;

public interface ICirrusAgentBundleDescription extends Serializable {

    /**
     * Returns the name of the bundle
     */
    String getName();

    /**
     * Returns the textual description of the bundle
     */
    String getDescription();

    /**
     * Returns the bundle version
     */
    String getVersion();

    /**
     * Returns the bundle vendor
     */
    String getVendor();
}
