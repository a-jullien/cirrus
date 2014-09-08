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

package com.cirrus.model.data.impl;

public class CirrusFileData extends AbstractCirrusData {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final long serialVersionUID = -6396844571673730055L;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public CirrusFileData(final String localPath, final long size) {
        super(localPath, DataType.FILE, size);
    }

    //==================================================================================================================
    // Override
    //==================================================================================================================

    @Override
    public String toString() {
        return "file " + super.toString();
    }
}
