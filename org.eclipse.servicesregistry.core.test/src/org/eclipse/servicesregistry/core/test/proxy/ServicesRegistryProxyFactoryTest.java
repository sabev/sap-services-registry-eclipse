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
package org.eclipse.servicesregistry.core.test.proxy;

import org.eclipse.servicesregistry.core.internal.proxy.factory.IConfigurator;
import org.eclipse.servicesregistry.core.internal.proxy.factory.ServicesRegistryProxyFactory;
import org.eclipse.servicesregistry.proxy.ServicesRegistrySi;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ServicesRegistryProxyFactoryTest
{
	private ServicesRegistryProxyFactory factory;
	private IConfigurator<ServicesRegistrySi> configurator_1;
	private IConfigurator<ServicesRegistrySi> configurator_2;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp()
	{
		configurator_1 = Mockito.mock(IConfigurator.class);
		configurator_2 = Mockito.mock(IConfigurator.class);
		factory = new ServicesRegistryProxyFactory(configurator_1, configurator_2);
	}

	@Test
	public void testCreateProxy()
	{
		final ServicesRegistrySi port = factory.createProxy();
		Mockito.verify(configurator_1, Mockito.times(1)).configure(Mockito.same(port));
		Mockito.verify(configurator_2, Mockito.times(1)).configure(Mockito.same(port));
	}
}
