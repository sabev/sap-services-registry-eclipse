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
package org.eclipse.servicesregistry.ui.test.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.internal.config.ServicesRegistrySystemValidator;
import org.eclipse.servicesregistry.ui.test.config.pageobjects.EditSrConfigDialogPageObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EditSrConfigDialogTest {
	
	
	private static final String CONNECTION_NAME = "MyConnection";
	private static final String SERVER_HOST = "MyHost";
	private static final int SERVER_PORT = 1234;
	private static final String USER_NAME = "MyUser";
	private static final String USER_PASS = "MyPassword";

	@Mock
	private IServicesRegistrySystem existingConfig;

	private EditSrConfigDialogPageObject configDialog;
	
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		when(existingConfig.displayName()).thenReturn(CONNECTION_NAME);
		when(existingConfig.host()).thenReturn(SERVER_HOST);
		when(existingConfig.port()).thenReturn(SERVER_PORT);
		
		configDialog = new EditSrConfigDialogPageObject(new ServicesRegistrySystemValidator(), new HashSet<IServicesRegistrySystem>(), existingConfig);
	}
	
	@Test
	public void testConfigValuesWithCredentialsStoredAreLoaded() 
	{
		when(existingConfig.useHttps()).thenReturn(false);
		when(existingConfig.areCredentialsStored()).thenReturn(true);
		when(existingConfig.userName()).thenReturn(USER_NAME);
		when(existingConfig.password()).thenReturn(USER_PASS);
		
		configDialog.open();
		
		
		assertEquals("Unexpected connecttion name", CONNECTION_NAME, configDialog.getConnectionName());
		assertEquals("Unexpected host", SERVER_HOST, configDialog.getHost());
		assertEquals("Unexpected port", Integer.toString(SERVER_PORT), configDialog.getPort());
		
		assertFalse("Use https button are expected to be unchecked", configDialog.isUseHttps());
		assertTrue("User credentials are expected to be stored", configDialog.areCredentialsStored());
		assertEquals("Unexpected user name", USER_NAME, configDialog.getUserName());
		assertEquals("Unexpected password", USER_PASS, configDialog.getPassword());
		assertTrue("User name text should be editable",configDialog.isUserNameEditable());
		assertTrue("Password text should be editable", configDialog.isPasswordEditable());
	}
	
	@Test
	public void testConfigValuesWithCredentialsNotStoredAreLoaded() 
	{
		when(existingConfig.useHttps()).thenReturn(false);
		when(existingConfig.areCredentialsStored()).thenReturn(false);
		
		configDialog.open();
		assertEquals("Unexpected connecttion name", CONNECTION_NAME, configDialog.getConnectionName());
		assertEquals("Unexpected host", SERVER_HOST, configDialog.getHost());
		assertEquals("Unexpected port", Integer.toString(SERVER_PORT), configDialog.getPort());
		assertFalse("Use https button are expected to be unchecked", configDialog.isUseHttps());
		assertFalse("User credentials are not expected to be stored", configDialog.areCredentialsStored());
		assertEquals("User name text should be blank", "", configDialog.getUserName());
		assertEquals("Password text should be blank", "", configDialog.getPassword());
		assertFalse("User name text should not be editable", configDialog.isUserNameEditable());
		assertFalse("Password text should not be editabe", configDialog.isPasswordEditable());
	}
	
	@Test
	public void testOKButtonEnabledByDefault()
	{
		when(existingConfig.useHttps()).thenReturn(false);
		when(existingConfig.areCredentialsStored()).thenReturn(false);
		
		configDialog.open();
		assertTrue("OK button should be enabled by default", configDialog.canOk());
	}
	
	@Test
	public void testUseHttpsButtonSelectectedLoaded()
	{
		when(existingConfig.useHttps()).thenReturn(true);
		when(existingConfig.areCredentialsStored()).thenReturn(false);
		
		configDialog.open();
		
		assertEquals("Unexpected connecttion name", CONNECTION_NAME, configDialog.getConnectionName());
		assertEquals("Unexpected host", SERVER_HOST, configDialog.getHost());
		assertEquals("Unexpected port", Integer.toString(SERVER_PORT), configDialog.getPort());
		assertTrue("Use https button are expected to be checked", configDialog.isUseHttps());
		assertFalse("User credentials are not expected to be stored",configDialog.areCredentialsStored());
		assertEquals("User name text should be blank", "",configDialog.getUserName());
		assertEquals("Password text should be blank", "", configDialog.getPassword());
		assertFalse("User name text should not be editable", configDialog.isUserNameEditable());
		assertFalse("Password text should not be editabe", configDialog.isPasswordEditable());
	}
}
