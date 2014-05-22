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

package com.cirrus.distribution.scheduler;

import com.cirrus.agent.ICirrusAgent;
import com.cirrus.distribution.scheduler.exception.CirrusAgentCannotBeFoundException;
import com.cirrus.server.ICirrusAgentManager;
import com.cirrus.server.exception.ServerNotStartedException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CirrusScheduler {

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private final ICirrusAgentManager cirrusAgentAdministration;
    private final ExecutorService executor;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public CirrusScheduler(final ICirrusAgentManager cirrusAgentAdministration) {
        this.cirrusAgentAdministration = cirrusAgentAdministration;
        this.executor = Executors.newSingleThreadExecutor();
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    public ICirrusAgent findAgent() throws CirrusAgentCannotBeFoundException, ServerNotStartedException {
        final List<ICirrusAgent> allAgents = this.cirrusAgentAdministration.listCirrusAgents();
        if (allAgents.size() == 0) {
            throw new CirrusAgentCannotBeFoundException("The scheduler is not able to find agents");
        } else {
            // TODO heuristics
            return allAgents.get(0);
        }
    }



    public <T> T scheduleAction(final IUserAction<T> action) throws ExecutionException {
        final Future<T> result = this.executor.submit(action);
        try {
            return result.get();
        } catch (final InterruptedException e) {
            throw new ExecutionException(e);
        }
    }
}
