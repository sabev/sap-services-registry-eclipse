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
package org.eclipse.servicesregistry.ui.internal.prefpage;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.window.Window;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.config.persistency.ConfigCreationCanceledException;
import org.eclipse.servicesregistry.core.config.persistency.ConfigLoadException;
import org.eclipse.servicesregistry.core.config.persistency.ConfigStoreException;
import org.eclipse.servicesregistry.core.config.persistency.IConfigStorage;
import org.eclipse.servicesregistry.core.config.persistency.IPreferencesController;
import org.eclipse.servicesregistry.core.config.persistency.IUserCredentialsHandler;
import org.eclipse.servicesregistry.core.internal.config.ICreatedObjectValidator;
import org.eclipse.servicesregistry.core.internal.config.ServicesRegistrySystemValidator;
import org.eclipse.servicesregistry.ui.internal.config.EditSrConfigDialog;
import org.eclipse.servicesregistry.ui.internal.config.IConfigDialog;
import org.eclipse.servicesregistry.ui.internal.config.NewSrConfigDialog;


public class SrPreferencesController implements IPreferencesController
{
	private final IConfigStorage configStorage;
	private final Set<IServicesRegistrySystem> configs;
	private final IShellAware shellAware;
	private final IUserCredentialsHandler userCredentialsHandler;

	/**
	 * Constructor
	 * @param shellAware the component which is capable of providing a parent shell for displayed UI 
	 * @param configStorage storage for SR preferences
	 * @throws ConfigLoadException 
	 */
	public SrPreferencesController(final IShellAware shellAware, final IConfigStorage configStorage, final IUserCredentialsHandler existingCredentialsHandler) throws ConfigLoadException
	{
		this.shellAware = shellAware;
		this.configStorage = configStorage;
		this.userCredentialsHandler = existingCredentialsHandler;
		configs = configStorage.readConfigurations();
	}

	@Override
	public IServicesRegistrySystem createNewConfiguration() throws ConfigCreationCanceledException
	{
		final IConfigDialog dialog = createNewConfigDialog();
		if (dialog.open() == Window.CANCEL)
		{
			throw new ConfigCreationCanceledException();
		}

		final IServicesRegistrySystem config = dialog.getConfig();
		configs.add(config);
		
		return config;
	}

	@Override
	public void deleteConfiguration(IServicesRegistrySystem config)
	{
		this.configs.remove(config);
	}
	
	@Override
	public void editConfiguration(IServicesRegistrySystem config)
	{
		final IConfigDialog dialog = createEditConfigDialog(config);
		if(dialog.open() == Window.OK)
		{
			this.configs.remove(config);
			this.configs.add(dialog.getConfig());
		}
	}

	@Override
	public Set<IServicesRegistrySystem> getConfigurations()
	{
		return configs;
	}

	@Override
	public void storeConfigurations() throws ConfigStoreException, ConfigLoadException
	{
		this.configStorage.storeConfigurations(this.configs, userCredentialsHandler);
	}

	protected IConfigDialog createNewConfigDialog()
	{
		return new NewSrConfigDialog(shellAware.getShell(), createConfigValidator(), configs);
	}

	protected IConfigDialog createEditConfigDialog(final IServicesRegistrySystem config)
	{
		return new EditSrConfigDialog(shellAware.getShell(), config, createConfigValidator(), configsCopy(config));
	}
	
	private Set<IServicesRegistrySystem> configsCopy(final IServicesRegistrySystem configToRemove)
	{
		final Set<IServicesRegistrySystem> copiedConfigs = new HashSet<IServicesRegistrySystem>();
		for(IServicesRegistrySystem cfg : configs)
		{
			if(cfg == configToRemove)
			{
				continue;
			}
			
			copiedConfigs.add(cfg);
		}
		
		return copiedConfigs;
	}
	
	protected ICreatedObjectValidator<IServicesRegistrySystem> createConfigValidator()
	{
		return new ServicesRegistrySystemValidator();
	}
}
