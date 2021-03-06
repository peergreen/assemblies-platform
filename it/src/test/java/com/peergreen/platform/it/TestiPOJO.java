/**
 * Copyright 2013 Peergreen S.A.S.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.peergreen.platform.it;


import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.options;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.Factory;
import org.apache.felix.ipojo.architecture.Architecture;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * Test that factories and instances of iPOJO are all valid
 * @author Florent Benoit
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestiPOJO {

    @Inject
    private BundleContext bundleContext;

    @Configuration
    public Option[] config() {
        return options(junitBundles());
    }

    @Before
    public void init() throws URISyntaxException {
    }


    @Test
    public void checkInject() {
        Assert.assertNotNull(bundleContext);
    }

    @Test
    public void testFactories() throws InvalidSyntaxException {
        List<Factory> factories = new ArrayList<Factory>();
        // get all factories
        ServiceReference<Factory>[] serviceReferenceFactories = (ServiceReference<Factory>[]) bundleContext.getAllServiceReferences(Factory.class.getName(), null);
        for (ServiceReference<Factory> serviceReferenceFactory : serviceReferenceFactories) {
            Factory factory = bundleContext.getService(serviceReferenceFactory);
            factories.add(factory);
        }

        // check we have factories
        Assert.assertTrue(factories.size() > 5);

        String errorMessage = "";

        // Now check state
        for (Factory factory : factories) {
            if (Factory.VALID != factory.getState()) {
                errorMessage = errorMessage.concat(String.format("Factory %s is invalid", factory.getName())).concat("\n");
            }
        }

        Assert.assertEquals(errorMessage, 0, errorMessage.length());
    }


    @Test
    public void testInstances() throws InvalidSyntaxException {
        List<Architecture> instances = new ArrayList<Architecture>();
        // get all instances
        ServiceReference<Architecture>[] serviceReferenceFactories = (ServiceReference<Architecture>[]) bundleContext.getAllServiceReferences(Architecture.class.getName(), null);
        for (ServiceReference<Architecture> serviceReferenceFactory : serviceReferenceFactories) {
            Architecture instance = bundleContext.getService(serviceReferenceFactory);
            instances.add(instance);
        }

        // check we have instances
        Assert.assertTrue(instances.size() > 5);

        String errorMessage = "";

        // Now check state
        for (Architecture instance : instances) {
            if (ComponentInstance.VALID != instance.getInstanceDescription().getState()) {
                errorMessage = errorMessage.concat(String.format("Factory %s is invalid", instance.getInstanceDescription().getName())).concat("\n");
            }
        }

        Assert.assertEquals(errorMessage, 0, errorMessage.length());
    }


}
