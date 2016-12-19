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
package edu.jhuapl.dorset;

import java.util.UUID;

import edu.jhuapl.dorset.users.User;

/**
 * Dorset Request
 * <p>
 * Represents a question or command to a Dorset application.
 */
public class Request {
    /** Maximum length of the identifier string ({@value #MAX_ID_LENGTH}) */
    public static final int MAX_ID_LENGTH = 36;

    private String text;
    private final String id;
    private final User user;

    /**
     * Create a request
     * <p>
     * Automatically sets the identifier of the request
     *
     * @param text  the text of the request
     */
    public Request(String text) {
        this(text, null, "");
    }
    
    /**
     * Create a request
     * <p>
     * Automatically sets the identifier of the request
     *
     * @param text  the text of the request
     * @param user  the user of the request
     * */
    public Request(String text, User user) {
        this(text, user, "");
    }

    /**
     * Create a request
     *
     * @param text  the text of the request
     * @param id  the identifier of the request (cannot be longer then MAX_ID_LENGTH)
     *            The identifier must be unique.
     */
    public Request(String text, String id) {
        this(text, null, id);
    }

    /**
     * Create a request
     *
     * @param text  the text of the request
     * @param user  the user of the request
     * @param id  the identifier of the request (cannot be longer then MAX_ID_LENGTH)
     *            The identifier must be unique.
     */
    public Request(String text, User user, String id) {
        if (id.length() > MAX_ID_LENGTH) {
            throw new IllegalArgumentException(
                            "Request id cannot be longer than " + String.valueOf(MAX_ID_LENGTH));
        }
        
        if (id.length() == 0) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }
        
        this.text = text;
        this.user = user;
    }
    
    /**
     * Get the text of the request
     *
     * @return the text of the request
     */
    public String getText() {
        return text;
    }

    /**
     * Set the text of the request
     *
     * @param text  the text of the request
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the identifier of the request
     *
     * @return identifier
     */
    public String getId() {
        return id;
    }
    
    /**
     * Get the User of the request
     *
     * @return user
     */
    public User getUser() {
        return user;
    }
 
}
