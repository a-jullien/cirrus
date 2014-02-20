package com.cirrus.osgi.service.dropbox;

import com.cirrus.osgi.extension.AuthenticationException;
import com.cirrus.osgi.extension.ICirrusStorageService;
import com.cirrus.osgi.extension.ServiceRequestFailedException;
import com.dropbox.core.*;

import java.util.Locale;

public class DropBoxStorageService implements ICirrusStorageService {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private String token;

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
    }

    @Override
    public String getAccountInformation() throws AuthenticationException, ServiceRequestFailedException {
        this.checkAuthenticationToken();
        final DbxAuthInfo authInfo = new DbxAuthInfo(this.token, DbxHost.Default);
        try {
            final String userLocale = Locale.getDefault().toString();
            final DbxRequestConfig requestConfig = new DbxRequestConfig("account-info", userLocale);
            final DbxClient dbxClient = new DbxClient(requestConfig, authInfo.accessToken, authInfo.host);

            final DbxAccountInfo dbxAccountInfo = dbxClient.getAccountInfo();
            return dbxAccountInfo.toStringMultiline();

        } catch (final DbxException e) {
            throw new ServiceRequestFailedException(e);
        }
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
