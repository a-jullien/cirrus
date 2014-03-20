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
import com.cirrus.data.ICirrusData;
import com.cirrus.data.impl.CirrusFolderData;
import com.cirrus.distribution.scheduler.ActionType;
import com.cirrus.server.osgi.extension.ICirrusStorageService;

public class DeleteAction extends AbstractAction<ICirrusData> {

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private final String path;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public DeleteAction(final ICirrusAgent cirrusAgent, final String path) {
        super(cirrusAgent, ActionType.CREATE_DIRECTORY);
        this.path = path;
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Override
    public ICirrusData call() throws Exception {
        final ICirrusAgent cirrusAgent = this.getCirrusAgent();
        final ICirrusStorageService storageService = cirrusAgent.getStorageService();
        return storageService.delete(this.path);
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================

}
