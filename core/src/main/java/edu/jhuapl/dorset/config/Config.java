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
package edu.jhuapl.dorset.config;

import java.util.Properties;

public class Config {
    private Properties props;

    public Config(Properties properties) {
        props = properties;
    }

    /**
     * Get the properties for a particular class
     * @param clazz The class
     * @return Properties object
     */
    public Properties getConfig(Class<?> clazz) {
        String className = clazz.getCanonicalName().toLowerCase();
        Properties filteredProps = new Properties();
        for (String key : props.stringPropertyNames()) {
            if (key.startsWith(className)) {
                String newKey = key.substring(className.length() + 1);
                filteredProps.setProperty(newKey, props.getProperty(key));
            }
        }
        return filteredProps;
    }
}
