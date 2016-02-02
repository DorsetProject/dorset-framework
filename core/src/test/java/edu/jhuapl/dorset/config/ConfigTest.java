/**
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

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;

public class ConfigTest {

    @Test
    public void testGetConfig() {
        Properties props = new Properties();
        props.setProperty("edu.jhuapl.dorset.config.dorsetconfigtest.username", "admin");
        props.setProperty("edu.jhuapl.dorset.config.dorsetconfigtest.port", "8080");
        props.setProperty("edu.jhuapl.dorset.debug", "false");
        Config config = new Config(props);

        Properties p = config.getConfig(this.getClass());

        assertEquals(2, p.size());
        assertEquals("admin", p.getProperty("username"));
    }

}
