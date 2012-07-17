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
package org.eclipse.servicesregistry.core.internal.config;

import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.platform.discovery.util.internal.StatusUtils;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.config.ServicesRegistrySystemComparator;
import org.eclipse.servicesregistry.core.internal.text.SrCoreMessages;

/**
 * Implementation of the {@link ICreatedObjectValidator} interface which validates a new SR configuration
 * 
 * @author Danail Branekov
 * 
 */
public class ServicesRegistrySystemValidator implements ICreatedObjectValidator<IServicesRegistrySystem>
{
	private final ServicesRegistrySystemComparator servicesRegistrySystemComparator = new ServicesRegistrySystemComparator();
	
	@Override
	public IStatus validateCreatedObject(final IServicesRegistrySystem servicesRegistrySystem, final Set<IServicesRegistrySystem> existingConfigs)
	{
		final IStatus singleConfigStatus = validateConfig(servicesRegistrySystem);
		final IStatus existingConfigsStatus = validateConfigAgainstExisting(servicesRegistrySystem, existingConfigs);

		return StatusUtils.getMostSevere(new IStatus[] { singleConfigStatus, existingConfigsStatus });
	}

	private IStatus validateConfig(final IServicesRegistrySystem system)
	{
		if (system.host().length() == 0)
		{
			return StatusUtils.statusError(SrCoreMessages.CFG_NO_HOST_ERROR);
		}
		if (!isValidDomainName(system.host()))
		{
			return StatusUtils.statusError(SrCoreMessages.CFG_INVALID_HOST_ERROR);
		}

		if (!isValidPortRange(system.port()))
		{
			return StatusUtils.statusError(SrCoreMessages.CFG_INVALID_PORT_VALUE);
		}

		
		if (!system.areCredentialsStored())
		{
			return Status.OK_STATUS;
		}

		
		if (system.userName().length() == 0)
			
		{
			return StatusUtils.statusError(SrCoreMessages.CFG_NO_USER_ERROR);
		}
		
	

		return StatusUtils.statusInfo(SrCoreMessages.CFG_STORE_CREDENTIALS_INFO);
	}

	private IStatus validateConfigAgainstExisting(final IServicesRegistrySystem system, final Set<IServicesRegistrySystem> existingConfigs)
	{
		for (IServicesRegistrySystem cfg : existingConfigs)
		{
			if (areConfigsDuplicate(system, cfg))
			{
				return StatusUtils.statusError(SrCoreMessages.CFG_ALREADY_EXISTS);
			}
		}

		return Status.OK_STATUS;
	}

	/**
	 * Two configurations are considered to be duplicate in case their host name (with case ignored) and port number are equal 
	 */
	private boolean areConfigsDuplicate(final IServicesRegistrySystem config1, final IServicesRegistrySystem config2)
	{
		return servicesRegistrySystemComparator.compare(config1, config2)==0;
	}

	private boolean isValidDomainName(String domainName)
	{
		String ipRule = "[0-9]+[.][0-9]+[.][0-9]+[.][0-9]+"; //$NON-NLS-1$
		String label = "[a-zA-Z][a-zA-Z0-9]*([-][a-zA-Z][a-zA-Z0-9]*)*"; //$NON-NLS-1$
		String domainNameRule = label + "([.]" + label + ")*"; //$NON-NLS-1$ //$NON-NLS-2$

		if (domainName.length() > 63)
		{
			return false;
		}
		if (domainName.matches(ipRule))
		{
			return validateIP(domainName);
		}
		return domainName.matches(domainNameRule);
	}

	private boolean validateIP(String ip)
	{
		ip = ip.replace(".", "a"); //$NON-NLS-1$ //$NON-NLS-2$
		String[] parts = ip.split("a"); //$NON-NLS-1$

		if (parts.length != 4)
			return false;

		for (int i = 0; i <= parts.length - 1; i++)
			if (Integer.parseInt(parts[i]) >= 256)
				return false;

		return true;
	}

	private boolean isValidPortRange(final int port)
	{
		// A valid port value is between 0 and 65535
		return port > 0 && port < 0xFFFF;
	}

}
