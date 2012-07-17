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
package org.eclipse.servicesregistry.search.ui.test;

import org.eclipse.servicesregistry.search.ui.test.classifications.ClassificationsTreeContentProviderTest;
import org.eclipse.servicesregistry.search.ui.test.properties.EndpointFilterTest;
import org.eclipse.servicesregistry.search.ui.test.properties.ServiceDefFilterTest;
import org.eclipse.servicesregistry.search.ui.test.result.ClickToResolveNodeTest;
import org.eclipse.servicesregistry.search.ui.test.result.ServicesContentProviderTest;
import org.eclipse.servicesregistry.search.ui.test.result.ServicesLabelProviderTest;
import org.eclipse.servicesregistry.search.ui.test.result.ServicesRegistryViewCustomizationTest;
import org.eclipse.servicesregistry.search.ui.test.result.WorkspaceResourceBrowserDestinationFilterTest;
import org.eclipse.servicesregistry.search.ui.test.result.actions.OpenWsdlInEditorConfigTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	ServicesContentProviderTest.class,
	ServicesLabelProviderTest.class,
	ServiceDefFilterTest.class,
	EndpointFilterTest.class,
	WorkspaceResourceBrowserDestinationFilterTest.class,
	ServicesRegistryViewCustomizationTest.class,
	OpenWsdlInEditorConfigTest.class,
	ClickToResolveNodeTest.class,
	ClassificationsTreeContentProviderTest.class
})
@RunWith(Suite.class)
public class AllTestsSuite
{

}
