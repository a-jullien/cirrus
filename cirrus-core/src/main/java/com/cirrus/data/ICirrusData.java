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

package com.cirrus.data;

import com.cirrus.data.impl.DataType;

import java.io.Serializable;

public interface ICirrusData extends Serializable {

    /**
     * Returns the type of the data (file or directory)
     */
    DataType getDataType();

    /**
     * Returns the creation time of the data. Never <code>null</code>
     */
    long getCreationTime();

    /**
     * Returns data name. Never <code>null</code>
     */
    String getName();

    /**
     * Returns the real path of the data. Never <code>null</code>
     */
    String getPath();
}
