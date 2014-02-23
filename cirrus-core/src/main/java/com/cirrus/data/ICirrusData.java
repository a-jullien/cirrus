package com.cirrus.data;

import java.io.Serializable;

public interface ICirrusData extends Serializable {

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
