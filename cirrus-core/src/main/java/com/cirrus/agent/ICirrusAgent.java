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

package com.cirrus.agent;

import com.cirrus.server.osgi.extension.ICirrusStorageService;
import com.cirrus.server.exception.StartCirrusAgentException;
import com.cirrus.server.exception.StopCirrusAgentException;
import com.cirrus.server.exception.UninstallCirrusAgentException;

public interface ICirrusAgent {

    /**
     * Returns the unique agent identifier. Never <code>null</code>
     */
    ICirrusAgentIdentifier getIdentifier();

    /**
     * Returns information about the osgi bundle. Never <code>null</code>
     */
    ICirrusAgentBundleDescription getCirrusAgentBundleDescription();

    /**
     * Returns information about vendor of the storage service. Never <code>null</code>
     */
    IStorageServiceVendor getStorageServiceVendor();

    /**
     * Returns the storage service used for easily requesting data
     *
     * @return {@link ICirrusStorageService} service
     */
    ICirrusStorageService getStorageService();

    /**
     * start cirrus agent
     */
    void start() throws StartCirrusAgentException;

    /**
     * stop cirrus agent
     */
    void stop() throws StopCirrusAgentException;

    /**
     * uninstall cirrus agent
     */
    void uninstall() throws UninstallCirrusAgentException;
}
