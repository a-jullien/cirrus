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

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    protected AbstractCirrusData(final String localPath) {
        this.localPath = localPath;
        this.creationTime = System.currentTimeMillis();
        this.name = DataUtils.extractFileNameFromPath(localPath);
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
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

    //==================================================================================================================
    // Override
    //==================================================================================================================

    @Override
    public String toString() {
        return this.getPath();
    }
}
