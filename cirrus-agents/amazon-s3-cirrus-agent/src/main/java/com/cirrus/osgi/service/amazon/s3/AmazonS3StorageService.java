package com.cirrus.osgi.service.amazon.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Owner;
import com.cirrus.data.ICirrusData;
import com.cirrus.osgi.extension.AuthenticationException;
import com.cirrus.osgi.extension.ICirrusStorageService;
import com.cirrus.osgi.extension.ServiceRequestFailedException;

import java.util.ArrayList;
import java.util.List;

public class AmazonS3StorageService implements ICirrusStorageService {

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private AmazonS3Client amazonS3Client;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public AmazonS3StorageService() {
        super();
    }

    @Override
    public void setAuthenticationToken(final String token) {
        final int indexOf = token.indexOf(" ");
        final String accessKey = token.substring(0, indexOf);
        final String accessSecret = token.substring(indexOf + 1, token.length());
        final AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
        this.amazonS3Client = new AmazonS3Client(credentials);
    }

    @Override
    public String getAccountName() throws AuthenticationException, ServiceRequestFailedException {
        final Owner s3AccountOwner = this.amazonS3Client.getS3AccountOwner();
        return s3AccountOwner.getDisplayName();
    }

    @Override
    public long getTotalSpace() throws ServiceRequestFailedException {
        // TODO
        return 0;
    }

    @Override
    public long getUsedSpace() throws ServiceRequestFailedException {
        // TODO
        return 0;
    }

    @Override
    public List<ICirrusData> list(final String path) throws ServiceRequestFailedException {
        // TODO
        return new ArrayList<>();
    }
}
