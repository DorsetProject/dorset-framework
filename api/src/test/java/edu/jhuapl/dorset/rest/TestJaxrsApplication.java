package edu.jhuapl.dorset.rest;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import edu.jhuapl.dorset.Application;

public class TestJaxrsApplication extends ResourceConfig {
    public TestJaxrsApplication(final Application app) {
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(app).to(Application.class);
            }
        });

        packages("edu.jhuapl.dorset.rest");
    }

}
