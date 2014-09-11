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

package com.cirrus.server.http.client;

import com.cirrus.model.authentication.Token;
import com.cirrus.model.authentication.impl.LoginPasswordCredentials;
import com.cirrus.server.osgi.extension.AuthenticationException;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientServiceFactoryTest {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    private ClientServiceFactory clientServiceFactory;

    @Before
    public void setUp() throws Exception {
        this.clientServiceFactory = new ClientServiceFactory();
    }

    //==================================================================================================================
    // Tests
    //==================================================================================================================

    @Test(expected = AuthenticationException.class)
    public void shouldErrorWhenGetClientServiceWithoutAuthentication() throws Exception {
        this.clientServiceFactory.createClientService(new Token());
    }

    @Test
    public void shouldSuccessfullyGetClientServiceAfterAuthentication() throws AuthenticationException {
        final Token token = this.clientServiceFactory.authenticate(new LoginPasswordCredentials("admin@admin.com", "admin"));
        final ClientService clientService = this.clientServiceFactory.createClientService(token);
        assertThat(clientService).isNotNull();
        assertThat(clientService.getServerName()).isNotNull();
        assertThat(clientService.getAgentManager()).isNotNull();
    }

    @Test(expected = AuthenticationException.class)
    public void shouldErrorWhenGetClientServiceAfterInvalidateToken() throws AuthenticationException {
        final Token token = this.clientServiceFactory.authenticate(new LoginPasswordCredentials("admin@admin.com", "admin"));
        this.clientServiceFactory.invalidateToken(token);
        this.clientServiceFactory.createClientService(token);
    }
}
