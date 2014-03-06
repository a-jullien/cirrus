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

package com.cirrus.osgi.service.amazon.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Owner;
import com.cirrus.data.ICirrusData;
import com.cirrus.osgi.extension.AbstractStorageService;
import com.cirrus.osgi.extension.AuthenticationException;
import com.cirrus.osgi.extension.ServiceRequestFailedException;

import java.util.ArrayList;
import java.util.List;

public class AmazonS3StorageService extends AbstractStorageService {

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
