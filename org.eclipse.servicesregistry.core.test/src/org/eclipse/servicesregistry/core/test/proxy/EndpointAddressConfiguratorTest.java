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

import javax.xml.ws.BindingProvider;

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.internal.proxy.EndpointAddressConfigurator;
import org.eclipse.servicesregistry.core.internal.proxy.factory.IConfigurator;
import org.eclipse.servicesregistry.core.internal.proxy.factory.ServicesRegistryProxyFactory;
import org.eclipse.servicesregistry.proxy.ServicesRegistrySi;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class EndpointAddressConfiguratorTest
{
	private static final String HOST = "my.sr.host";
	private static final int PORT = 12345;

	private ServicesRegistrySi proxy;
	private IConfigurator<ServicesRegistrySi> configurator;
	private IServicesRegistrySystem srSystem;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp()
	{
		mockSrSystem();
		proxy = new ServicesRegistryProxyFactory().createProxy();
		configurator = new EndpointAddressConfigurator(srSystem);
	}

	@Test
	public void testConfigureHttp()
	{
		useHttp();
		configurator.configure(proxy);
		Assert.assertEquals("Unexpected endpoint address", "http://my.sr.host:12345/ServicesRegistrySiService/ServicesRegistrySiPort", configuredEndpointAddress());
	}

	@Test
	public void testConfigureHttps()
	{
		useHttps();
		configurator.configure(proxy);
		Assert.assertEquals("Unexpected endpoint address", "https://my.sr.host:12345/ServicesRegistrySiService/ServicesRegistrySiPort", configuredEndpointAddress());
	}

	private void useHttp()
	{
		Mockito.stub(srSystem.useHttps()).toReturn(false);
	}

	private void useHttps()
	{
		Mockito.stub(srSystem.useHttps()).toReturn(true);
	}

	private String configuredEndpointAddress()
	{
		return (String) ((BindingProvider) proxy).getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
	}

	private void mockSrSystem()
	{
		srSystem = Mockito.mock(IServicesRegistrySystem.class);
		Mockito.stub(srSystem.host()).toReturn(HOST);
		Mockito.stub(srSystem.port()).toReturn(PORT);
	}

}
