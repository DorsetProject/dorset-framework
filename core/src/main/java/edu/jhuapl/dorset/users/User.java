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

package edu.jhuapl.dorset.users;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Dorset User
 *
 */
public class User {

    public static String ID = "Dorset-id";
    public static String USERNAME = "Dorset-userName";
    public static String FIRSTNAME = "Dorset-firstName";
    public static String LASTNAME = "Dorset-lastName";
    public static String LOCATION = "Dorset-location";
    public static String EMAIL = "Dorset-email";
    public static String DOB = "Dorset-dob";

    protected Map<String, String> userInformation = new HashMap<String, String>();

    public String getId() {
        return userInformation.get(ID);
    }

    public void setId(String id) {
        this.setUserInformation(ID, id);
    }

    public String getUserName() {
        return userInformation.get(USERNAME);
    }

    public void setUserName(String userName) {
        this.setUserInformation(USERNAME, userName);
    }

    public String getFirstName() {
        return userInformation.get(FIRSTNAME);
    }

    public void setFirstName(String firstName) {
        this.setUserInformation(FIRSTNAME, firstName);
    }

    public String getLastName() {
        return userInformation.get(LASTNAME);
    }

    public void setLastName(String lastName) {
        this.setUserInformation(LASTNAME, lastName);
    }

    public String getLocation() {
        return userInformation.get(LOCATION);
    }

    public void setLocation(String location) {
        this.setUserInformation(LOCATION, location);
    }

    public String getEmail() {
        return userInformation.get(EMAIL);
    }

    public void setEmail(String email) {
        this.setUserInformation(EMAIL, email);
    }

    public String getDob() {
        return userInformation.get(DOB);
    }

    public void setDob(String dob) {
        this.setUserInformation(DOB, dob);
    }

    public void setUserInformation(String key, String value) {
        userInformation.put(key, value);
    }

    public String getUserInformation(String key) {
        return userInformation.get(key);
    }

    public Set<String> getUserInformationKeys() {
        return userInformation.keySet();
    }
}
