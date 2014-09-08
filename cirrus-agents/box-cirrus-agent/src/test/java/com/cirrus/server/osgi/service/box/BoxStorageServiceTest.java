/*
 * *
 *  * Copyright (c) 2014 Antoine Jullien
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.cirrus.server.osgi.service.box;

import static org.assertj.core.api.Assertions.assertThat;

public class BoxStorageServiceTest {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    /*

    private BoxStorageService boxStorageService;

    @Before
    public void setUp() throws Exception {
        this.boxStorageService = new BoxStorageService();

        final String boxClientId = System.getenv("BOX_CLIENT_ID");
        final String boxClientSecret = System.getenv("BOX_CLIENT_SECRET");

        assertThat(boxClientId).isNotNull();
        assertThat(boxClientSecret).isNotNull();

        this.boxStorageService.authenticate(new AccessKeyPasswordTrustedToken(boxClientId, boxClientSecret));
    }

    @After
    public void tearDown() throws Exception {
        this.boxStorageService.shutdown();
    }

    @Test
    public void shouldHaveTotalSpace() throws Exception {

        final long totalSpace = this.boxStorageService.getTotalSpace();
        assertThat(totalSpace).isGreaterThan(0);
    }

    @Test
    public void shouldHaveUsedSpace() throws Exception {
        final long usedSpace = this.boxStorageService.getUsedSpace();
        assertThat(usedSpace).isGreaterThan(0);
    }

    */
}
