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

import org.junit.Test;

import static com.mongodb.util.MyAsserts.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class QueryBuilderTest {

    @Test
    public void shouldHaveCorrectQueryWithNoCriteria() {
        final QueryBuilder queryBuilder = new QueryBuilder();
        final IQuery query = queryBuilder.buildQuery();
        assertNotNull(query);
        assertEquals("{}", query);
    }

    @Test
    public void shouldHaveSuccessfullyBuildQueryWithSimpleCriteria() {
        final QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.appendCriteria("property1", "value1").appendCriteria("property2", "value2");
        final IQuery query = queryBuilder.buildQuery();
        assertNotNull(query);
        assertEquals("{property1:'value1',property2:'value2'}", query);
    }

    @Test
    public void shouldHaveSuccessfullyBuildQueryWithTwoIdenticalCriteria() {
        final QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.appendCriteria("property1", "value1").appendCriteria("property1", "value1");
        final IQuery query = queryBuilder.buildQuery();
        assertNotNull(query);
        assertEquals("{property1:'value1'}", query);
    }
}
