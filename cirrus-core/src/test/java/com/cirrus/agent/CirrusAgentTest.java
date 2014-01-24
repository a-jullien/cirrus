package com.cirrus.agent;

import com.cirrus.agent.impl.UUIDBasedCirrusAgentIdentifier;
import junit.framework.Assert;
import org.junit.Test;

public class CirrusAgentTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldHaveErrorWhenCreateCirrusAgentIdentifierFromBadExternalRepresentation() {
        new UUIDBasedCirrusAgentIdentifier("Bad Representation");
    }

    @Test
    public void shouldCreateSuccessfullyDefaultCirrusAgentIdentifier() {
        final ICirrusAgentIdentifier cirrusAgentIdentifier = new UUIDBasedCirrusAgentIdentifier();
        Assert.assertNotNull(cirrusAgentIdentifier.toExternal());
    }

    @Test
    public void shouldCreateSuccessfullyCirrusAgentIdentifierFromExternalRepresentation() {
        final ICirrusAgentIdentifier cirrusAgentIdentifier =
                new UUIDBasedCirrusAgentIdentifier("1f553132-43e0-4fd3-9e50-fcf1d0adc978");
        final String externalRepresentation = cirrusAgentIdentifier.toExternal();
        Assert.assertNotNull(externalRepresentation);
        Assert.assertEquals("1f553132-43e0-4fd3-9e50-fcf1d0adc978", externalRepresentation);
    }
}
