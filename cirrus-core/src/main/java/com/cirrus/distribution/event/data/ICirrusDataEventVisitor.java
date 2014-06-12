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

package com.cirrus.distribution.event.data;

import com.cirrus.distribution.event.data.impl.ICirrusDataCreatedEvent;
import com.cirrus.distribution.event.data.impl.ICirrusDataRemovedEvent;
import com.cirrus.utils.Try;

public interface ICirrusDataEventVisitor<R> {

    /**
     * Visits an event when a new data has been created
     */
    Try<R> visit(ICirrusDataCreatedEvent createdEvent);

    /**
     * Visits an event when an existing data has been removed
     */
    Try<R> visit(ICirrusDataRemovedEvent removedEvent);
}
