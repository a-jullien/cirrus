package com.cirrus.osgi.service.dropbox;

import com.cirrus.data.ICirrusData;
import com.cirrus.data.impl.CirrusFileData;
import com.cirrus.data.impl.CirrusFolderData;
import com.cirrus.osgi.extension.AuthenticationException;
import com.cirrus.osgi.extension.ICirrusStorageService;
import com.cirrus.osgi.extension.ServiceRequestFailedException;
import com.dropbox.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DropBoxStorageService implements ICirrusStorageService {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private String token;
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
    public void setAuthenticationToken(final String token) {
        this.token = token;

        final DbxAuthInfo authInfo = new DbxAuthInfo(this.token, DbxHost.Default);
        final String userLocale = Locale.getDefault().toString();
        final DbxRequestConfig requestConfig = new DbxRequestConfig("dropbox-bundle-configuration", userLocale);
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
        if (this.token == null) {
            throw new AuthenticationException("The authentication token is not valid");
        }
    }
}
