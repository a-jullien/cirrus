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

import com.cirrus.data.ICirrusMetaData;

public class CirrusMetaData implements ICirrusMetaData {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private String name;
    private String mediaType;
    private long creationDate;
    private String cirrusAgentType;
    private String cirrusAgentId;
    private String localPath;
    private String virtualPath;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public CirrusMetaData() {
        super();
    }

    //==================================================================================================================
    // Getters
    //==================================================================================================================
    public String getName() {
        return name;
    }

    public String getMediaType() {
        return mediaType;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public String getCirrusAgentType() {
        return cirrusAgentType;
    }

    public String getCirrusAgentId() {
        return cirrusAgentId;
    }

    public String getLocalPath() {
        return localPath;
    }

    public String getVirtualPath() {
        return virtualPath;
    }

    //==================================================================================================================
    // Setters
    //==================================================================================================================
    public void setName(final String name) {
        this.name = name;
    }

    public void setMediaType(final String mediaType) {
        this.mediaType = mediaType;
    }

    public void setCreationDate(final long creationDate) {
        this.creationDate = creationDate;
    }

    public void setCirrusAgentType(final String cirrusAgentType) {
        this.cirrusAgentType = cirrusAgentType;
    }

    public void setCirrusAgentId(final String cirrusAgentId) {
        this.cirrusAgentId = cirrusAgentId;
    }

    public void setLocalPath(final String localPath) {
        this.localPath = localPath;
    }

    public void setVirtualPath(final String virtualPath) {
        this.virtualPath = virtualPath;
    }
}
