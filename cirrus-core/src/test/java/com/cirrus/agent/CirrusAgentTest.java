package com.cirrus.agent;

import com.cirrus.agent.impl.StorageServiceVendor;
import com.cirrus.agent.impl.UUIDBasedCirrusAgentIdentifier;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class CirrusAgentTest {

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Test(expected = IllegalArgumentException.class)
    public void shouldHaveErrorWhenCreateCirrusAgentIdentifierFromBadExternalRepresentation() {
        new UUIDBasedCirrusAgentIdentifier("Bad Representation");
    }

    @Test
    public void shouldCreateSuccessfullyDefaultCirrusAgentIdentifier() {
        final ICirrusAgentIdentifier cirrusAgentIdentifier = new UUIDBasedCirrusAgentIdentifier();
        assertNotNull(cirrusAgentIdentifier.toExternal());
    }

    @Test
    public void shouldCreateSuccessfullyCirrusAgentIdentifierFromExternalRepresentation() {
        final ICirrusAgentIdentifier cirrusAgentIdentifier =
                new UUIDBasedCirrusAgentIdentifier("1f553132-43e0-4fd3-9e50-fcf1d0adc978");
        final String externalRepresentation = cirrusAgentIdentifier.toExternal();
        assertNotNull(externalRepresentation);
        assertEquals("1f553132-43e0-4fd3-9e50-fcf1d0adc978", externalRepresentation);
    }

    @Test
    public void shouldEqualsForCirrusAgentIdentifierSatisfied() {
        final ICirrusAgentIdentifier cirrusAgentIdentifier1 =
                new UUIDBasedCirrusAgentIdentifier("1f553132-43e0-4fd3-9e50-fcf1d0adc978");

        final ICirrusAgentIdentifier cirrusAgentIdentifier2 =
                new UUIDBasedCirrusAgentIdentifier("1f553132-43e0-4fd3-9e50-fcf1d0adc978");

        assertEquals(cirrusAgentIdentifier1, cirrusAgentIdentifier2);
    }

    @Test
    public void shouldCreateSuccessfullyStorageServiceVendor() {
        final IStorageServiceVendor storageServiceVendor = this.giveMeAStorageServiceVendor();
        assertEquals("DropBox", storageServiceVendor.getName());
        assertEquals("1.0", storageServiceVendor.getVersion());
        assertEquals("DropBox & Co", storageServiceVendor.getVendor());
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================

    private IStorageServiceVendor giveMeAStorageServiceVendor() {
        return new StorageServiceVendor("DropBox", "1.0", "DropBox & Co");
    }
}
