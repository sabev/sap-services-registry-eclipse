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
package org.eclipse.servicesregistry.search.core.internal.destinations;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.platform.discovery.runtime.api.IDestinationChangeHandler;
import org.eclipse.platform.discovery.runtime.api.IDestinationsProvider;
import org.eclipse.platform.discovery.runtime.api.ISearchDestination;
import org.eclipse.platform.discovery.runtime.api.impl.DestinationsProvider;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.config.persistency.ConfigLoadException;
import org.eclipse.servicesregistry.core.config.persistency.IConfigChangeHandler;
import org.eclipse.servicesregistry.core.config.persistency.IConfigStorage;
import org.eclipse.servicesregistry.core.config.persistency.SrConfigStorageFactory;
import org.eclipse.servicesregistry.core.internal.logging.ILogger;
import org.eclipse.servicesregistry.core.internal.logging.Logger;

public class SrDestinationsProvider extends DestinationsProvider implements IDestinationsProvider, IConfigChangeHandler
{
	private final IConfigStorage configStorage;
	private final ILogger logger = Logger.instance();
	private final Set<IDestinationChangeHandler> destinationChangeHandlers;

	public SrDestinationsProvider()
	{
		configStorage = SrConfigStorageFactory.getDefault();
		destinationChangeHandlers = new HashSet<IDestinationChangeHandler>();
	}

	public Set<ISearchDestination> getSearchDestinations()
	{
		final Set<ISearchDestination> result = new HashSet<ISearchDestination>();
		try
		{
			for (IServicesRegistrySystem config : configStorage.readConfigurations())
			{
				result.add(new ServicesRegistryDestination(config));
			}
		} catch (ConfigLoadException e)
		{
			logger.logError(e.getMessage(), e);
		}

		return result;
	}

	public synchronized void registerDestinationsChangeHandler(IDestinationChangeHandler handler)
	{
		if(handler == null || this.destinationChangeHandlers.contains(handler))
		{
			//already registered
			return;
		}
		
		// Register as a listener for configuration storage changes in case there are no listeners registered yet
		if(this.destinationChangeHandlers.isEmpty())
		{
			configStorage.registerServerConfigChangeHandler(this);
		}
		
		this.destinationChangeHandlers.add(handler);
	}

	@Override
	public synchronized void unregisterDestinationsChangeHandler(final IDestinationChangeHandler handler)
	{
		if(handler == null)
		{
			return;
		}
		
		this.destinationChangeHandlers.remove(handler);
		
		// Unregister from config changes listener on case there are no delegates to notify
		if(destinationChangeHandlers.isEmpty())
		{
			configStorage.unregisterServerConfigChangeHandler(this);
		}
	}

	@Override
	public void handleConfigChange()
	{
		for(IDestinationChangeHandler h : this.destinationChangeHandlers)
		{
			h.handleDestinationsChange();
		}
	}
}
