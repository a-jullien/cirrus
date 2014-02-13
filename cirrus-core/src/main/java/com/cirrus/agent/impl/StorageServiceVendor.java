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

import com.cirrus.agent.IStorageServiceVendor;

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
}
