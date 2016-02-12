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
package edu.jhuapl.dorset.components;

import static org.junit.Assert.*;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.config.spi.StandardConverters;
import org.hibernate.metadata.ClassMetadata;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class HibernateServiceTest {
    private HibernateService hs;

    @BeforeClass
    public static void setUpBeforeClass() {
        // force slf4j logging because jboss logger won't pick it up when we 
        // use slf4j simple logger
        System.setProperty("org.jboss.logging.provider", "slf4j");
    }

    @After
    public void cleanup() {
        if (hs != null) {
            hs.shutdown();
        }
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        props.setProperty("hibernate.connection.url", "jdbc:h2:mem:hibernate_test");
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        props.setProperty("hibernate.mapping.test", "hibernate_service_test.hbm.xml");
        return props;
    }

    @Test
    public void testCreationOfSessionFactory() {
        Properties props = getProperties();
        Config conf = ConfigFactory.parseProperties(props);

        hs = new HibernateService(conf);
        SessionFactory sf = hs.getSessionFactory();
        assertNotNull(sf);
        assertFalse(sf.isClosed());

        // traverse through the session factory to get at configuration values
        SessionFactoryOptions sfo = sf.getSessionFactoryOptions();
        StandardServiceRegistry ssr = sfo.getServiceRegistry();
        ConfigurationService cs = ssr.getService(ConfigurationService.class);
        assertEquals(props.getProperty("hibernate.connection.driver_class"), cs.getSetting("hibernate.connection.driver_class", StandardConverters.STRING));
        assertEquals(props.getProperty("hibernate.connection.url"), cs.getSetting("hibernate.connection.url", StandardConverters.STRING));
        assertEquals(props.getProperty("hibernate.dialect"), cs.getSetting("hibernate.dialect", StandardConverters.STRING));
        assertEquals(props.getProperty("hibernate.hbm2ddl.auto"), cs.getSetting("hibernate.hbm2ddl.auto", StandardConverters.STRING));

        // check mapping
        ClassMetadata cm = sf.getClassMetadata(TestObject.class);
        String[] names = cm.getPropertyNames();
        assertEquals(1, names.length);
        assertEquals("name", names[0]);
        assertEquals("string", cm.getPropertyType("name").getName());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testInvalidMapping() {
        Properties props = getProperties();
        props.setProperty("hibernate.mapping.test2", "not_exist.hbm.xml");
        Config conf = ConfigFactory.parseProperties(props);
        hs = new HibernateService(conf);
    }

    @Test
    public void testShutdown() {
        Properties props = getProperties();
        Config conf = ConfigFactory.parseProperties(props);
        hs = new HibernateService(conf);
        SessionFactory sf = hs.getSessionFactory();

        hs.shutdown();
        
        assertTrue(sf.isClosed());
    }

}
