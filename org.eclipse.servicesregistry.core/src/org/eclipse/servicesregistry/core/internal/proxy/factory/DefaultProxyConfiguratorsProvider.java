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

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.internal.proxy.EndpointAddressConfigurator;
import org.eclipse.servicesregistry.core.internal.proxy.UserCredentialsConfigurator;
import org.eclipse.servicesregistry.proxy.ServicesRegistrySi;

class DefaultProxyConfiguratorsProvider
{
	@SuppressWarnings("unchecked")
	public IConfigurator<ServicesRegistrySi>[] getConfigurators(final IServicesRegistrySystem srSystem)
	{
		return new IConfigurator[]{new EndpointAddressConfigurator(srSystem), new UserCredentialsConfigurator(srSystem)};
	}
}
