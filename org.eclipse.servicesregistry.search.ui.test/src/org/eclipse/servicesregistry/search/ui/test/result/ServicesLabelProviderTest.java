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
package org.eclipse.servicesregistry.search.ui.test.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;
import org.eclipse.servicesregistry.search.core.model.IServiceEndpoint;
import org.eclipse.servicesregistry.search.ui.internal.result.ClickToResolveNode;
import org.eclipse.servicesregistry.search.ui.internal.result.ServicesLabelProvider;
import org.eclipse.servicesregistry.search.ui.internal.text.SearchUIMessages;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ServicesLabelProviderTest
{
	private static final String DEFINITION_NAME = "MyDefinition";
	private static final String ENDPOINT_NAME = "MyEndpoint";
	@Mock
	private IServiceDefinition definition;
	@Mock
	private IServiceEndpoint endpoint;

	private ServicesLabelProvider provider;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		Mockito.stub(definition.getName()).toReturn(DEFINITION_NAME);
		Mockito.stub(endpoint.getEndpointName()).toReturn(ENDPOINT_NAME);
		provider = new ServicesLabelProvider();
	}

	@Test
	public void testGetTextForUnknownObject()
	{
		assertNull("Unexectedly returned text for unknown object", provider.getText(new Object()));
	}

	@Test
	public void testGetTextForDefinition()
	{
		assertEquals("Unexpected text for service definition", DEFINITION_NAME, provider.getText(definition));
	}

	@Test
	public void testGetTextForEndpoint()
	{
		assertEquals("Unexpected text for service endpoint", ENDPOINT_NAME, provider.getText(endpoint));
	}
	
	@Test
	public void testGetTextForClickToResolveNode()
	{
		assertEquals("Unexpected text for ClickToResolveNode", SearchUIMessages.ClickToResolveNodeText, provider.getText(new ClickToResolveNode(null, null)));
	}
}
