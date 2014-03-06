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

package com.cirrus.osgi.extension;

import com.cirrus.osgi.server.ICirrusDataListener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractStorageService implements ICirrusStorageService {

    //==================================================================================================================
    // Private
    //==================================================================================================================
    protected List<ICirrusDataListener> listeners;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    protected AbstractStorageService() {
        super();
        this.listeners = new ArrayList<>();
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public void registerListener(final ICirrusDataListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    @Override
    public void unregisterListener(final ICirrusDataListener listener) {
        if (this.listeners.contains(listener)) {
            this.listeners.remove(listener);
        }
    }
}
