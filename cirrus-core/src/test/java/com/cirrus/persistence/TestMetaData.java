package com.cirrus.persistence;

import com.cirrus.data.ICirrusMetaData;
import com.cirrus.data.impl.CirrusMetaData;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class TestMetaData {

    @Test
    public void shouldHaveCirrusMetaDataMappedFromJson() throws IOException {
        System.out.println(System.currentTimeMillis());
        final URL jsonMetaData = this.getClass().getResource("/persistence/data/cirrus-meta-data.json");
        assertNotNull(jsonMetaData);

        final ObjectMapper objectMapper = new ObjectMapper();
        final ICirrusMetaData cirrusMetaData = objectMapper.readValue(jsonMetaData, CirrusMetaData.class);
        assertNotNull(cirrusMetaData);
        assertEquals("myFile.txt", cirrusMetaData.getName());
        assertEquals("text/plain", cirrusMetaData.getMediaType());
        assertEquals(1393367761472L, cirrusMetaData.getCreationDate());
        assertEquals("dropbox", cirrusMetaData.getCirrusAgentType());
        assertEquals("1f553132-43e0-4fd3-9e50-fcf1d0adc978", cirrusMetaData.getCirrusAgentId());
        assertEquals("/", cirrusMetaData.getLocalPath());
        assertEquals("/home/virtual/myProject", cirrusMetaData.getVirtualPath());
    }
}
