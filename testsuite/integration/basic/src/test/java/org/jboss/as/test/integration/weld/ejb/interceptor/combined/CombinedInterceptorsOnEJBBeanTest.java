/*
 * JBoss, Home of Professional Open Source
 * Copyright 2018, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.test.integration.weld.ejb.interceptor.combined;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@RunWith(Arquillian.class)
public class CombinedInterceptorsOnEJBBeanTest {

    @Deployment
    public static Archive<?> getDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackage(CombinedInterceptorsOnEJBBeanTest.class.getPackage())
            .addAsManifestResource(createBeansXml(), "beans.xml");
    }

    @Inject
    SimpleService service;

    @Test
    public void testBothInterceptorsAreInvoked() {
        service.ok();
        service.notOk();
        Assert.assertEquals(1, CDIInterceptor.INVOKED);
        Assert.assertEquals(1, EJBInterceptor.INVOKED);
    }

    private static StringAsset createBeansXml() {
        return new StringAsset("<beans bean-discovery-mode=\"all\" version=\"1.1\"><interceptors>"
            + "<class>org.jboss.as.test.integration.weld.ejb.interceptor.combined.CDIInterceptor</class>"
            + "<class>org.jboss.as.test.integration.weld.ejb.interceptor.combined.EJBInterceptor</class>"
            + "</interceptors></beans>");
    }
}
