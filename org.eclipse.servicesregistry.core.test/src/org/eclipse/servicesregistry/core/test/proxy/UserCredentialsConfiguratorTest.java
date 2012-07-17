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

import static org.junit.Assert.assertFalse;

import java.text.MessageFormat;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import junit.framework.Assert;

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.internal.proxy.UserCredentialsConfigurator;
import org.eclipse.servicesregistry.core.internal.proxy.factory.IConfigurator;
import org.eclipse.servicesregistry.core.internal.proxy.factory.ServicesRegistryProxyFactory;
import org.eclipse.servicesregistry.proxy.ServicesRegistrySi;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UserCredentialsConfiguratorTest
{
	private IConfigurator<ServicesRegistrySi> configurator;
	private IServicesRegistrySystem srSystem;
	private ServicesRegistrySi proxy;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp()
	{
		proxy = new ServicesRegistryProxyFactory().createProxy();
		srSystem = Mockito.mock(IServicesRegistrySystem.class);
		configurator = new UserCredentialsConfigurator(srSystem);
	}

	@Test
	public void testSetCredentials()
	{
		mockCredentials("myuser", "yourpass");
		configurator.configure(proxy);
		Assert.assertEquals("Unexpected username", "myuser", configuredUserName());
		Assert.assertEquals("Unexpected password", "yourpass", configuredPassword());
	}

	@Test
	public void testNoCredentialsAvailable()
	{
		configurator.configure(proxy);
		assertRequestContextPropertyNotSet(BindingProvider.PASSWORD_PROPERTY);
		assertRequestContextPropertyNotSet(BindingProvider.USERNAME_PROPERTY);
	}

	private String configuredPassword()
	{
		return (String) requestContext().get(BindingProvider.PASSWORD_PROPERTY);
	}

	private String configuredUserName()
	{
		return (String) requestContext().get(BindingProvider.USERNAME_PROPERTY);
	}
	
	private void assertRequestContextPropertyNotSet(final String propertyName)
	{
		assertFalse(MessageFormat.format("Property {0} unexpectedly set", propertyName), requestContext().containsKey(propertyName));
	}

	private Map<String, Object> requestContext()
	{
		return ((BindingProvider) proxy).getRequestContext();
	}

	private void mockCredentials(final String userName, final String password)
	{
		Mockito.stub(srSystem.userName()).toReturn(userName);
		Mockito.stub(srSystem.password()).toReturn(password);
	}
}
