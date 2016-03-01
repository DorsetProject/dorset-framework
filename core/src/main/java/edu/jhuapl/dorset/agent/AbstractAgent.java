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
 * <p>
 * Agent implementations should inherit from this class. It provides convenient
 * methods for managing the agent name and description.
 */
public abstract class AbstractAgent implements Agent {
    protected Description description = Description.getUninitializedDescription(this.getClass());

    @Override
    public String getName() {
        return description.getName();
    }

    @Override
    public void setName(String name) {
        description.setName(name);
    }

    @Override
    public Description getDescription() {
        return new Description(description);
    }

    @Override
    public void setDescription(Description description) {
        this.description = new Description(description);
    }

}
