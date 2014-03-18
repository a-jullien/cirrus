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

package com.cirrus.server.osgi.service.local;

import com.cirrus.data.ICirrusData;
import com.cirrus.data.impl.CirrusFileData;
import com.cirrus.data.impl.CirrusFolderData;
import com.cirrus.agent.authentication.impl.AnonymousTrustedToken;
import com.cirrus.server.osgi.extension.AbstractStorageService;
import com.cirrus.server.osgi.extension.ServiceRequestFailedException;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LocalStorageService extends AbstractStorageService<AnonymousTrustedToken> {

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private File rootDirectory;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    public LocalStorageService() {
        super();
    }

    @Override
    public void initializeCirrusRootDirectory() {
        this.rootDirectory = new File("/" + ROOT_DIRECTORY_NAME);
        if (!this.rootDirectory.exists()) {
            throw new IllegalAccessError("the directory <" + this.rootDirectory.getAbsolutePath() + "> must be created");
        }
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
        final String newPath = this.rootDirectory.getAbsolutePath() + File.separatorChar + path;
        final File file = new File(newPath);
        if (!file.exists()) {
            throw new ServiceRequestFailedException("The directory <" + newPath + "> doesn't exist");
        }

        if (!file.isDirectory()) {
            throw new ServiceRequestFailedException("The path <" + newPath + "> is not a directory");
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
        final String newPath = this.rootDirectory.getAbsolutePath() + File.separatorChar + path;
        final File file = new File(newPath);
        if (file.exists()) {
            throw new ServiceRequestFailedException("The directory <" + newPath + "> already exists");
        } else {
            final boolean created = file.mkdirs();
            if (!created) {
                throw new ServiceRequestFailedException("The directory <" + newPath + "> cannot be created");
            } else {
                return new CirrusFolderData(file.getPath());
            }
        }
    }

    @Override
    public ICirrusData delete(final String path) throws ServiceRequestFailedException {
        final String newPath = this.rootDirectory.getAbsolutePath() + File.separatorChar + path;
        final File file = new File(newPath);
        if (!file.exists()) {
            throw new ServiceRequestFailedException("The entry <" + newPath + "> doesn't exist");
        } else {
            final ICirrusData cirrusData = this.createCirrusDataFromFile(file);
            final boolean fileDeleted = file.delete();
            if (!fileDeleted) {
                throw new ServiceRequestFailedException("The file <" + newPath + "> cannot be deleted");
            } else {
                return cirrusData;
            }
        }
    }

    @Override
    public CirrusFileData transferFile(final String filePath, final long fileSize, final InputStream inputStream) throws ServiceRequestFailedException {
        final String newPath = this.rootDirectory.getAbsolutePath() + File.separatorChar + filePath;
        final File file = new File(newPath);
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
