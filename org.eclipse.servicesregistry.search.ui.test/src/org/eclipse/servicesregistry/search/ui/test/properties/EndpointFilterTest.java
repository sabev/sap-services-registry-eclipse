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
package org.eclipse.servicesregistry.search.ui.test.properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.servicesregistry.search.core.model.IServiceEndpoint;
import org.eclipse.servicesregistry.search.ui.internal.properties.EndpointFilter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class EndpointFilterTest
{
	private EndpointFilter filter;

	@Before
	public void setUp()
	{
		filter = new EndpointFilter();
	}

	@Test
	public void testSelectServiceDefinition()
	{
		assertTrue("Endpoints should be accepted", filter.select(Mockito.mock(IServiceEndpoint.class)));
	}

	@Test
	public void testSelectObject()
	{
		assertFalse("Arbitrary objects should not be accepted by filter", filter.select(new Object()));
	}

}
