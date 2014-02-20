/**
 * The MIT License (MIT)
 * Copyright (c) 2014 Antoine Jullien
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.cirrus.agent.impl;

import com.cirrus.agent.ICirrusAgent;
import com.cirrus.agent.ICirrusAgentIdentifier;
import com.cirrus.agent.IStorageServiceVendor;
import com.cirrus.osgi.extension.ICirrusStorageService;

public class CirrusAgent implements ICirrusAgent {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final ICirrusAgentIdentifier identifier;
    private final IStorageServiceVendor storageServiceVendor;
    private final ICirrusStorageService storageService;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    public CirrusAgent(final ICirrusStorageService storageService, final IStorageServiceVendor storageServiceVendor) {
        super();
        this.storageService = storageService;
        this.storageServiceVendor = storageServiceVendor;
        this.identifier = new UUIDBasedCirrusAgentIdentifier();
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public ICirrusAgentIdentifier getIdentifier() {
        return this.identifier;
    }

    @Override
    public IStorageServiceVendor getStorageServiceVendor() {
        return this.storageServiceVendor;
    }

    @Override
    public ICirrusStorageService getStorageService() {
        return this.storageService;
    }
}
