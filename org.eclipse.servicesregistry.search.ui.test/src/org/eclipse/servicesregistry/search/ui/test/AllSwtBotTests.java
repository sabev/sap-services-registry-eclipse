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

import org.eclipse.servicesregistry.search.ui.test.properties.EndpointGeneralSectionTest;
import org.eclipse.servicesregistry.search.ui.test.properties.ServiceDefGeneralSectionTest;
import org.eclipse.servicesregistry.search.ui.test.classifications.ClassificationTreeContributionTest;
import org.eclipse.servicesregistry.search.ui.test.result.actions.GetEndpointWsdlActionTest;
import org.eclipse.servicesregistry.search.ui.test.result.actions.SaveWsdlInWorkspaceActionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	GetEndpointWsdlActionTest.class,
	SaveWsdlInWorkspaceActionTest.class,
	ServiceDefGeneralSectionTest.class,
	EndpointGeneralSectionTest.class,
	ClassificationTreeContributionTest.class
})
public class AllSwtBotTests
{

}
