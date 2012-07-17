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
package org.eclipse.servicesregistry.ui.test.prefpage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.config.persistency.IPreferencesController;
import org.eclipse.servicesregistry.ui.internal.prefpage.SrConfigContentProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class SrConfigContentProviderTest
{
	@Mock
	private IPreferencesController controller;
	@Mock
	private IServicesRegistrySystem config1;
	@Mock
	private IServicesRegistrySystem config2;
	
	private SrConfigContentProvider provider;
	private Set<IServicesRegistrySystem> configsSet;
	
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		Mockito.stub(config1.displayName()).toReturn("Connection1");
		Mockito.stub(config2.displayName()).toReturn("Connection2");
		
		final IServicesRegistrySystem[] configsArr = new IServicesRegistrySystem[]{config1, config2};
		configsSet = new HashSet<IServicesRegistrySystem>(Arrays.asList(configsArr));
		Mockito.stub(controller.getConfigurations()).toReturn(configsSet);
		
		provider = new SrConfigContentProvider(controller);
	}

	@Test
	public void testGetElements()
	{
		final Object[] result = provider.getElements(null);
		assertEquals("Two elements expected", 2, result.length);
		final List<IServicesRegistrySystem> configs = new ArrayList<IServicesRegistrySystem>();
		for(Object o : result)
		{
			configs.add((IServicesRegistrySystem)o);
		}
		
		assertTrue("Config 1 not returned", configs.contains(config1));
		assertTrue("Config 2 not returned", configs.contains(config2));
	}
	
	@Test
	public void testGetElementsResultIsSorted()
	{
		assertEquals("Connection1", ((IServicesRegistrySystem)provider.getElements(null)[0]).displayName());
		assertEquals("Connection2", ((IServicesRegistrySystem)provider.getElements(null)[1]).displayName());

		configsSet = new HashSet<IServicesRegistrySystem>(Arrays.asList(new IServicesRegistrySystem[]{config2, config1}));
		assertEquals("Connection1", ((IServicesRegistrySystem)provider.getElements(null)[0]).displayName());
		assertEquals("Connection2", ((IServicesRegistrySystem)provider.getElements(null)[1]).displayName());
	}
}