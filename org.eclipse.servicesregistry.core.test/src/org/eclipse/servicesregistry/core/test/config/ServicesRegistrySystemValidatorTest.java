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
package org.eclipse.servicesregistry.core.test.config;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;

import org.eclipse.servicesregistry.core.internal.config.ServicesRegistrySystemValidator;
import org.eclipse.servicesregistry.core.internal.text.SrCoreMessages;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.mockito.MockitoAnnotations;

public class ServicesRegistrySystemValidatorTest {
	
	@Mock
	private IServicesRegistrySystem config;

	private ServicesRegistrySystemValidator validator;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		validator = new ServicesRegistrySystemValidator();
	}

	@Test
	public void testValidateConfigWithNoHost()
	{
		when(config.host()).thenReturn("");
		final IStatus validationStatus = validator.validateCreatedObject(config, new HashSet<IServicesRegistrySystem>());
		assertEquals("Error status expected", IStatus.ERROR, validationStatus.getSeverity());
		assertEquals("Unexpected error message", SrCoreMessages.CFG_NO_HOST_ERROR, validationStatus.getMessage());
	}

	@Test
	public void testValidateConfigWithTooLongHost()
	{
		final StringBuilder hostNameBuilder = new StringBuilder();
		for (int i = 0; i < 100; i++)
		{
			hostNameBuilder.append("A");
		}

		when(config.host()).thenReturn(hostNameBuilder.toString());
		illegalHostNameTest(config);
	}

	@Test
	public void testValidateConfigWithIllegalDomainName()
	{
		when(config.host()).thenReturn("^");
		illegalHostNameTest(config);
	}

	@Test
	public void testValidateConfigWithIllegalIp()
	{
		when(config.host()).thenReturn("1.2.3");
		illegalHostNameTest(config);

		when(config.host()).thenReturn("1.2.3.257");
		illegalHostNameTest(config);

		when(config.host()).thenReturn("1.2.test.4");
		illegalHostNameTest(config);
	}

	@Test
	public void testValidateWithInvalidPort()
	{
		when(config.host()).thenReturn("host");
		when(config.port()).thenReturn(Integer.MAX_VALUE);


		final IStatus validationStatus = validator.validateCreatedObject(config, new HashSet<IServicesRegistrySystem>());
		assertEquals("Error status expected", IStatus.ERROR, validationStatus.getSeverity());
		assertEquals("Unexpected error message", SrCoreMessages.CFG_INVALID_PORT_VALUE, validationStatus.getMessage());
	}

	@Test
	public void testValidateWithCredentialsNotStored()
	{
		when(config.host()).thenReturn("host");
		when(config.port()).thenReturn(1080);
		
		when(config.areCredentialsStored()).thenReturn(false);

		final IStatus validationStatus = validator.validateCreatedObject(config, new HashSet<IServicesRegistrySystem>());
		assertEquals("OK status expected", IStatus.OK, validationStatus.getSeverity());
	}
	
	@Test
	public void testValidateWithUsernameNotSpecified()
	{
		when(config.host()).thenReturn("host");
		when(config.port()).thenReturn(1080);
		when(config.areCredentialsStored()).thenReturn(true);

		when(config.userName()).thenReturn("");

		final IStatus validationStatus = validator.validateCreatedObject(config, new HashSet<IServicesRegistrySystem>());
		assertEquals("Error status expected", IStatus.ERROR, validationStatus.getSeverity());
		assertEquals("Unexpected error message", SrCoreMessages.CFG_NO_USER_ERROR, validationStatus.getMessage());
	}

	@Test
	public void testValidateWithValidConfiguration()
	{
		when(config.host()).thenReturn("host");
		when(config.port()).thenReturn(1080);
		when(config.areCredentialsStored()).thenReturn(true);

		when(config.userName()).thenReturn("Drake");

		final IStatus validationStatus = validator.validateCreatedObject(config, new HashSet<IServicesRegistrySystem>());
		assertEquals("INFO status expected", IStatus.INFO, validationStatus.getSeverity());
		assertEquals("Unexpected info message", SrCoreMessages.CFG_STORE_CREDENTIALS_INFO, validationStatus.getMessage());
	}
	
	@Test
	public void testDuplicateConfigurations()
	{
		final IServicesRegistrySystem existingConfig = createConfig("connDisplayName", "hostName", 1, false, null, null);
		setupMockWithData(config, "connDisplayName", "hostName", 1, false, null, null);
		final IStatus validationStatus = validator.validateCreatedObject(config, new HashSet<IServicesRegistrySystem>(Arrays.asList(new IServicesRegistrySystem[] { existingConfig })));
		assertEquals("ERROR status expected", IStatus.ERROR, validationStatus.getSeverity());
		assertEquals("Unexpected error message", MessageFormat.format(SrCoreMessages.CFG_ALREADY_EXISTS, existingConfig.displayName()), validationStatus.getMessage());
	}

	@Test
	public void testDuplicateConfigurationsWithDifferentCase()
	{
		final IServicesRegistrySystem existingConfig = createConfig("conndisplayname", "hostname", 1, false, null, null);
		setupMockWithData(config, "connDisplayName", "hostName", 1, false, null, null);
		final IStatus validationStatus = validator.validateCreatedObject(config, new HashSet<IServicesRegistrySystem>(Arrays.asList(new IServicesRegistrySystem[] { existingConfig })));
		assertEquals("ERROR status expected", IStatus.ERROR, validationStatus.getSeverity());
		assertEquals("Unexpected error message", MessageFormat.format(SrCoreMessages.CFG_ALREADY_EXISTS, existingConfig.displayName()), validationStatus.getMessage());
	}

	@Test
	public void testNoDuplicationsFoundNoCredentials()
	{
		final IServicesRegistrySystem existingConfig = createConfig("conndisplayname", "otherhostname", 1, false, null, null);
		setupMockWithData(config, "connDisplayName", "hostName", 1,  false, null, null);
		final IStatus validationStatus = validator.validateCreatedObject(config, new HashSet<IServicesRegistrySystem>(Arrays.asList(new IServicesRegistrySystem[] { existingConfig })));
		assertEquals("OK status expected", IStatus.OK, validationStatus.getSeverity());
	}
	
	@Test
	public void testNoDuplicationsFoundWithCredentials()
	{
		final IServicesRegistrySystem existingConfig = createConfig("conndisplayname", "otherhostname", 1, false, null, null);
		setupMockWithData(config,"connDisplayName", "hostName", 1, true, "Drake", "draker");
		final IStatus validationStatus = validator.validateCreatedObject(config, new HashSet<IServicesRegistrySystem>(Arrays.asList(new IServicesRegistrySystem[] { existingConfig })));
		assertEquals("INFO status expected", IStatus.INFO, validationStatus.getSeverity());
		assertEquals("Unexpected info message", SrCoreMessages.CFG_STORE_CREDENTIALS_INFO, validationStatus.getMessage());
	}

	private void illegalHostNameTest(final IServicesRegistrySystem configToValidate)
	{
		final IStatus validationStatus = validator.validateCreatedObject(configToValidate, new HashSet<IServicesRegistrySystem>());
		assertEquals("Error status expected", IStatus.ERROR, validationStatus.getSeverity());
		assertEquals("Unexpected error message", SrCoreMessages.CFG_INVALID_HOST_ERROR, validationStatus.getMessage());
	}

	private IServicesRegistrySystem createConfig(final String displayName, final String hostName, final int port, final boolean credentialsStored, final String userName, final String password)
	{
		final IServicesRegistrySystem config = mock(IServicesRegistrySystem.class);
		setupMockWithData(config, displayName, hostName, port, credentialsStored, userName, password);
		return config;
	}

	private void setupMockWithData(final IServicesRegistrySystem config, final String displayName, final String hostName, final int port,
			final boolean credentialsStored, final String userName, final String password)
	{
		when(config.displayName()).thenReturn(displayName);
		when(config.host()).thenReturn(hostName);
		when(config.port()).thenReturn(port);
		when(config.areCredentialsStored()).thenReturn(credentialsStored);
		when(config.userName()).thenReturn(userName);
		when(config.password()).thenReturn(password);
	}
}
