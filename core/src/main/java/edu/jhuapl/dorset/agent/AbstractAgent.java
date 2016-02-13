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
 *
 */
public abstract class AbstractAgent implements Agent {
    protected String name = this.getClass().getCanonicalName().toLowerCase();
    protected Description description = Description.getUninitializedDescription(name);

    /* (non-Javadoc)
     * @see edu.jhuapl.dorset.agent.Agent#getName()
     */
    @Override
    public String getName() {
        return name.toLowerCase();
    }

    /* (non-Javadoc)
     * @see edu.jhuapl.dorset.agent.Agent#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        name = name.toLowerCase();
        this.name = name;
        if (description != null) {
            description.setName(name);
        }
    }

    /* (non-Javadoc)
     * @see edu.jhuapl.dorset.agent.Agent#getDescription()
     */
    @Override
    public Description getDescription() {
        return description;
    }

    /* (non-Javadoc)
     * @see edu.jhuapl.dorset.agent.Agent#setDescription(edu.jhuapl.dorset.agent.Description)
     */
    @Override
    public void setDescription(Description description) {
        this.description = description;
    }

}
