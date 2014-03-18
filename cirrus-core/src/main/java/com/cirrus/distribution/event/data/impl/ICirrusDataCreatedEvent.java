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

package com.cirrus.distribution.event.data.impl;

import com.cirrus.data.ICirrusData;
import com.cirrus.distribution.event.data.ICirrusDataEvent;

public interface ICirrusDataCreatedEvent extends ICirrusDataEvent {

    /**
     * Returns the created cirrus data. Never <code>null</code>
     */
    ICirrusData getCirrusData();

    /**
     * Returns the virtual path where the cirrus data is 'virtually' stored
     */
    String getVirtualPath();
}