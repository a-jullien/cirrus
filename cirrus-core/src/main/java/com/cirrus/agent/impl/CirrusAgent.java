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

package com.cirrus.agent.impl;

import com.cirrus.agent.ICirrusAgent;
import com.cirrus.agent.ICirrusAgentBundleDescription;
import com.cirrus.agent.ICirrusAgentIdentifier;
import com.cirrus.agent.IStorageServiceVendor;
import com.cirrus.osgi.extension.ICirrusStorageService;
import com.cirrus.server.exception.CirrusAgentInstallationException;
import com.cirrus.server.exception.StartCirrusAgentException;
import com.cirrus.server.exception.StopCirrusAgentException;
import com.cirrus.server.exception.UninstallCirrusAgentException;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;

import java.util.Dictionary;

public class CirrusAgent implements ICirrusAgent {

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private final ICirrusAgentIdentifier identifier;
    private final IStorageServiceVendor storageServiceVendor;
    private final ICirrusStorageService storageService;
    private final ICirrusAgentBundleDescription bundleDescription;
    private final Bundle bundle;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================

    public CirrusAgent(final Bundle bundle) throws CirrusAgentInstallationException {
        super();
        this.bundle = bundle;
        this.storageService = this.resolveStorageServiceFrom(bundle);
        this.storageServiceVendor = this.createStorageServiceVendor(bundle);
        this.bundleDescription = this.createBundleDescription(bundle);
        this.identifier = new UUIDBasedCirrusAgentIdentifier(this.bundleDescription.getName());
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================

    @Override
    public ICirrusAgentIdentifier getIdentifier() {
        return this.identifier;
    }

    @Override
    public ICirrusAgentBundleDescription getCirrusAgentBundleDescription() {
        return this.bundleDescription;
    }

    @Override
    public IStorageServiceVendor getStorageServiceVendor() {
        return this.storageServiceVendor;
    }

    @Override
    public ICirrusStorageService getStorageService() {
        return this.storageService;
    }

    @Override
    public void start() throws StartCirrusAgentException {
        try {
            this.bundle.start();
        } catch (final BundleException e) {
            throw new StartCirrusAgentException(e);
        }
    }

    @Override
    public void stop() throws StopCirrusAgentException {
        try {
            this.bundle.stop();
        } catch (final BundleException e) {
            throw new StopCirrusAgentException(e);
        }
    }

    @Override
    public void uninstall() throws UninstallCirrusAgentException {
        try {
            this.bundle.uninstall();
        } catch (final BundleException e) {
            throw new UninstallCirrusAgentException(e);
        }
    }

    //==================================================================================================================
    // Override
    //==================================================================================================================

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final CirrusAgent that = (CirrusAgent) o;

        return identifier.equals(that.identifier);

    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public String toString() {
        return this.bundleDescription.toString();
    }

    //==================================================================================================================
    // Attributes
    //==================================================================================================================
    private StorageServiceVendor createStorageServiceVendor(final Bundle bundle) {
        final Dictionary<String, String> dictionary = bundle.getHeaders();

        final String serviceName = dictionary.get(ICirrusStorageService.SERVICE_NAME_PROPERTY);
        final String serviceVersion = dictionary.get(ICirrusStorageService.SERVICE_VERSION_PROPERTY);
        final String serviceVendor = dictionary.get(ICirrusStorageService.SERVICE_VENDOR_PROPERTY);

        return new StorageServiceVendor(serviceName, serviceVersion, serviceVendor);
    }

    private ICirrusStorageService resolveStorageServiceFrom(final Bundle bundle) throws CirrusAgentInstallationException {
        final Dictionary<String, String> dictionary = bundle.getHeaders();
        final String serviceClazz = dictionary.get(ICirrusStorageService.SERVICE_CLASS_PROPERTY);

        final Class<?> loadClass;
        try {
            loadClass = bundle.loadClass(serviceClazz);
            return (ICirrusStorageService) loadClass.newInstance();

        } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new CirrusAgentInstallationException(e);
        }
    }

    private ICirrusAgentBundleDescription createBundleDescription(final Bundle bundle) {
        final Dictionary<String, String> dict = bundle.getHeaders();
        final String bundleName = dict.get(Constants.BUNDLE_NAME);
        final String bundleDescription = dict.get(Constants.BUNDLE_DESCRIPTION);
        final String bundleVersion = dict.get(Constants.BUNDLE_VERSION);
        final String bundleVendor = dict.get(Constants.BUNDLE_VENDOR);

        return new CirrusAgentBundleDescription(bundleName, bundleDescription, bundleVersion, bundleVendor);
    }
}
