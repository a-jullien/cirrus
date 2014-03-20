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

package com.cirrus.distribution.scheduler.action;

import com.cirrus.agent.ICirrusAgent;
import com.cirrus.data.impl.CirrusFileData;
import com.cirrus.distribution.scheduler.ActionType;
import com.cirrus.server.osgi.extension.ICirrusStorageService;

import java.io.InputStream;

public class TransferFileAction extends AbstractAction<CirrusFileData> {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final String filePath;
    private final long fileSize;
    private final InputStream stream;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public TransferFileAction(final ICirrusAgent cirrusAgent,
                              final String filePath,
                              final long fileSize,
                              final InputStream stream) {
        super(cirrusAgent, ActionType.TRANSFER_FILE);
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.stream = stream;
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public CirrusFileData call() throws Exception {
        final ICirrusAgent cirrusAgent = this.getCirrusAgent();
        final ICirrusStorageService storageService = cirrusAgent.getStorageService();
        return storageService.transferFile(this.filePath, this.fileSize, this.stream);
    }
}
