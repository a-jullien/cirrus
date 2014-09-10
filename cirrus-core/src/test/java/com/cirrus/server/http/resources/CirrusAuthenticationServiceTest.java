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

package com.cirrus.server.http.resources;

import com.cirrus.model.authentication.impl.LoginPasswordCredentials;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class CirrusAuthenticationServiceTest extends AbstractJerseyTest {

    @Test
    public void shouldAuthenticationErrorWhenTryToAuthenticationWithNonExistingUser() {
        final WebTarget webTargetForPath = super.getWebTargetFor("cirrus", "authentication");

        final LoginPasswordCredentials credentials = new LoginPasswordCredentials("notExist@flunny.org", "myPassword");
        final Response response = webTargetForPath.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(credentials, MediaType.APPLICATION_JSON));

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(401);
    }
}
