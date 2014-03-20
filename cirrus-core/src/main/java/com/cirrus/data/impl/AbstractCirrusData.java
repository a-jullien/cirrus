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

package com.cirrus.data.impl;

import com.cirrus.data.ICirrusData;
import com.cirrus.utils.DataUtils;

public abstract class AbstractCirrusData implements ICirrusData {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final long serialVersionUID = -1754037774277907007L;

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private final long creationTime;
    private final String localPath;
    private final String name;
    private final DataType dataType;
    private final long size;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    protected AbstractCirrusData(final String localPath, final DataType dataType, final long size) {
        this.localPath = localPath;
        this.dataType = dataType;
        this.creationTime = System.currentTimeMillis();
        this.name = DataUtils.extractFileNameFromPath(localPath);
        this.size = size;
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Override
    public DataType getDataType() {
        return this.dataType;
    }

    @Override
    public long getCreationTime() {
        return this.creationTime;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPath() {
        return this.localPath;
    }

    @Override
    public long getSize() {
        return size;
    }

    //==================================================================================================================
    // Override
    //==================================================================================================================

    @Override
    public String toString() {
        return this.getPath();
    }
}
