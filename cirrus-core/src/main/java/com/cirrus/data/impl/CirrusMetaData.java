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
import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;

public class CirrusMetaData implements ICirrusMetaData {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    @Id
    @ObjectId // auto
    private String id;
    private String name;
    private long creationDate;
    private String cirrusAgentType;
    private String cirrusAgentId;
    private String localPath;
    private String virtualPath;
    private DataType dataType;
    private String mediaType;
    private long size;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public CirrusMetaData() {
        super();
    }

    //==================================================================================================================
    // Getters
    //==================================================================================================================

    @Override
    public String getId() {
        return this.id;
    }

    public DataType getDataType() {
        return dataType;
    }

    public String getName() {
        return name;
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

    public String getMediaType() {
        return mediaType;
    }

    public long getSize() {
        return size;
    }

    //==================================================================================================================
    // Setters
    //==================================================================================================================
    public void setId(final String id) {
        this.id = id;
    }

    public void setDataType(final DataType dataType) {
        this.dataType = dataType;
    }

    public void setName(final String name) {
        this.name = name;
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

    public void setMediaType(final String mediaType) {
        this.mediaType = mediaType;
    }

    public void setSize(final long size) {
        this.size = size;
    }
}
