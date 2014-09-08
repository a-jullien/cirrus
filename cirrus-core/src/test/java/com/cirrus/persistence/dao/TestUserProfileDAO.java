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

package com.cirrus.persistence.dao;

import com.cirrus.agent.authentication.AuthenticationMode;
import com.cirrus.agent.authentication.IStorageServiceAuthenticator;
import com.cirrus.agent.authentication.impl.AccessKeyAuthenticator;
import com.cirrus.agent.authentication.impl.AccessKeyPasswordAuthenticator;
import com.cirrus.agent.authentication.impl.AnonymousAuthenticator;
import com.cirrus.model.profile.IStorageServiceProfile;
import com.cirrus.model.profile.IUserProfile;
import com.cirrus.model.profile.impl.StorageServiceProfile;
import com.cirrus.model.profile.impl.UserProfile;
import com.cirrus.persistence.dao.profile.IUserProfileDAO;
import com.cirrus.persistence.service.MongoDBService;
import com.cirrus.server.configuration.CirrusProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUserProfileDAO {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================

    private IUserProfileDAO profileDAO;

    @Before
    public void setUp() throws Exception {
        final CirrusProperties cirrusProperties = new CirrusProperties();
        final String databaseURL = cirrusProperties.getProperty(CirrusProperties.MONGODB_URL);

        final MongoDBService mongoDBService = new MongoDBService(databaseURL);
        this.profileDAO = mongoDBService.getUserProfileDAO();
    }

    @After
    public void tearDown() throws Exception {
        this.profileDAO.dropCollection();
    }

    //==================================================================================================================
    // Tests
    //==================================================================================================================

    @Test
    public void shouldSaveUserProfileWithoutStorageServiceProfile() throws Exception {
        this.profileDAO.save(new UserProfile("test1@flunny.org"));

        final List<IUserProfile> userProfiles = this.profileDAO.listUserProfiles();
        assertThat(userProfiles).isNotNull();
        assertThat(userProfiles.size()).isEqualTo(1);

        final IUserProfile loadedUserProfile = userProfiles.get(0);
        assertThat(loadedUserProfile.getEmailAddress()).isEqualTo("test1@flunny.org");
        assertThat(loadedUserProfile.listStorageProfiles().size()).isEqualTo(0);
    }

    @Test
    public void testSaveUserProfileWithOneStorageServiceProfileUsingAnonymousAuthenticator() throws Exception {
        final IUserProfile userProfile = new UserProfile("test1@flunny.org");

        final StorageServiceProfile storageServiceProfile = new StorageServiceProfile("DropBox");
        storageServiceProfile.setStorageServiceAuthenticator(new AnonymousAuthenticator());
        userProfile.addStorageProfile(storageServiceProfile);
        this.profileDAO.save(userProfile);

        final IUserProfile loadedUserProfile = this.profileDAO.getUserProfileByEmailAddress("test1@flunny.org");
        assertThat(loadedUserProfile).isNotNull();
        final List<IStorageServiceProfile> loadedStorageProfiles = loadedUserProfile.listStorageProfiles();
        assertThat(loadedStorageProfiles.size()).isEqualTo(1);
        assertThat(loadedStorageProfiles.get(0).getServiceName()).isEqualTo("DropBox");
        final IStorageServiceAuthenticator authenticator = loadedStorageProfiles.get(0).getStorageServiceAuthenticator();
        assertThat(authenticator).isExactlyInstanceOf(AnonymousAuthenticator.class);
        assertThat(authenticator.getMode()).isEqualTo(AuthenticationMode.ANONYMOUS);
    }

    @Test
    public void testSaveUserProfileWithOneStorageServiceProfileUsingAccessKeyAuthenticator() throws Exception {
        final IUserProfile userProfile = new UserProfile("test1@flunny.org");

        final StorageServiceProfile storageServiceProfile = new StorageServiceProfile("DropBox");
        storageServiceProfile.setStorageServiceAuthenticator(new AccessKeyAuthenticator("myAccessKey"));
        userProfile.addStorageProfile(storageServiceProfile);
        this.profileDAO.save(userProfile);

        final IUserProfile loadedUserProfile = this.profileDAO.getUserProfileByEmailAddress("test1@flunny.org");
        assertThat(loadedUserProfile).isNotNull();
        final List<IStorageServiceProfile> loadedStorageProfiles = loadedUserProfile.listStorageProfiles();
        assertThat(loadedStorageProfiles.size()).isEqualTo(1);
        assertThat(loadedStorageProfiles.get(0).getServiceName()).isEqualTo("DropBox");
        final IStorageServiceAuthenticator authenticator = loadedStorageProfiles.get(0).getStorageServiceAuthenticator();
        assertThat(authenticator).isExactlyInstanceOf(AccessKeyAuthenticator.class);
        assertThat(authenticator.getMode()).isEqualTo(AuthenticationMode.ACCESS_KEY);
    }

    @Test
    public void testSaveUserProfileWithOneStorageServiceProfileUsingAccessKeyPasswordAuthenticator() throws Exception {
        final IUserProfile userProfile = new UserProfile("test1@flunny.org");

        final StorageServiceProfile storageServiceProfile = new StorageServiceProfile("DropBox");
        storageServiceProfile.setStorageServiceAuthenticator(new AccessKeyPasswordAuthenticator("myAccessKey", "myAccessPassword"));
        userProfile.addStorageProfile(storageServiceProfile);
        this.profileDAO.save(userProfile);

        final IUserProfile loadedUserProfile = this.profileDAO.getUserProfileByEmailAddress("test1@flunny.org");
        assertThat(loadedUserProfile).isNotNull();
        final List<IStorageServiceProfile> loadedStorageProfiles = loadedUserProfile.listStorageProfiles();
        assertThat(loadedStorageProfiles.size()).isEqualTo(1);
        assertThat(loadedStorageProfiles.get(0).getServiceName()).isEqualTo("DropBox");
        final IStorageServiceAuthenticator authenticator = loadedStorageProfiles.get(0).getStorageServiceAuthenticator();
        assertThat(authenticator).isExactlyInstanceOf(AccessKeyPasswordAuthenticator.class);
        assertThat(authenticator.getMode()).isEqualTo(AuthenticationMode.ACCESS_KEY_PASSWORD);
    }
}
