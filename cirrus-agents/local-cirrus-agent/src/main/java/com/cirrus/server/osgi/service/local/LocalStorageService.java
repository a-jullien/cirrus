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

import com.cirrus.agent.authentication.impl.AnonymousTrustedToken;
import com.cirrus.data.ICirrusData;
import com.cirrus.data.impl.CirrusFileData;
import com.cirrus.data.impl.CirrusFolderData;
import com.cirrus.server.osgi.extension.AbstractStorageService;
import com.cirrus.server.osgi.extension.ServiceRequestFailedException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LocalStorageService extends AbstractStorageService<AnonymousTrustedToken> {

    //==================================================================================================================
    // Private
    //==================================================================================================================

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
        final Path newPath = Paths.get(this.getGlobalContext().getRootPath(), path);

        if (!Files.exists(newPath)) {
            throw new ServiceRequestFailedException("The directory <" + newPath + "> doesn't exist");
        }

        if (!Files.isDirectory(newPath)) {
            throw new ServiceRequestFailedException("The path <" + newPath + "> is not a directory");
        }


        final List<ICirrusData> result = new ArrayList<>();
        try (final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(newPath)) {
            for (final Path currentPath : directoryStream) {
                result.add(this.createCirrusDataFromFile(currentPath));
            }

        } catch (final IOException e) {
            // ignore
        }

        return result;
    }

    @Override
    public CirrusFolderData createDirectory(final String path) throws ServiceRequestFailedException {
        final Path newPath = Paths.get(this.getGlobalContext().getRootPath(), path);

        if (Files.exists(newPath)) {
            throw new ServiceRequestFailedException("The directory <" + newPath + "> already exists");
        } else {
            try {
                final Path directories = Files.createDirectories(newPath);
                return new CirrusFolderData(directories.toString());
            } catch (final IOException e) {
                throw new ServiceRequestFailedException(e);
            }
        }
    }

    @Override
    public ICirrusData delete(final String path) throws ServiceRequestFailedException {
        final Path newPath = Paths.get(this.getGlobalContext().getRootPath(), path);

        if (!Files.exists(newPath)) {
            throw new ServiceRequestFailedException("The entry <" + newPath + "> doesn't exist");
        } else {
            try {
                final ICirrusData cirrusData = this.createCirrusDataFromFile(newPath);
                Files.delete(newPath);
                return cirrusData;
            } catch (final IOException e) {
                throw new ServiceRequestFailedException(e);
            }
        }
    }

    @Override
    public CirrusFileData transferFile(final String filePath, final long fileSize, final InputStream inputStream) throws ServiceRequestFailedException {
        final Path newPath = Paths.get(this.getGlobalContext().getRootPath(), filePath);

        try {
            final Path createFilePath = Files.createFile(newPath);

            try (final OutputStream outputStream = Files.newOutputStream(createFilePath)) {
                IOUtils.copy(inputStream, outputStream);
            }

            return new CirrusFileData(createFilePath.toString(), Files.size(createFilePath));
        } catch (final IOException e) {
            throw new ServiceRequestFailedException(e);
        }
    }

    //==================================================================================================================
    // Private
    //==================================================================================================================

    private ICirrusData createCirrusDataFromFile(final Path path) throws IOException {
        if (Files.isDirectory(path)) {
            return new CirrusFolderData(path.toString());
        } else {
            return new CirrusFileData(path.toString(), Files.size(path));
        }
    }
}
