package com.cirrus.data;

import com.cirrus.data.impl.CirrusFileData;
import com.cirrus.data.impl.CirrusFolderData;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class TestCirrusDataManagement {

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Test(expected = IllegalArgumentException.class)
    public void shouldErrorWhenCirrusFileDataHasBadPath() {
        new CirrusFileData("aFile");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldErrorWhenCirrusFolderDataHasBadPath() {
        new CirrusFolderData("aFile");
    }

    @Test
    public void shouldCreateSuccessfullyCirrusFileData() {
        final ICirrusData cirrusData = new CirrusFileData("/tmp/aFile");
        assertTrue(cirrusData.getCreationTime() > 0);
        assertNotNull(cirrusData.getName());
        assertEquals("aFile", cirrusData.getName());
        assertEquals("/tmp/aFile", cirrusData.getPath());
    }

    @Test
    public void shouldCreateSuccessfullyCirrusFolderData() {
        final ICirrusData cirrusData = new CirrusFileData("/tmp/aFolder");
        assertTrue(cirrusData.getCreationTime() > 0);
        assertNotNull(cirrusData.getName());
        assertEquals("aFolder", cirrusData.getName());
        assertEquals("/tmp/aFolder", cirrusData.getPath());
    }
}
