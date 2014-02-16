package com.cirrus.miscellaneous;

import com.dropbox.core.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Locale;


public class DropBoxAuthInitializer {

    public static void main(final String[] args) throws IOException, DbxException {
        if (args.length != 3) {
            printHelp(System.out);
            return;
        }

        final String appKey = args[0];
        final String appSecret = args[1];
        final String authFileOutput = args[2];

        final DbxAppInfo appInfo = new DbxAppInfo(appKey, appSecret);
        final DbxRequestConfig config = new DbxRequestConfig(
                "DropBox cirrus agent", Locale.getDefault().toString());
        final DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);

        final String authorizedURL = webAuth.start();
        System.out.println("1. Go to: " + authorizedURL);
        System.out.println("2. Click \"Allow\" (you might have to log in first)");
        System.out.println("3. Copy the authorization code.");
        final String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();

        try {
            final DbxAuthFinish authFinish = webAuth.finish(code);
            System.out.println("Authorization complete.");
            System.out.println("- User ID: " + authFinish.userId);
            System.out.println("- Access Token: " + authFinish.accessToken);

            // Save auth information to output file.
            final DbxAuthInfo authInfo = new DbxAuthInfo(authFinish.accessToken, appInfo.host);
            try {
                DbxAuthInfo.Writer.writeToFile(authInfo, authFileOutput);
                System.out.println("Saved authorization information to \"" + authFileOutput + "\".");
            } catch (final IOException ex) {
                System.err.println("Error saving to <auth-file-out>: " + ex.getMessage());
                System.err.println("Dumping to stderr instead:");
                DbxAuthInfo.Writer.writeToStream(authInfo, System.err);
                System.exit(1);
            }

        } catch (final DbxException ex) {
            System.err.println("Error in DbxWebAuth.start: " + ex.getMessage());
            System.exit(1);
        }
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private static void printHelp(final PrintStream out) {
        out.println("Usage: COMMAND <appKey> <appSecret> <auth-file-output>");
        out.println("");
        out.println("<app-key>: the application key provided by dropbox account");
        out.println("<app-secret>: the application secret passsword provided by dropbox account");
        out.println("<auth-file-output>: If authorization is successful, the resulting API");
        out.println("  access token will be saved to this file, which can then be used with");
        out.println("  other example programs.");
        out.println("");
    }

}
