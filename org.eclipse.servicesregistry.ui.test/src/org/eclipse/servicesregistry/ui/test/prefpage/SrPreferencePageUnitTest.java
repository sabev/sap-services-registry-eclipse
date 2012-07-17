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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.servicesregistry.core.config.persistency.ConfigLoadException;
import org.eclipse.servicesregistry.core.config.persistency.ConfigStoreException;
import org.eclipse.servicesregistry.core.config.persistency.IPreferencesController;
import org.eclipse.servicesregistry.core.internal.logging.ILogger;
import org.eclipse.servicesregistry.testutils.ReflectionUtils;
import org.eclipse.servicesregistry.ui.internal.prefpage.SrPreferencePage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class SrPreferencePageUnitTest
{
	@Mock
	private IPreferencesController controller;
	private SrPreferencePage page;
	
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		setupPage();
	}
	
	
	private void setupPage()
	{
		page = new SrPreferencePage(){
			@Override
			protected IPreferencesController createPreferencesController()
			{
				return controller;
			}
		};
		page.init(null);
	}

	@Test
	public void testInitInitializesController() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		final IPreferencesController c = (IPreferencesController) ReflectionUtils.getFieldValue(page, "controller");
		assertTrue("Server configurations not initialized properly", c == controller);
	}
	
	@Test
	public void testPerformOkStoresPreferences() throws ConfigStoreException, ConfigLoadException
	{
		assertTrue(page.performOk());
		Mockito.verify(controller, Mockito.times(1)).storeConfigurations();
	}

	@Test
	public void testPerformOkWithExcOnPreferencesStore() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ConfigStoreException, ConfigLoadException
	{
		final ConfigStoreException exc = new ConfigStoreException();
		final ILogger loggerMock = Mockito.mock(ILogger.class);
		ReflectionUtils.setFieldValue(page, "logger", loggerMock);
		Mockito.doThrow(exc).when(controller).storeConfigurations();
		
		assertFalse(page.performOk());
		Mockito.verify(loggerMock, Mockito.times(1)).logError(Mockito.eq(exc.getMessage()), Mockito.eq(exc));
	}
	
}
