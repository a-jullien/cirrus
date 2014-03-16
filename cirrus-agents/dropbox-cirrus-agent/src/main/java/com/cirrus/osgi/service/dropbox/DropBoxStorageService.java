/**
 * Copyright (c) 2014 Antoine Jullien
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cirrus.osgi.service.dropbox;

import com.cirrus.data.ICirrusData;
import com.cirrus.data.impl.CirrusFileData;
import com.cirrus.data.impl.CirrusFolderData;
import com.cirrus.osgi.agent.authentication.impl.AccessKeyTrustedToken;
import com.cirrus.osgi.extension.AbstractStorageService;
import com.cirrus.osgi.extension.AuthenticationException;
import com.cirrus.osgi.extension.ServiceRequestFailedException;
import com.dropbox.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DropBoxStorageService extends AbstractStorageService<AccessKeyTrustedToken> {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private DbxClient client;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public DropBoxStorageService() {
        super();
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Override
    public void authenticateFrom(final AccessKeyTrustedToken authenticationMechanism) {
        final DbxAuthInfo authInfo = new DbxAuthInfo(authenticationMechanism.getAccessKey(), DbxHost.Default);
        final String userLocale = Locale.getDefault().toString();
        final DbxRequestConfig requestConfig = new DbxRequestConfig(SERVICE_NAME_PROPERTY, userLocale);
        this.client = new DbxClient(requestConfig, authInfo.accessToken, authInfo.host);
    }

    @Override
    public String getAccountName() throws AuthenticationException, ServiceRequestFailedException {
        this.checkAuthenticationToken();
        try {
            final DbxAccountInfo dbxAccountInfo = this.client.getAccountInfo();
            return dbxAccountInfo.displayName;

        } catch (final DbxException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    @Override
    public long getTotalSpace() throws ServiceRequestFailedException {
        try {
            final DbxAccountInfo dbxAccountInfo = this.client.getAccountInfo();
            return dbxAccountInfo.quota.total;
        } catch (final DbxException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    @Override
    public long getUsedSpace() throws ServiceRequestFailedException {
        try {
            final DbxAccountInfo dbxAccountInfo = this.client.getAccountInfo();
            final long shared = dbxAccountInfo.quota.shared;
            final long normal = dbxAccountInfo.quota.normal;
            return shared + normal;
        } catch (final DbxException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    @Override
    public List<ICirrusData> list(final String path) throws ServiceRequestFailedException {
        final List<ICirrusData> result = new ArrayList<>();

        try {
            final DbxEntry.WithChildren listing = this.client.getMetadataWithChildren("/");
            for (final DbxEntry child : listing.children) {
                final ICirrusData cirrusData;
                final boolean isFile = child.isFile();
                if (isFile) {
                    cirrusData = new CirrusFileData(child.path);
                } else {
                    cirrusData = new CirrusFolderData(child.path);
                }

                result.add(cirrusData);

            }
        } catch (final DbxException e) {
            throw new ServiceRequestFailedException(e);
        }

        return result;
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================

    private void checkAuthenticationToken() throws AuthenticationException {
        if (this.client == null) {
            throw new AuthenticationException("The authentication token is not valid");
        }
    }
}
