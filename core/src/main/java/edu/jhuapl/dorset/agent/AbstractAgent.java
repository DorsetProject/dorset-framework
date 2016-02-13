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
package edu.jhuapl.dorset.agent;

/**
 * Abstract Agent class
 * 
 * Agent implementations should inherit from this class. It provides convenient
 * methods for managing the agent name and description.
 */
public abstract class AbstractAgent implements Agent {
    protected String name = this.getClass().getCanonicalName().toLowerCase();
    protected Description description = Description.getUninitializedDescription(name);

    @Override
    public String getName() {
        return name.toLowerCase();
    }

    @Override
    public void setName(String name) {
        name = name.toLowerCase();
        this.name = name;
        if (description != null) {
            description.setName(name);
        }
    }

    @Override
    public Description getDescription() {
        return description;
    }

    @Override
    public void setDescription(Description description) {
        this.description = description;
    }

}
