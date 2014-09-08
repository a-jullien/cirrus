/*
 * *
 *  * Copyright (c) 2014 Antoine Jullien
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.cirrus.server.osgi.service.box;

import com.box.boxjavalibv2.BoxClient;
import com.box.boxjavalibv2.dao.BoxFile;
import com.box.boxjavalibv2.dao.BoxOAuthToken;
import com.box.boxjavalibv2.dao.BoxUser;
import com.box.boxjavalibv2.exceptions.AuthFatalFailureException;
import com.box.boxjavalibv2.exceptions.BoxServerException;
import com.box.boxjavalibv2.jsonparsing.BoxJSONParser;
import com.box.boxjavalibv2.jsonparsing.BoxResourceHub;
import com.box.boxjavalibv2.requests.requestobjects.BoxFileUploadRequestObject;
import com.box.boxjavalibv2.requests.requestobjects.BoxOAuthRequestObject;
import com.box.restclientv2.exceptions.BoxRestException;
import com.cirrus.agent.authentication.impl.AccessKeyPasswordAuthenticator;
import com.cirrus.model.data.ICirrusData;
import com.cirrus.model.data.impl.CirrusFileData;
import com.cirrus.model.data.impl.CirrusFolderData;
import com.cirrus.server.osgi.extension.AbstractStorageService;
import com.cirrus.server.osgi.extension.AuthenticationException;
import com.cirrus.server.osgi.extension.ServiceRequestFailedException;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class BoxStorageService extends AbstractStorageService<AccessKeyPasswordAuthenticator> {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private BoxClient boxClient;


    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    public BoxStorageService() {
        super();
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Override
    public void authenticate(final AccessKeyPasswordAuthenticator trustedToken) throws AuthenticationException {
        try {
            final String url = "https://www.box.com/api/oauth2/authorize?response_type=code&client_id=" + trustedToken.getAccessKey();
            Desktop.getDesktop().browse(java.net.URI.create(url));
            final String code = getCode();

            this.boxClient = createBoxClient(code, trustedToken.getAccessKey(), trustedToken.getAccessPassword());
        } catch (final BoxRestException | BoxServerException | AuthFatalFailureException | IOException e) {
            throw new AuthenticationException(e);
        }
    }

    @Override
    public void shutdown() {
        // do nothing here
    }

    @Override
    public String getAccountName() throws ServiceRequestFailedException {
        final BoxUser currentUser;
        try {
            currentUser = this.boxClient.getUsersManager().getCurrentUser(null);
            return currentUser.getName();

        } catch (BoxRestException | BoxServerException | AuthFatalFailureException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    @Override
    public long getTotalSpace() throws ServiceRequestFailedException {
        final BoxUser currentUser;
        try {
            currentUser = this.boxClient.getUsersManager().getCurrentUser(null);
            return currentUser.getSpaceAmount().longValue();

        } catch (BoxRestException | BoxServerException | AuthFatalFailureException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    @Override
    public long getUsedSpace() throws ServiceRequestFailedException {
        final BoxUser currentUser;
        try {
            currentUser = this.boxClient.getUsersManager().getCurrentUser(null);
            return currentUser.getSpaceUsed().longValue();

        } catch (BoxRestException | BoxServerException | AuthFatalFailureException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    @Override
    public List<ICirrusData> list(final String path) throws ServiceRequestFailedException {
        return null;
    }

    @Override
    public CirrusFolderData createDirectory(final String path) throws ServiceRequestFailedException {
        return null;
    }

    @Override
    public ICirrusData delete(final String path) throws ServiceRequestFailedException {
        return null;
    }

    @Override
    public CirrusFileData transferFile(final String filePath, final long fileSize, final InputStream inputStream) throws ServiceRequestFailedException {
        try {
            final BoxFileUploadRequestObject requestObject = BoxFileUploadRequestObject.uploadFileRequestObject(null, null, null);
            final BoxFile boxFile = this.boxClient.getFilesManager().uploadFile(requestObject);
            return new CirrusFileData(filePath, boxFile.getSize().longValue());
        } catch (BoxRestException | BoxServerException | AuthFatalFailureException | InterruptedException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================

    private static BoxClient createBoxClient(final String code, final String clientId, final String secret) throws BoxRestException, BoxServerException, AuthFatalFailureException {
        final BoxResourceHub hub = new BoxResourceHub();
        final BoxJSONParser parser = new BoxJSONParser(hub);
        final BoxClient client = new BoxClient(clientId, secret, hub, parser);
        final BoxOAuthRequestObject oAuthRequestObject = BoxOAuthRequestObject.createOAuthRequestObject(code, clientId, secret, null);
        final BoxOAuthToken bt = client.getOAuthManager().createOAuth(oAuthRequestObject);
        client.authenticate(bt);
        return client;
    }

    // TODO Remove this code
    private static String getCode() throws IOException {

        final ServerSocket serverSocket = new ServerSocket(4000);
        final Socket socket = serverSocket.accept();
        final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (true) {
            String code = "";
            try {
                final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                out.write("HTTP/1.1 200 OK\r\n");
                out.write("Content-Type: text/html\r\n");
                out.write("\r\n");

                code = in.readLine();
                System.out.println(code);
                final String match = "code";
                final int loc = code.indexOf(match);

                if (loc > 0) {
                    final int httpstr = code.indexOf("HTTP") - 1;
                    code = code.substring(code.indexOf(match), httpstr);
                    final String[] parts = code.split("=");
                    code = parts[1];
                    out.write("Now return to command line to see the output of the HelloWorld sample app.");
                } else {
                    // It doesn't have a code
                    out.write("Code not found in the URL!");
                }

                out.close();

                return code;
            } catch (final IOException e) {
                System.exit(1);
                break;
            }
        }
        return "";
    }

}
