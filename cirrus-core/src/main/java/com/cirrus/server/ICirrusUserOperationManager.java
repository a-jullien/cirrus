/*
 * *
 *  * Copyright (c) 2014 Antoine Jullien
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.cirrus.server;

import com.cirrus.model.data.ICirrusMetaData;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ICirrusUserOperationManager {

    /**
     * Creates a new directory from the specified virtual path
     */
    void createDirectory(String virtualPath) throws ExecutionException;

    /**
     * transfer file operation
     */
    void transferFile(final String filePath, final long fileSize, final InputStream inputStream) throws ExecutionException;

    /**
     * Delete a cirrus from the path
     */
    void delete(final String path) throws ExecutionException;

    /**
     * Returns the list all available data for a specified path
     */
    List<ICirrusMetaData> listCirrusData(String virtualPath);
}
