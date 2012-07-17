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
package org.eclipse.servicesregistry.core.internal.proxy.factory;

import java.net.MalformedURLException;

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.proxy.ServicesRegistrySi;
import org.eclipse.servicesregistry.proxy.ServicesRegistrySiService;

/**
 * Factory for creating {@link ServicesRegistrySi} instances
 * 
 * @author Danail Branekov
 * 
 */
public class ServicesRegistryProxyFactory
{
	private final IConfigurator<ServicesRegistrySi>[] configurator;

	/**
	 * Construct a factory which creates a {@link IServicesRegistrySystem} instance capable of cummicating with the services registry system
	 * 
	 * @param srSystem
	 *            the services registry system; must not be null
	 */
	public ServicesRegistryProxyFactory(final IServicesRegistrySystem srSystem)
	{
		this(new DefaultProxyConfiguratorsProvider().getConfigurators(srSystem));
	}

	public ServicesRegistryProxyFactory(final IConfigurator<ServicesRegistrySi>... configurators)
	{
		this.configurator = configurators;
	}

	public ServicesRegistrySi createProxy()
	{
		final ServicesRegistrySi port = createDefaultPort();
		for (IConfigurator<ServicesRegistrySi> conf : configurator)
		{
			conf.configure(port);
		}
		return port;
	}

	private ServicesRegistrySi createDefaultPort()
	{
		try
		{
			return new ServicesRegistrySiService().getServicesRegistrySiPort();
		}
		catch (MalformedURLException e)
		{
			throw new IllegalStateException(e);
		}
	}
}
