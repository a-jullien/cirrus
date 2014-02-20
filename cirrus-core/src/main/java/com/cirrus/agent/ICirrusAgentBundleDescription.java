package com.cirrus.agent;

import java.io.Serializable;

public interface ICirrusAgentBundleDescription extends Serializable {

    String getName();
    String getDescription();
    String getVersion();
    String getVendor();
}
