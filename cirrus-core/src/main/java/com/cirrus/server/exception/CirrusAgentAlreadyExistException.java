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

package com.cirrus.server.exception;

import com.cirrus.agent.ICirrusAgentIdentifier;

public class CirrusAgentAlreadyExistException extends Exception {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final long serialVersionUID = -1392844796809290064L;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public CirrusAgentAlreadyExistException(final ICirrusAgentIdentifier cirrusAgentIdentifier) {
        super("The cirrus agent <" + cirrusAgentIdentifier.toExternal() + "> doesn't exist");
    }
}
