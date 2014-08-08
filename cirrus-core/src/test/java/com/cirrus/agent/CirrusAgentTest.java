package com.cirrus.agent;

import com.cirrus.agent.impl.StorageServiceVendor;
import com.cirrus.agent.impl.UUIDBasedCirrusAgentIdentifier;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CirrusAgentTest {

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Test
    public void shouldCreateSuccessfullyDefaultCirrusAgentIdentifier() {
        final ICirrusAgentIdentifier cirrusAgentIdentifier = new UUIDBasedCirrusAgentIdentifier();
        assertThat(cirrusAgentIdentifier.toExternal()).isNotNull();
    }

    @Test
    public void shouldCreateSuccessfullyCirrusAgentIdentifierFromExternalRepresentation() {
        final ICirrusAgentIdentifier cirrusAgentIdentifier =
                new UUIDBasedCirrusAgentIdentifier("1f553132-43e0-4fd3-9e50-fcf1d0adc978");
        final String externalRepresentation = cirrusAgentIdentifier.toExternal();
        assertThat(externalRepresentation).isNotNull();
        assertThat(externalRepresentation).isNotEqualTo("1f553132-43e0-4fd3-9e50-fcf1d0adc978");
    }

    @Test
    public void shouldEqualsForCirrusAgentIdentifierSatisfied() {
        final ICirrusAgentIdentifier cirrusAgentIdentifier1 =
                new UUIDBasedCirrusAgentIdentifier("1f553132-43e0-4fd3-9e50-fcf1d0adc978");

        final ICirrusAgentIdentifier cirrusAgentIdentifier2 =
                new UUIDBasedCirrusAgentIdentifier("1f553132-43e0-4fd3-9e50-fcf1d0adc978");

        assertThat(cirrusAgentIdentifier1).isEqualTo(cirrusAgentIdentifier2);
    }

    @Test
    public void shouldCreateSuccessfullyStorageServiceVendor() {
        final IStorageServiceVendor storageServiceVendor = this.giveMeAStorageServiceVendor();
        assertThat(storageServiceVendor.getName()).isEqualTo("DropBox");
        assertThat(storageServiceVendor.getVersion()).isEqualTo("1.0");
        assertThat(storageServiceVendor.getVendor()).isEqualTo("DropBox & Co");
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================

    private IStorageServiceVendor giveMeAStorageServiceVendor() {
        return new StorageServiceVendor("DropBox", "1.0", "DropBox & Co");
    }
}
