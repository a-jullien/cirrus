package com.cirrus.server;

import com.cirrus.server.exception.StartCirrusServerException;
import com.cirrus.server.exception.StopCirrusServerException;
import com.cirrus.server.impl.OSGIBasedCirrusServer;
import org.junit.Test;

public class OSGICirrusServerTest {

    @Test
    public void shouldSuccessfullyCreateAndStartCirrusServer() throws StartCirrusServerException, StopCirrusServerException {
        final ICirrusServer cirrusServer = new OSGIBasedCirrusServer();
        cirrusServer.start();

        cirrusServer.stop();
    }
}
