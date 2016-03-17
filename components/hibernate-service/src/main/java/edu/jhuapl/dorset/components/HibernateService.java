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

import java.util.Map;

import org.hibernate.MappingException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

import edu.jhuapl.dorset.ShutdownListener;

/**
 * Service to configure the Hibernate session factory
 * <p>
 * The standard hibernate configuration is used and should be placed in the
 * application's application.conf file:
 * <pre>
 * hibernate.connection.driver_class = com.mysql.jdbc.Driver
 * hibernate.connection.url = jdbc:mysql://localhost:3306/db_name
 * hibernate.connection.username = db_user
 * hibernate.connection.password = db_password
 * hibernate.dialect = org.hibernate.dialect.MySQLDialect
 * </pre>
 * <p>
 * Mapping files will be loaded if the configuration includes the key:
 * <p>
 * hibernate.mapping.[name of component] = [filename]
 * <p>
 * It is recommended to store the default mapping for components in a
 * reference.conf file.
 * <p>
 * see SqlReporter for an example of a mapping configuration.
 */
public class HibernateService implements ShutdownListener {
    private static final String HIBERNATE_KEY = "hibernate";
    private static final String MAPPING_KEY = "hibernate.mapping";
    private final Logger logger = LoggerFactory.getLogger(HibernateService.class);

    private SessionFactory sessionFactory;

    /**
     * Create the hibernate service which initializes the session factory
     *
     * @param conf  application configuration
     * @throws UnsupportedOperationException if a mapping file is invalid
     */
    public HibernateService(Config conf) {
        Configuration hibernateConf = new Configuration();
        Config hc = conf.getConfig(HIBERNATE_KEY).atPath(HIBERNATE_KEY);
        for (Map.Entry<String, ConfigValue> entry : hc.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(MAPPING_KEY)) {
                logger.info("Loading hibernate map from " + conf.getString(key));
                try {
                    hibernateConf.addResource(conf.getString(key));
                } catch (MappingException e) {
                    String msg = "Something wrong with mapping: " + conf.getString(key);
                    throw new UnsupportedOperationException(msg, e);
                }
            } else {
                logger.info("Setting hibernate property: " + key + "=" + conf.getString(key));
                hibernateConf.setProperty(key, conf.getString(key));
            }
        }
        sessionFactory = hibernateConf.buildSessionFactory();
    }

    /**
     * Get the Hibernate session factory
     *
     * @return session factory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Shutdown the session factory
     * <p>
     * Must be called before exiting the application
     */
    @Override
    public void shutdown() {
        logger.info("Shutting down the hibernate service");
        sessionFactory.close();
    }
}
