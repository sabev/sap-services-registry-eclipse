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
package org.eclipse.servicesregistry.core.internal.proxy;

import javax.xml.ws.BindingProvider;

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.proxy.ServicesRegistrySi;

/**
 * Configures the user credentials on a {@link ServicesRegistrySi} instance in order to be able to cummunicate with the services registry system
 * specified by the {@link IServicesRegistrySystem} instance specified
 * 
 * @author Danail Branekov
 * 
 */
public class UserCredentialsConfigurator extends Configurator
{
	public UserCredentialsConfigurator(final IServicesRegistrySystem srSystem)
	{
		super(srSystem);
	}

	@Override
	public void configure(final ServicesRegistrySi proxy)
	{
		setUserName(proxy, srSystem.userName());
		setPassword(proxy, srSystem.password());
	}

	private void setPassword(final ServicesRegistrySi proxy, final String password)
	{
		setRequestContextNonNullProperty(proxy, BindingProvider.PASSWORD_PROPERTY, password);
	}

	private void setUserName(final ServicesRegistrySi proxy, final String userName)
	{
		setRequestContextNonNullProperty(proxy, BindingProvider.USERNAME_PROPERTY, userName);
	}
	
	private void setRequestContextNonNullProperty(final ServicesRegistrySi proxy, final String propName, final Object propValue)
	{
		if(propValue == null)
		{
			return;
		}
		requestContext(proxy).put(propName, propValue);
	}
}
