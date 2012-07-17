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
 * Configures the endpoint addres of a {@link ServicesRegistrySi} proxy instance to be able to communicate with the services registry system
 * represented by {@link IServicesRegistrySystem} specified
 * 
 * @author Danail Branekov
 */
public class EndpointAddressConfigurator extends Configurator
{
	private static final String ENDPOINT_PATH = "/ServicesRegistrySiService/ServicesRegistrySiPort";
	private static final String HTTPS = "https://";
	private static final String HTTP = "http://";
	private static final String COLON = ":";

	public EndpointAddressConfigurator(final IServicesRegistrySystem srSystem)
	{
		super(srSystem);
	}

	@Override
	public void configure(final ServicesRegistrySi port)
	{
		setEndpointAddress(port, createEndpointAddress(srSystem));
	}

	private void setEndpointAddress(final ServicesRegistrySi port, final String endpointAddress)
	{
		requestContext(port).put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
	}

	private String createEndpointAddress(final IServicesRegistrySystem srSystem)
	{
		final StringBuilder endpointBuilder = new StringBuilder();
		endpointBuilder.append(srSystem.useHttps() ? HTTPS : HTTP);
		endpointBuilder.append(srSystem.host());
		endpointBuilder.append(COLON);
		endpointBuilder.append(srSystem.port());
		endpointBuilder.append(ENDPOINT_PATH);
		return endpointBuilder.toString();
	}
}
