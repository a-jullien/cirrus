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

package com.cirrus.osgi.service.local;

import com.cirrus.data.ICirrusData;
import com.cirrus.data.impl.CirrusFileData;
import com.cirrus.data.impl.CirrusFolderData;
import com.cirrus.osgi.agent.authentication.impl.AnonymousTrustedToken;
import com.cirrus.osgi.extension.AbstractStorageService;
import com.cirrus.osgi.extension.ServiceRequestFailedException;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LocalStorageService extends AbstractStorageService<AnonymousTrustedToken> {

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public LocalStorageService() {
        super();
    }

    //==================================================================================================================
    // Public
    //==================================================================================================================
    @Override
    public void authenticate(final AnonymousTrustedToken trustedToken) {
        // do nothing here
    }

    @Override
    public String getAccountName() throws ServiceRequestFailedException {
        return System.getProperty("user.name");
    }

    @Override
    public long getTotalSpace() throws ServiceRequestFailedException {
        throw new ServiceRequestFailedException("Not Yet Implemented");
    }

    @Override
    public long getUsedSpace() throws ServiceRequestFailedException {
        throw new ServiceRequestFailedException("Not Yet Implemented");
    }

    @Override
    public List<ICirrusData> list(final String path) throws ServiceRequestFailedException {
        final File file = new File(path);
        if (!file.exists()) {
            throw new ServiceRequestFailedException("The directory <" + path + "> doesn't exist");
        }

        if (!file.isDirectory()) {
            throw new ServiceRequestFailedException("The path <" + path + "> is not a directory");
        }

        final List<ICirrusData> result = new ArrayList<>();
        final File[] children = file.listFiles();
        if (children != null) {
            for (final File child : children) {
                result.add(this.createCirrusDataFromFile(child));
            }
        }

        return result;
    }

    @Override
    public CirrusFolderData createDirectory(final String path) throws ServiceRequestFailedException {
        final File file = new File(path);
        final boolean created = file.mkdir();
        if (!created) {
            throw new ServiceRequestFailedException("The directory <" + path + "> cannot be created");
        } else {
            return new CirrusFolderData(file.getPath());
        }
    }

    @Override
    public ICirrusData delete(final String path) throws ServiceRequestFailedException {
        final File file = new File(path);
        if (!file.exists()) {
            throw new ServiceRequestFailedException("The entry <" + path + "> doesn't exist");
        } else {
            final ICirrusData cirrusData = this.createCirrusDataFromFile(file);
            final boolean fileDeleted = file.delete();
            if (!fileDeleted) {
                throw new ServiceRequestFailedException("The file <" + path + "> cannot be deleted");
            } else {
                return cirrusData;
            }
        }
    }

    @Override
    public CirrusFileData transferFile(final String filePath, final long fileSize, final InputStream inputStream) throws ServiceRequestFailedException {
        final File file = new File(filePath);
        try {
            final boolean newFile = file.createNewFile();
            if (!newFile) {
                throw new ServiceRequestFailedException("The file <" + file + "> cannot be created");
            } else {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(file);
                    IOUtils.copy(inputStream, outputStream);
                } finally {
                    this.closeStream(inputStream);
                    this.closeStream(outputStream);
                }

                return new CirrusFileData(file.getPath());
            }
        } catch (final IOException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private void closeStream(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final IOException e) {
                // ignore
            }
        }
    }

    private ICirrusData createCirrusDataFromFile(final File file) {
        if (file.isFile()) {
            return new CirrusFileData(file.getPath());
        } else {
            return new CirrusFolderData(file.getPath());
        }
    }
}
