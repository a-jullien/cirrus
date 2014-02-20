package com.cirrus.agent;

import com.cirrus.agent.impl.CirrusAgent;
import com.cirrus.server.exception.CirrusAgentInstallationException;
import org.osgi.framework.Bundle;

public class FakeCirrusAgent extends CirrusAgent {

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public FakeCirrusAgent(final Bundle bundle) throws CirrusAgentInstallationException {
        super(bundle);
    }
}
