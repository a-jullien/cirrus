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

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final List<ICriteria> criterias;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public QueryBuilder() {
        super();
        this.criterias = new ArrayList<>();
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    public QueryBuilder appendCriteria(final String property, final String value) {
        final ICriteria criteria = new Criteria(property, value);
        if (!this.criterias.contains(criteria)) {
            this.criterias.add(criteria);
        }

        return this;
    }

    public IQuery buildQuery() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('{');
        for (int i = 0; i < this.criterias.size(); i++) {
            final ICriteria criteria = this.criterias.get(i);
            stringBuilder.append(criteria.getName()).append(':').append("'").append(criteria.getValue()).append("'");

            if (i < this.criterias.size() -1) {
                stringBuilder.append(',');
            }

        }
        stringBuilder.append('}');
        return new Query(stringBuilder.toString());
    }
}
