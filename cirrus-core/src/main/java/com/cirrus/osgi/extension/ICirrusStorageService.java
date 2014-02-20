/**
 * The MIT License (MIT)
 * Copyright (c) 2014 Antoine Jullien
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.cirrus.osgi.extension;

@SuppressWarnings("UnusedDeclaration")
public interface ICirrusStorageService {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    String SERVICE_NAME_PROPERTY = "Storage-Service-Name";
    String SERVICE_VERSION_PROPERTY = "Storage-Service-Version";
    String SERVICE_VENDOR_PROPERTY = "Storage-Service-Vendor";
    String SERVICE_CLASS_PROPERTY = "Storage-Service-Class";

    /**
     * Setter for the trusted token mandatory for a successfully authentication to the storage service
     *
     * @param token the trusted token provided by the service
     */
    void setAuthenticationToken(final String token);

    /**
     * Returns the account information of the storage service
     */
    String getAccountInformation() throws AuthenticationException, ServiceRequestFailedException;
}
