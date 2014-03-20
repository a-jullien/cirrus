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
        assertNotNull(cirrusData.getName());
        assertEquals("aFile", cirrusData.getName());
        assertEquals("/tmp/aFile", cirrusData.getPath());
        assertEquals(42, cirrusData.getSize());
    }

    @Test
    public void shouldCreateSuccessfullyCirrusFolderData() {
        final ICirrusData cirrusData = new CirrusFolderData("/tmp/aFolder");
        assertTrue(cirrusData.getCreationTime() > 0);
        assertNotNull(cirrusData.getName());
        assertEquals("aFolder", cirrusData.getName());
        assertEquals("/tmp/aFolder", cirrusData.getPath());
        assertEquals(0, cirrusData.getSize());
    }
}
