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

package com.cirrus.server.osgi.service.amazon.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.cirrus.agent.authentication.impl.AccessKeyPasswordTrustedToken;
import com.cirrus.data.ICirrusData;
import com.cirrus.data.impl.CirrusFileData;
import com.cirrus.data.impl.CirrusFolderData;
import com.cirrus.server.osgi.extension.AbstractStorageService;
import com.cirrus.server.osgi.extension.ServiceRequestFailedException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AmazonS3StorageService extends AbstractStorageService<AccessKeyPasswordTrustedToken> {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final String BUCKET_NAME = "cirrus-bucket";
    private static final String SEPARATOR = "/";

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private AmazonS3Client amazonS3Client;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public AmazonS3StorageService() {
        super();
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Override
    public void authenticate(final AccessKeyPasswordTrustedToken trustedToken) {
        final String accessKey = trustedToken.getAccessKey();
        final String accessSecret = trustedToken.getAccessPassword();
        final AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
        this.amazonS3Client = new AmazonS3Client(credentials);
        this.createBucketIfNotExist();
    }

    @Override
    public void shutdown() {
        this.amazonS3Client.shutdown();
    }

    @Override
    public String getAccountName() throws ServiceRequestFailedException {
        final Owner s3AccountOwner = this.amazonS3Client.getS3AccountOwner();
        return s3AccountOwner.getDisplayName();
    }

    @Override
    public long getTotalSpace() throws ServiceRequestFailedException {
        throw new ServiceRequestFailedException("Not Yet Implemented");
    }

    @Override
    public long getUsedSpace() throws ServiceRequestFailedException {
        throw new ServiceRequestFailedException("Not Yet Implemented");
    }

    @Override
    public List<ICirrusData> list(final String path) throws ServiceRequestFailedException {
        final List<ICirrusData> content = new ArrayList<>();

        final ListObjectsRequest listObjectsRequest = buildObjectRequest(path);

        ObjectListing objectListing;

        do {
            objectListing = this.amazonS3Client.listObjects(listObjectsRequest);
            for (final S3ObjectSummary objectSummary :
                    objectListing.getObjectSummaries()) {
                final String key = objectSummary.getKey();

                System.out.println( " - " + key + "  " +
                        "(size = " + objectSummary.getSize() +
                        ", etag = " + objectSummary.getETag() + ", storageClass = " + objectSummary.getStorageClass() + ")");

                if (path.equals(SEPARATOR)) {
                    // root directory
                    if (!key.contains(SEPARATOR)) {
                        content.add(new CirrusFileData(SEPARATOR + key, objectSummary.getSize()));
                    } else {
                        if (key.indexOf(SEPARATOR) == key.length() -1) {
                            content.add(new CirrusFolderData(key));
                        }
                    }
                } else {
                    final int beginIndex = key.indexOf(SEPARATOR);
                    final String substring = key.substring(beginIndex + 1, key.length());
                    if (!substring.isEmpty()) {
                        System.out.println("key = " + key + " substring = " + substring);


                    }
                }
            }
            listObjectsRequest.setMarker(objectListing.getNextMarker());
        } while (objectListing.isTruncated());

        return content;
    }

    @Override
    public CirrusFolderData createDirectory(final String path) throws ServiceRequestFailedException {
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        final InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

        final String key = path + SEPARATOR;
        final PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, key,
                emptyContent, metadata);

        // Send request to S3 to create folder
        this.amazonS3Client.putObject(putObjectRequest);

        return new CirrusFolderData(key);
    }

    @Override
    public ICirrusData delete(final String path) throws ServiceRequestFailedException {
        this.amazonS3Client.deleteObject(BUCKET_NAME, path);
        return null;
    }

    @Override
    public CirrusFileData transferFile(final String filePath, final long fileSize, final InputStream inputStream) throws ServiceRequestFailedException {
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileSize);
        this.amazonS3Client.putObject(BUCKET_NAME, filePath, inputStream, metadata);
        return new CirrusFileData(SEPARATOR + filePath, fileSize);
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private void createBucketIfNotExist() {
        if (!this.amazonS3Client.doesBucketExist(BUCKET_NAME)) {
            this.amazonS3Client.createBucket(BUCKET_NAME);
        }
    }

    private ListObjectsRequest buildObjectRequest(final String path) {
        if (path.equals(SEPARATOR)) {
            return new ListObjectsRequest()
                    .withBucketName(BUCKET_NAME);
        } else {
            return new ListObjectsRequest()
                    .withBucketName(BUCKET_NAME).withPrefix(key(path));
        }
    }

    private static String key(final String path) {
        final int index = path.indexOf(SEPARATOR);
        if (index == 0) {
            return path.substring(1, path.length() - 1);
        } else {
            return path;
        }
    }
}
