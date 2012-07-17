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
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.*;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.internal.config.ServicesRegistrySystemValidator;
import org.eclipse.servicesregistry.core.internal.text.SrCoreMessages;
import org.eclipse.servicesregistry.ui.internal.text.SrUiMessages;
import org.eclipse.servicesregistry.ui.test.config.pageobjects.AbstractSrConfigDialogPageObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class AbstractSrConfigDialogTest 
{
	
	private static final String CONNECTION_NAME = "MyConnection";
	private static final String DEFAULT_CONNECTION_NAME = "MyHost:1234";
	private static final String SERVER_HOST = "MyHost";
	private static final String SERVER_PORT = "1234";
	private static final String USER_NAME = "MyUser";
	private static final String USER_PASS = "MyPassword";

	private Set<IServicesRegistrySystem> existingConfigs;
	private ServicesRegistrySystemValidator validator;
	
	private AbstractSrConfigDialogPageObject configDialog;
	
	@Before
	public void setUp() throws Exception
	{
		existingConfigs = new HashSet<IServicesRegistrySystem>();
		validator = new ServicesRegistrySystemValidator();
		
		configDialog = new AbstractSrConfigDialogPageObject(validator, existingConfigs);
		configDialog.open();
	}
	
	@After
	public void tearDown() 
	{
		configDialog.verifyHelpIsSet();
		configDialog.cancel();
	}
	
	@Test
	public void testCheckDefaultUiState()
	{
		assertFalse("Store credentials should be false by default", configDialog.areCredentialsStored());
		assertFalse("Use https button should be false by default", configDialog.isUseHttps());

		assertFalse("User name should not be editable if credentials are not stored", configDialog.isUserNameEditable());
		assertFalse("Password should not be editable if credentials are not stored", configDialog.isPasswordEditable());

		assertEquals("Unexpected connection name hint", MessageFormat.format(SrUiMessages.CFG_DLG_USE_DEFAULT_HINT_CONN, ':'), configDialog.getConnectionNameHint());
	}

	@Test
	public void testSelectingStoreCredentialsResetsCredentials()
	{
		configDialog.toggleStoreCredentials();
		configDialog.setUserName(USER_NAME);
		configDialog.setPassword(USER_PASS);

		configDialog.toggleStoreCredentials();
		assertFalse("Username text is editable", configDialog.isUserNameEditable());
		assertFalse("Password text is editable", configDialog.isPasswordEditable());
		assertEquals("Username not reset", "", configDialog.getUserName());
		assertEquals("Password not reset", "", configDialog.getPassword());
	}

	@Test
	public void testGetSatus()
	{
		configDialog.setHost("a");
		configDialog.setHost("");

		IStatus status = configDialog.getStatus();
		assertTrue("Error status expected for not specifying host", status.getSeverity() == IStatus.ERROR);
		assertEquals("Unexpected error message", SrCoreMessages.CFG_NO_HOST_ERROR, status.getMessage());

		configDialog.setHost("http://myhost");

		status = configDialog.getStatus();
		assertTrue("Error status expected for not specifying host", status.getSeverity() == IStatus.ERROR);
		assertEquals("Unexpected error message", SrCoreMessages.CFG_INVALID_HOST_ERROR, status.getMessage());

		configDialog.setHost("myhost");
		status = configDialog.getStatus();
		assertTrue("Error status expected for not specifying port", status.getSeverity() == IStatus.ERROR);
		assertEquals("Unexpected error message", SrCoreMessages.CFG_INVALID_PORT_VALUE, status.getMessage());
		
		configDialog.setPort("AAA");
		status = configDialog.getStatus();
		assertTrue("Error status expected for specifying invalid port", status.getSeverity() == IStatus.ERROR);
		assertEquals("Unexpected error message", SrCoreMessages.CFG_INVALID_PORT_VALUE, status.getMessage());

		configDialog.setPort("1234");
		status = configDialog.getStatus();
		assertTrue("OK status expected", status.getSeverity() == IStatus.OK);

		configDialog.setConnectionName("MyConnection");
		status = configDialog.getStatus();
		assertTrue("OK status expected", status.getSeverity() == IStatus.OK);

		configDialog.toggleStoreCredentials();
		status = configDialog.getStatus();
		assertTrue("Error status expected for not specifying user name", status.getSeverity() == IStatus.ERROR);
		assertEquals("Unexpected error message", SrCoreMessages.CFG_NO_USER_ERROR, status.getMessage());

		configDialog.setUserName("myuser");
		status = configDialog.getStatus();
		assertTrue("INFO status expected", status.getSeverity() == IStatus.INFO);
		assertEquals("Unexpected info message", SrCoreMessages.CFG_STORE_CREDENTIALS_INFO, status.getMessage());
	}

	@Test
	public void testDefaultConnectionNameUpdated()
	{
		configDialog.setConnectionName(DEFAULT_CONNECTION_NAME);
		configDialog.setHost(SERVER_HOST);
		configDialog.setPort(SERVER_PORT);
		
		final String newHost = SERVER_HOST+"new";
		configDialog.setHost(newHost);
		assertEquals(newHost + ":" + SERVER_PORT, configDialog.getConnectionName());
	}
	
	@Test
	public void testConnectionNameSetToDefaultValueWhenNotExplicitelySpecified()
	{
		configDialog.setHost(SERVER_HOST);
		configDialog.setPort(SERVER_PORT);
		configDialog.ok();
		assertEquals("Connection name was not set to default when the user did not specify it explicitely", DEFAULT_CONNECTION_NAME, configDialog.getConfig().displayName());
	}
	
	@Test
	public void testGetConfigWhenDialogConfirmed()
	{
		configDialog.setConnectionName(CONNECTION_NAME);
		configDialog.setHost(SERVER_HOST);
		configDialog.setPort(SERVER_PORT);
		configDialog.toggleStoreCredentials();
		configDialog.setUserName(USER_NAME);
		configDialog.setPassword(USER_PASS);
		
		configDialog.ok();

		final IServicesRegistrySystem config = configDialog.getConfig();
		assertNotNull(config);
		assertEquals("Unexpected connection name", CONNECTION_NAME, config.displayName());
		assertEquals("Unexpected connection name", SERVER_HOST, config.host());
		assertEquals("Unexpected connection name", Integer.parseInt(SERVER_PORT), config.port());
		assertEquals("Unexpected connection name", USER_NAME, config.userName());
		assertEquals("Unexpected connection name", USER_PASS, config.password());
	}
	
	@Test
	public void testCreateDuplicateConfig()
	{
		IServicesRegistrySystem existing = mock(IServicesRegistrySystem.class);
		when(existing.host()).thenReturn("myhost");
		when(existing.port()).thenReturn(123);
		
		existingConfigs.add(existing);
		
		// Enter and delete something - the dialog according the UI policy should come up with a non-error status
		configDialog.setHost("myhost");
		configDialog.setPort("123");

		IStatus status = configDialog.getStatus();
		assertTrue("Error status expected for duplicated configs", status.getSeverity() == IStatus.ERROR);
		assertEquals("Unexpected error message", SrCoreMessages.CFG_ALREADY_EXISTS, status.getMessage());
	}
	
}