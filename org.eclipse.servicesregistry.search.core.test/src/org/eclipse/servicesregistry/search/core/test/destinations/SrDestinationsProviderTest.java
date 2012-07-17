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
package org.eclipse.servicesregistry.search.core.test.destinations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.platform.discovery.runtime.api.IDestinationChangeHandler;
import org.eclipse.platform.discovery.runtime.api.ISearchDestination;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.config.persistency.ConfigLoadException;
import org.eclipse.servicesregistry.core.config.persistency.ConfigStoreException;
import org.eclipse.servicesregistry.core.config.persistency.IConfigStorage;
import org.eclipse.servicesregistry.core.internal.logging.ILogger;
import org.eclipse.servicesregistry.search.core.destinations.IServicesRegistryDestination;
import org.eclipse.servicesregistry.search.core.internal.destinations.SrDestinationsProvider;
import org.eclipse.servicesregistry.testutils.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class SrDestinationsProviderTest
{
	@Mock
	private IConfigStorage configStorage;
	@Mock
	private IServicesRegistrySystem serverConfig;
	
	private SrDestinationsProvider provider;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		
		provider = new SrDestinationsProvider();
		ReflectionUtils.setFieldValue(provider, "configStorage", configStorage);
		setupTestSocoConfig();
	}

	private void setupTestSocoConfig()
	{
		Mockito.stub(serverConfig.host()).toReturn("host");
		Mockito.stub(serverConfig.port()).toReturn(1111);
		Mockito.stub(serverConfig.displayName()).toReturn("MyConnection");
		Mockito.stub(serverConfig.areCredentialsStored()).toReturn(false);
		Mockito.stub(serverConfig.useHttps()).toReturn(false);
	}

	@Test
	public void testGetDestinations() throws ConfigLoadException
	{
		Mockito.stub(configStorage.readConfigurations()).toReturn(new HashSet<IServicesRegistrySystem>(Arrays.asList(new IServicesRegistrySystem[]{serverConfig})));
		final Set<ISearchDestination> result = provider.getSearchDestinations();
		assertEquals("One destination expected", 1, result.size());
		assertEquals("Unexpected host", "host", ((IServicesRegistryDestination) result.iterator().next()).servicesRegistrySystem().host());
	}

	@Test
	public void testGetDestinationsWithExc() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ConfigLoadException
	{
		final ConfigLoadException exc = new ConfigLoadException();
		final ILogger loggerMock = Mockito.mock(ILogger.class);
		ReflectionUtils.setFieldValue(provider, "logger", loggerMock);
		Mockito.stub(configStorage.readConfigurations()).toThrow(exc);
		assertEquals("No destinations expected", 0, provider.getSearchDestinations().size());
		Mockito.verify(loggerMock, Mockito.times(1)).logError(Mockito.eq(exc.getMessage()), Mockito.same(exc));
	}

	@Test
	public void testDestinationsChangeHandlerNotifiedOnDestChange()
	{
		final IDestinationChangeHandler changeHandler = Mockito.mock(IDestinationChangeHandler.class);
		provider.registerDestinationsChangeHandler(changeHandler);
		provider.handleConfigChange();
		
		Mockito.verify(changeHandler, Mockito.times(1)).handleDestinationsChange();
		Mockito.verify(configStorage, Mockito.times(1)).registerServerConfigChangeHandler(Mockito.same(provider));
	}
	
	@Test
	public void testRegisterDestinationsChangeHandler()
	{
		final IDestinationChangeHandler firstHandler = Mockito.mock(IDestinationChangeHandler.class);
		final IDestinationChangeHandler secondHandler = Mockito.mock(IDestinationChangeHandler.class);
		
		provider.registerDestinationsChangeHandler(firstHandler);
		provider.registerDestinationsChangeHandler(secondHandler);
		provider.handleConfigChange();

		Mockito.verify(configStorage, Mockito.times(1)).registerServerConfigChangeHandler(Mockito.eq(provider));
		Mockito.verify(firstHandler, Mockito.times(1)).handleDestinationsChange();
		Mockito.verify(secondHandler, Mockito.times(1)).handleDestinationsChange();
	}
	
	@Test
	public void testUnregisterDestinationsChangeHandler()
	{
		final IDestinationChangeHandler firstHandler = Mockito.mock(IDestinationChangeHandler.class);
		final IDestinationChangeHandler secondHandler = Mockito.mock(IDestinationChangeHandler.class);

		provider.registerDestinationsChangeHandler(firstHandler);
		provider.registerDestinationsChangeHandler(secondHandler);
		
		provider.unregisterDestinationsChangeHandler(secondHandler);
		provider.handleConfigChange();
		
		Mockito.verify(firstHandler, Mockito.times(1)).handleDestinationsChange();
		Mockito.verify(secondHandler, Mockito.never()).handleDestinationsChange();
	}
	
	@Test
	public void testRegisterDestinationsChangeHandlerTwice()
	{
		final IDestinationChangeHandler handler = Mockito.mock(IDestinationChangeHandler.class);
		
		provider.registerDestinationsChangeHandler(handler);
		provider.registerDestinationsChangeHandler(handler);
		provider.handleConfigChange();
		
		Mockito.verify(handler, Mockito.times(1)).handleDestinationsChange();
	}

	@Test
	public void testProviderWithoutDestChangeHandlerNotInterestedInPrefChange() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ConfigStoreException, ConfigLoadException
	{
		provider = new SrDestinationsProvider()
		{
			@Override
			public void handleConfigChange()
			{
				fail("Unexpected invocation");
			}
		};
		
		IConfigStorage cfgStorage = (IConfigStorage) ReflectionUtils.getFieldValue(provider, "configStorage");
		cfgStorage.resetConfigurations();
	}
}
