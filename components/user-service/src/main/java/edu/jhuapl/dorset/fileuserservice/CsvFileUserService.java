/*
 * Copyright 2016 The Johns Hopkins University Applied Physics Laboratory LLC
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.jhuapl.dorset.fileuserservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import edu.jhuapl.dorset.users.User;
import edu.jhuapl.dorset.users.UserService;

/**
 * 
 * CsvFileUserService
 *
 */
public class CsvFileUserService implements UserService {
    private static final Logger logger = LoggerFactory
            .getLogger(CsvFileUserService.class);
    protected User user;

    protected String userFileStore;
    private static final CellProcessor[] processors = new CellProcessor[] {
            new NotNull(), new NotNull(), new NotNull(), new Optional(),
            new Optional(), new Optional() };

    public CsvFileUserService(String fileName) {
        this.userFileStore = fileName;
    }

    @Override
    public User load(String userName, String uniqueIdentifier) {
        this.user = null;

        ICsvBeanReader csvBeanReader = null;
        InputStream url = UserService.class.getClassLoader()
                .getResourceAsStream(this.userFileStore);

        try {
            csvBeanReader = new CsvBeanReader(new BufferedReader(
                    new InputStreamReader(url)),
                    CsvPreference.STANDARD_PREFERENCE);
            final String[] header = csvBeanReader.getHeader(true);
            User userIterating;
            while ((userIterating = csvBeanReader.read(User.class, header,
                    processors)) != null) {
                if (userIterating.getUserName().equalsIgnoreCase(userName)) {
                    user = userIterating;
                }
            }
            csvBeanReader.close();

        } catch (NullPointerException | IOException e) {
            logger.error("Failed to load " + this.userFileStore + ".", e);
        }

        return this.user;
    }

    @Override
    public User getCurrentUser() {
        return this.user;
    }

}
