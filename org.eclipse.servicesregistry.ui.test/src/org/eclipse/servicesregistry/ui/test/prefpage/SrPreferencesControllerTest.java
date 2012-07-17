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
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.window.Window;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.config.persistency.ConfigCreationCanceledException;
import org.eclipse.servicesregistry.core.config.persistency.ConfigLoadException;
import org.eclipse.servicesregistry.core.config.persistency.ConfigStoreException;
import org.eclipse.servicesregistry.core.config.persistency.IConfigStorage;
import org.eclipse.servicesregistry.core.config.persistency.IUserCredentialsHandler;
import org.eclipse.servicesregistry.ui.internal.config.IConfigDialog;
import org.eclipse.servicesregistry.ui.internal.prefpage.SrPreferencesController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class SrPreferencesControllerTest
{
	@Mock
	private IConfigDialog newConfigDialog;
	@Mock
	private IConfigDialog editConfigDialog;
	@Mock
	private IServicesRegistrySystem config1;
	@Mock
	private IServicesRegistrySystem config2;
	@Mock
	private IServicesRegistrySystem newConfig;
	@Mock
	private IConfigStorage configStorage;
	@Mock
	private IUserCredentialsHandler credentialsHandler;
	
	private SrPreferencesController controller;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		Mockito.stub(configStorage.readConfigurations()).toReturn(new HashSet<IServicesRegistrySystem>(Arrays.asList(new IServicesRegistrySystem[]{config1, config2})));
   		
		controller = new SrPreferencesController(null, configStorage, credentialsHandler)
		{
			@Override
			protected IConfigDialog createEditConfigDialog(IServicesRegistrySystem config)
			{
				return editConfigDialog;
			}
			
			@Override
			protected IConfigDialog createNewConfigDialog()
			{
				return newConfigDialog;
			}
		};
	}

	@Test
	public void testCreateNewConfig() throws ConfigCreationCanceledException
	{
		stubDialogOpen(newConfigDialog, Window.OK);
		stubDialogGetConfig(newConfigDialog, newConfig);
	
		assertTrue("Unexpected config created", controller.createNewConfiguration() == newConfig);
		Mockito.verify(newConfigDialog, Mockito.times(1)).open();
		Mockito.verify(newConfigDialog, Mockito.times(1)).getConfig();
		
		assertEquals("Three configs expected", 3, controller.getConfigurations().size());
		assertTrue("Newly created config not in configs set", controller.getConfigurations().contains(newConfig));
	}

	@Test
	public void testCreateNewConfigCanceled()
	{
		stubDialogOpen(newConfigDialog, Window.CANCEL);
		
		try
		{
			controller.createNewConfiguration();
			fail("ConfigCreationCanceledException expected");
		}
		catch(ConfigCreationCanceledException e)
		{
			//expected
		}
		assertEquals("Two configs expected", 2, controller.getConfigurations().size());
		Mockito.verify(newConfigDialog, Mockito.never()).getConfig();
	}

	@Test
	public void testEditConfig()
	{
		stubDialogOpen(editConfigDialog, Window.OK);
		stubDialogGetConfig(editConfigDialog, newConfig);

		controller.editConfiguration(config1);
		
		assertEquals("Two configs expected", 2, controller.getConfigurations().size());
		assertTrue("New config not returned", controller.getConfigurations().contains(newConfig));
		assertTrue("Config2 not returned", controller.getConfigurations().contains(config2));
	}
	
	@Test
	public void testEditConfigCanceled()
	{
		stubDialogOpen(editConfigDialog, Window.CANCEL);

		controller.editConfiguration(config1);
		
		assertEquals("Two configs expected", 2, controller.getConfigurations().size());
		assertTrue("Config1 not returned", controller.getConfigurations().contains(config1));
		assertTrue("Config2 not returned", controller.getConfigurations().contains(config2));
	}
	
	@Test
	public void testDeleteConfig()
	{
		controller.deleteConfiguration(config1);
		assertEquals("One config expected", 1, controller.getConfigurations().size());
		assertTrue("Config2 not returned", controller.getConfigurations().contains(config2));
	}

	@Test
	public void testStoreConfigs() throws ConfigStoreException, ConfigLoadException
	{
		controller.deleteConfiguration(config1);
		final Set<IServicesRegistrySystem> configs = controller.getConfigurations();
		controller.storeConfigurations();
		Mockito.verify(configStorage, Mockito.times(1)).storeConfigurations(Mockito.eq(configs), Mockito.eq(credentialsHandler));
	}

	@Test
	public void testStoreConfigsWithException() throws ConfigLoadException, ConfigStoreException
	{
		final ConfigLoadException exc = new ConfigLoadException();
		controller.deleteConfiguration(config1);
		final Set<IServicesRegistrySystem> configs = controller.getConfigurations();
		Mockito.doThrow(exc).when(configStorage).storeConfigurations(Mockito.eq(configs), Mockito.eq(credentialsHandler));
		
		try
		{
			controller.storeConfigurations();
			fail("ConfigStoreException expected");
		} catch (ConfigLoadException e)
		{
			assertTrue("Unexpected excetion", e == exc);
		}
	}
	
	private void stubDialogOpen(final IConfigDialog dlg, final int result)
	{
		Mockito.stub(dlg.open()).toReturn(result);
	}
	
	private void stubDialogGetConfig(final IConfigDialog dlg, final IServicesRegistrySystem result)
	{
		Mockito.stub(dlg.getConfig()).toReturn(result);
	}
	
}
