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

package com.cirrus.persistence;

public class Criteria implements ICriteria {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final String name;
    private final String value;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public Criteria(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Criteria criteria = (Criteria) o;

        if (name != null ? !name.equals(criteria.name) : criteria.name != null) return false;
        if (value != null ? !value.equals(criteria.value) : criteria.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
