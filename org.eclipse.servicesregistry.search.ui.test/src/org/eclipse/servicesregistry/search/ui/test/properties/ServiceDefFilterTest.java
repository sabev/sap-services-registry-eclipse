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

import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;
import org.eclipse.servicesregistry.search.ui.internal.properties.ServiceDefFilter;
import org.eclipse.servicesregistry.search.ui.test.internal.IResolvableServiceDefinition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ServiceDefFilterTest
{
	private ServiceDefFilter filter;

	@Before
	public void setUp()
	{
		filter = new ServiceDefFilter();
	}

	@Test
	public void testSelectResolvedServiceDefinition()
	{
		assertTrue("Service definitions should be accepted", filter.select(resolvedServiceDefinition()));
	}

	@Test
	public void testSelectObject()
	{
		assertFalse("Arbitrary objects should not be accepted by filter", filter.select(new Object()));
	}
	
	@Test
	public void testSelectUnresolvedDefinition()
	{
		assertFalse("Unresolved objects should not be accepted by filter", filter.select(unresolvedServiceDefinition()));
	}
	
	private IServiceDefinition resolvedServiceDefinition()
	{
		return mockServiceDefinition(true);
	}
	
	private IServiceDefinition unresolvedServiceDefinition()
	{
		return mockServiceDefinition(false);
	}
	
	private void stubDefinitionResolved(final IResolvableServiceDefinition definition, final boolean resolved)
	{
		Mockito.stub(definition.isResolved()).toReturn(resolved);
	}

	private IResolvableServiceDefinition mockServiceDefinition(final boolean resolved)
	{
		final IResolvableServiceDefinition def = Mockito.mock(IResolvableServiceDefinition.class);
		stubDefinitionResolved(def, resolved);
		return def;
	};
	
}
