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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.jhuapl.dorset.users.User;
import edu.jhuapl.dorset.users.UserException;
import edu.jhuapl.dorset.users.UserService;


/**
 * FileUserService userservice
 *
 * FileUserService is a file-based UserService that leverages property files to maintain a set of
 * Users. All User files are stored in a single directory and each User has a single User file. The
 * naming convention for the set of files includes a base name that is uniform across all users then
 * a dash followed by the corresponding Username. Example:
 * "path/to/user/files/exampleuser-jdoe.properties."
 */
public class FileUserService implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(FileUserService.class);

    private Map<String, User> users;
    protected String userDirPath;
    protected String fileBaseName;

    /**
     * 
     * FileUserService
     * 
     * @param userDirectoryPath Path to directory that contains User files
     * @param fileBaseName Base string of User file names
     * 
     */
    public FileUserService(String userDirectoryPath, String fileBaseName) throws UserException {
        users = new HashMap<String, User>();
        this.userDirPath = userDirectoryPath;
        this.fileBaseName = fileBaseName;

        // Check if directory exists
        if (!(Paths.get(this.userDirPath).toFile().exists())) {
            logger.error("Invalid directory. Could not instantiate FileUserService.");
            throw new UserException("Invalid directory. Could not instantiate FileUserService.");
        }
    }

    @Override
    public String create(User user) throws UserException {
        Properties prop = new Properties();
        OutputStream output = null;

        File userFile = pathToUserFile(user.getUserName()).toFile();
        // Check if Username already exists
        if (userFile.exists()) {
            throw new UserException("Username (" + user.getUserName() + ") already exists.");
        }

        try {
            output = new FileOutputStream(userFile);

            Set<String> userKeys = user.getUserInformationKeys();
            for (String key : userKeys) {
                prop.setProperty(key, user.getUserInformation(key));
            }
            prop.store(output, null);

        } catch (IOException e) {
            throw new UserException("IOException when creating the User file for "
                            + user.getUserName() + ". " + e.getMessage());
        }

        this.users.put(user.getUserName(), user);
        return user.getUserName();
    }

    @Override
    public String retrieve(Properties properties) throws UserException {
        User user = new User();
        String userName = properties.getProperty("userName");

        FileInputStream in;
        Properties props = new Properties();
        File userFile = pathToUserFile(userName).toFile();
        try {
            in = new FileInputStream(userFile);
            props.load(in);
            in.close();
        } catch (IOException e) {
            throw new UserException("User file (" + this.fileBaseName + "-" + userName
                            + ".properties) does not exist.");
        }

        Set<Object> keySet = props.keySet();
        for (Object key : keySet) {
            user.setUserInformation((String) key, props.getProperty((String) key));
        }

        this.users.put(userName, user);
        return userName;
    }

    @Override
    public void update(String userName, User user) throws UserException {
        this.delete(userName);
        this.create(user);
    }

    @Override
    public void delete(String userName) {
        File userFile = pathToUserFile(userName).toFile();
        if (userFile.delete()) {
            users.remove(userName);
            logger.info(userFile.getName() + " successfully deleted.");
        } else {
            logger.error("Delete operation failed.");
        }
    }

    @Override
    public User getUser(String userName) {
        return users.get(userName);
    }

    public Path pathToUserFile(String userName) {
        return Paths.get(this.userDirPath, this.fileBaseName + "-" + userName + ".properties");

    }

}
