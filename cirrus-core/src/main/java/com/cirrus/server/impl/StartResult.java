package com.cirrus.server.impl;

import com.cirrus.server.exception.StartCirrusServerException;

public class StartResult {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final boolean success;
    private final StartCirrusServerException reason;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    private StartResult(final boolean success, final StartCirrusServerException reason) {
        this.success = success;
        this.reason = reason;
    }

    //==================================================================================================================
    // Getters
    //==================================================================================================================
    public boolean isSuccess() {
        return success;
    }

    public StartCirrusServerException getReason() {
        return reason;
    }

    //==================================================================================================================
    // Builders
    //==================================================================================================================
    static StartResult success() {
        return new StartResult(true, null);
    }

    static StartResult failure(final Exception e) {
        return new StartResult(true, new StartCirrusServerException(e));
    }
}


