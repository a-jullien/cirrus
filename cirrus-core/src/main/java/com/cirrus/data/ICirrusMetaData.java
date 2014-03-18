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

public interface ICirrusMetaData {

    /**
     * Returns the data type
     */
    DataType getDataType();

    /**
     * Returns the unique identifier of the meta data
     */
    String getId();

    /**
     * Returns the name of the meta data
     */
    String getName();

    /**
     * Returns the creation date
     */
    long getCreationDate();

    /**
     * Returns the cirrus agent type
     */
    String getCirrusAgentType();

    /**
     * Returns the cirrus agent identifier where the data is stored
     */
    String getCirrusAgentId();

    /**
     * Returns the real local path of the data
     */
    String getLocalPath();

    /**
     * Returns the virtual path of the data
     */
    String getVirtualPath();

    /**
     * Returns the media type
     */
    String getMediaType();
}
