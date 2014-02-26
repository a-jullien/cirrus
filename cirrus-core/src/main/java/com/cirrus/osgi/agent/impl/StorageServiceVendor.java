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

package com.cirrus.osgi.agent.impl;

import com.cirrus.osgi.agent.IStorageServiceVendor;

public class StorageServiceVendor implements IStorageServiceVendor {

    //==================================================================================================================
    // Constants
    //==================================================================================================================
    private static final long serialVersionUID = -175173491510090334L;

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final String name;
    private final String version;
    private final String vendor;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public StorageServiceVendor(final String name, final String version, final String vendor) {
        this.name = name;
        this.version = version;
        this.vendor = vendor;
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public String getVendor() {
        return this.vendor;
    }

    //==================================================================================================================
    // Override
    //==================================================================================================================
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final StorageServiceVendor that = (StorageServiceVendor) o;

        return !(name != null ? !name.equals(that.name) : that.name != null) &&
                !(vendor != null ? !vendor.equals(that.vendor) : that.vendor != null) &&
                !(version != null ? !version.equals(that.version) : that.version != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (vendor != null ? vendor.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "name='" + name + ", version='" + version + ", vendor='" + vendor;
    }
}
