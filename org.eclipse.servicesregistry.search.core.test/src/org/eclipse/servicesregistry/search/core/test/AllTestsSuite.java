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
package org.eclipse.servicesregistry.search.core.test;

import org.eclipse.servicesregistry.search.core.test.destinations.ServicesRegistryDestinationTest;
import org.eclipse.servicesregistry.search.core.test.destinations.SrDestinationsProviderTest;
import org.eclipse.servicesregistry.search.core.test.resources.EditResourcesManagerTest;
import org.eclipse.servicesregistry.search.core.test.resources.WorkspaceFilesEditManagerTest;
import org.eclipse.servicesregistry.search.core.test.search.FindServiceDefinitionsQueryTest;
import org.eclipse.servicesregistry.search.core.test.search.ServiceDefinitionResolverTest;
import org.eclipse.servicesregistry.search.core.test.search.result.resolve.ServiceDefinitionResolutionTest;
import org.eclipse.servicesregistry.search.core.test.slavectrl.SRSlaveControllerTest;
import org.eclipse.servicesregistry.search.core.test.slavectrl.actions.DownloadWsdlContributedActionTest;
import org.eclipse.servicesregistry.search.core.test.slavectrl.actions.GetEndpointWsdlContributedActionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	ServicesRegistryDestinationTest.class,
	FindServiceDefinitionsQueryTest.class,
	ServiceDefinitionResolverTest.class,
	ServiceDefinitionResolutionTest.class,
	WorkspaceFilesEditManagerTest.class,
	EditResourcesManagerTest.class,
	SRSlaveControllerTest.class,
	GetEndpointWsdlContributedActionTest.class,
	DownloadWsdlContributedActionTest.class,
	SrDestinationsProviderTest.class
})
@RunWith(Suite.class)
public class AllTestsSuite
{

}
