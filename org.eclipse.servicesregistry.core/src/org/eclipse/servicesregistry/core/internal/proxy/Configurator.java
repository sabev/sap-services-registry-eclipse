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

import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.eclipse.servicesregistry.core.internal.proxy.factory.IConfigurator;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.proxy.ServicesRegistrySi;

abstract class Configurator implements IConfigurator<ServicesRegistrySi>
{
	protected final IServicesRegistrySystem srSystem;

	Configurator(final IServicesRegistrySystem srSystem)
	{
		this.srSystem = srSystem;
	}
	
	protected Map<String, Object> requestContext(final ServicesRegistrySi port)
	{
		return ((BindingProvider) port).getRequestContext();
	}
}
