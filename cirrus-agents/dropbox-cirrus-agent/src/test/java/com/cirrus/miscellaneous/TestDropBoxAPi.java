package com.cirrus.miscellaneous;

import com.dropbox.core.*;
import com.dropbox.core.json.JsonReader;

import java.util.Locale;

public class TestDropBoxAPi {

    public static void main(final String[] args) throws DbxException, JsonReader.FileLoadException {

        if (args.length == 0) {
            System.out.println("Please specify the authentication file");
            return;
        }

        final DbxAuthInfo authInfo = DbxAuthInfo.Reader.readFromFile(args[0]);

        final String userLocale = Locale.getDefault().toString();
        final DbxRequestConfig requestConfig = new DbxRequestConfig("examples-account-info", userLocale);
        final DbxClient dbxClient = new DbxClient(requestConfig, authInfo.accessToken, authInfo.host);

        try {
            final DbxAccountInfo dbxAccountInfo = dbxClient.getAccountInfo();
            System.out.println("User's account info: " + dbxAccountInfo.toStringMultiline());

        }
        catch (final DbxException ex) {
            ex.printStackTrace();
            System.err.println("Error in getAccountInfo(): " + ex.getMessage());
            System.exit(1);
        }
    }
}
