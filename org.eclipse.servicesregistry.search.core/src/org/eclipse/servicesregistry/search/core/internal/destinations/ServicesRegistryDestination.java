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

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.search.core.destinations.IServicesRegistryDestination;

public class ServicesRegistryDestination implements IServicesRegistryDestination
{
	private final IServicesRegistrySystem srSystem;

	public ServicesRegistryDestination(final IServicesRegistrySystem srSystem)
	{
		this.srSystem = srSystem;
	}

	@Override
	public String getDisplayName()
	{
		return srSystem.displayName();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (!obj.getClass().equals(this.getClass()))
		{
			return false;
		}

		final ServicesRegistryDestination dest = (ServicesRegistryDestination) obj;
		return configurationEqual(dest.srSystem);
	}
	
	private boolean configurationEqual(final IServicesRegistrySystem otherSrConfig)
	{
		if(!this.srSystem.displayName().equals(otherSrConfig.displayName()))
		{
			return false;
		}
		
		if(!this.srSystem.host().equals(otherSrConfig.host()))
		{
			return false;
		}
		
		if(this.srSystem.port() != otherSrConfig.port())
		{
			return false;
		}
		
		if(this.srSystem.useHttps() != otherSrConfig.useHttps())
		{
			return false;
		}
		
		if(this.srSystem.areCredentialsStored() != otherSrConfig.areCredentialsStored())
		{
			return false;
		}
		
		if(this.srSystem.areCredentialsStored())
		{
			if(!srSystem.userName().equals(otherSrConfig.userName()))
			{
				return false;
			}
			if(!srSystem.password().equals(otherSrConfig.password()))
			{
				return false;
			}
		}
		
		return true;
	}

	@Override
	public int hashCode()
	{
		int hash = 1;
		hash = 31*hash + this.srSystem.displayName().hashCode();
		hash = 31*hash + this.srSystem.host().hashCode();
		hash = 31*hash + this.srSystem.port();
		hash = 31*hash + (srSystem.areCredentialsStored() ? 1 : 0);
		hash = 31*hash + (srSystem.useHttps() ? 1 : 0);
		if(this.srSystem.areCredentialsStored())
		{
			hash = 31*hash + this.srSystem.userName().hashCode();
			hash = 31*hash + this.srSystem.password().hashCode();
		}

		return hash;
	}

	@Override
	public IServicesRegistrySystem servicesRegistrySystem()
	{
		return this.srSystem;
	}
}
