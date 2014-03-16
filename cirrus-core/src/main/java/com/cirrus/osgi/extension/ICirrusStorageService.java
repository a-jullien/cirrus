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

package com.cirrus.osgi.extension;

import com.cirrus.osgi.agent.authentication.IStorageServiceTrustedToken;
import com.cirrus.data.ICirrusData;
import com.cirrus.osgi.server.ICirrusDataListener;

import java.util.List;

@SuppressWarnings("UnusedDeclaration")
public interface ICirrusStorageService<TrustedToken extends IStorageServiceTrustedToken> {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    String SERVICE_NAME_PROPERTY = "Storage-Service-Name";
    String SERVICE_VERSION_PROPERTY = "Storage-Service-Version";
    String SERVICE_VENDOR_PROPERTY = "Storage-Service-Vendor";
    String SERVICE_CLASS_PROPERTY = "Storage-Service-Class";

    /**
     * Register specified listener in order to notify changes in data management
     */
    void registerListener(final ICirrusDataListener listener);

    /**
     * Unregister specified listener
     */
    void unregisterListener(final ICirrusDataListener listener);

    /**
     * Authenticates from specified authentication mechanism
     */
    void authenticateFrom(final TrustedToken authenticationMechanism);

    /**
     * Returns the account name of the storage service
     */
    String getAccountName() throws AuthenticationException, ServiceRequestFailedException;

    /**
     * Returns the total space
     */
    long getTotalSpace() throws ServiceRequestFailedException;

    /**
     * Returns the used space
     */
    long getUsedSpace() throws ServiceRequestFailedException;

    /**
     * List data from specified path
     */
    List<ICirrusData> list(final String path) throws ServiceRequestFailedException;

}
