/*******************************************************************************
 * Copyright (c) 2012 SAP AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.servicesregistry.core.test;

import org.eclipse.servicesregistry.core.test.classifications.ClassificationSystemNodeTest;
import org.eclipse.servicesregistry.core.test.classifications.ClassificationSystemsFinderTest;
import org.eclipse.servicesregistry.core.test.classifications.ClassificationValueNodeTest;
import org.eclipse.servicesregistry.core.test.classifications.ClassificationValuesFinderTest;
import org.eclipse.servicesregistry.core.test.classifications.ClassificationsTreeBuilderTest;
import org.eclipse.servicesregistry.core.test.classifications.HierarchicalValueNodeTest;
import org.eclipse.servicesregistry.core.test.classifications.TreeNodeListTest;
import org.eclipse.servicesregistry.core.test.config.ServicesRegistrySystemValidatorTest;
import org.eclipse.servicesregistry.core.test.prefs.SrConfigStorageTest;
import org.eclipse.servicesregistry.core.test.prefs.securestore.CredentialsTest;
import org.eclipse.servicesregistry.core.test.prefs.securestore.PasswordStoreTest;
import org.eclipse.servicesregistry.core.test.proxy.EndpointAddressConfiguratorTest;
import org.eclipse.servicesregistry.core.test.proxy.ServicesRegistryProxyFactoryTest;
import org.eclipse.servicesregistry.core.test.proxy.SrApiImplTest;
import org.eclipse.servicesregistry.core.test.proxy.UserCredentialsConfiguratorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	ServicesRegistryProxyFactoryTest.class,
	EndpointAddressConfiguratorTest.class,
	UserCredentialsConfiguratorTest.class,
	SrApiImplTest.class,
	ServicesRegistrySystemValidatorTest.class,
	CredentialsTest.class,
	PasswordStoreTest.class,
	SrConfigStorageTest.class,
	ClassificationSystemsFinderTest.class,
	ClassificationValuesFinderTest.class,
	ClassificationSystemNodeTest.class,
	ClassificationValueNodeTest.class,
	HierarchicalValueNodeTest.class,
	ClassificationsTreeBuilderTest.class,
	TreeNodeListTest.class
})
@RunWith(Suite.class)
public class AllTestsSuite {
}
