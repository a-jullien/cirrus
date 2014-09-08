package com.cirrus.model.data;

import com.cirrus.model.data.impl.CirrusFileData;
import com.cirrus.model.data.impl.CirrusFolderData;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

public class TestCirrusDataManagement {

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Test(expected = IllegalArgumentException.class)
    public void shouldErrorWhenCirrusFileDataHasBadPath() {
        new CirrusFileData("aFile", 42);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldErrorWhenCirrusFolderDataHasBadPath() {
        new CirrusFolderData("aFile");
    }

    @Test
    public void shouldCreateSuccessfullyCirrusFileData() {
        final ICirrusData cirrusData = new CirrusFileData("/tmp/aFile", 42);
        assertTrue(cirrusData.getCreationTime() > 0);
        assertThat(cirrusData.getName()).isNotNull();
        assertThat(cirrusData.getName()).isEqualTo("aFile");
        assertThat(cirrusData.getPath()).isEqualTo("/tmp/aFile");
        assertThat(cirrusData.getSize()).isEqualTo(42);
    }

    @Test
    public void shouldCreateSuccessfullyCirrusFolderData() {
        final ICirrusData cirrusData = new CirrusFolderData("/tmp/aFolder");
        assertTrue(cirrusData.getCreationTime() > 0);
        assertThat(cirrusData.getName()).isNotNull();
        assertThat(cirrusData.getName()).isEqualTo("aFolder");
        assertThat(cirrusData.getPath()).isEqualTo("/tmp/aFolder");
        assertThat(cirrusData.getSize()).isEqualTo(0);
    }
}
