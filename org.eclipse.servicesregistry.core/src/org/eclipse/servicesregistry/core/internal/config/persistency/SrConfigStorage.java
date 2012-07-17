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
package org.eclipse.servicesregistry.core.internal.config.persistency;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.platform.discovery.util.internal.ContractChecker;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.config.persistency.IConfigChangeHandler;
import org.eclipse.servicesregistry.core.config.persistency.IConfigStorage;
import org.eclipse.servicesregistry.core.config.persistency.IUserCredentialsHandler;
import org.eclipse.servicesregistry.core.config.persistency.ConfigLoadException;
import org.eclipse.servicesregistry.core.config.persistency.ConfigStoreException;
import org.eclipse.servicesregistry.core.config.persistency.IUserCredentialsHandler.Buttons;
import org.eclipse.servicesregistry.core.internal.config.ServicesRegistrySystem;
import org.eclipse.servicesregistry.core.internal.config.persistency.securestore.Credentials;
import org.eclipse.servicesregistry.core.internal.config.persistency.securestore.IPasswordStore;

public class SrConfigStorage implements IConfigStorage
{
	public static final String SR_CONFIGS_COUNT_PROPERTY_NAME = "SR_CONFIGS_COUNT_PROPERTY_NAME"; //$NON-NLS-1$
	public static final String SR_CONFIG_DISPLAY_NAME_PROPERTY_PREFIX = "SR_CONFIG_DISPLAY_NAME_"; //$NON-NLS-1$
	public static final String SR_CONFIG_HOST_PROPERTY_PREFIX = "SR_CONFIG_HOST_"; //$NON-NLS-1$
	public static final String SR_CONFIG_PORT_PROPERTY_PREFIX = "SR_CONFIG_PORT_"; //$NON-NLS-1$
	public static final String SR_CONFIG_USEHTTPS_PROPERTY_PREFIX = "SR_CONFIG_USEHTTPS_"; //$NON-NLS-1$
	
	public static final String SR_CONFIG_STORECRED_PROPERTY_PREFIX = "SR_CONFIG_STORECRED_"; //$NON-NLS-1$
	public static final String SR_CONFIG_USERNAME_PROPERTY_PREFIX = "SR_CONFIG_USERNAME_"; //$NON-NLS-1$
	public static final String SR_CONFIG_PASSWORD_PROPERTY_PREFIX = "SR_CONFIG_PASSWORD_"; //$NON-NLS-1$
	
	public static final String SR_CONFIG_DEFAULT_CONFIG_CREATED = "SR_CONFIG_DEFAULT_CONFIG_CREATED";//$NON-NLS-1$
	
	public static final String ROLE = "org.eclipse.servicesregistry.user"; //$NON-NLS-1$
	
	private final static String SR_DEFAULT_HOST = "sr.esworkplace.sap.com";//$NON-NLS-1$
	private final static int SR_DEFAULT_PORT = 80;
	private final static String SR_DEFAULT_CONN_NAME = "SAP ES Workplace"; //$NON-NLS-1$
	
	private final IPersistentPreferenceStore preferenceStore;
	private final IPasswordStore passwordStore;
	private final Set<IConfigChangeHandler> serverConfigChangeHandlers;
	
	public SrConfigStorage(final IPersistentPreferenceStore preferenceStore, final IPasswordStore passwordStore)
	{
		this.preferenceStore = preferenceStore;
		this.passwordStore = passwordStore;
		serverConfigChangeHandlers = new HashSet<IConfigChangeHandler>();
	}

	public Set<IServicesRegistrySystem> readConfigurations() throws ConfigLoadException
	{
		final Set<IServicesRegistrySystem> result = new HashSet<IServicesRegistrySystem>();

		final int configCount = preferenceStore.getInt(SR_CONFIGS_COUNT_PROPERTY_NAME);
		for (int i = 0; i < configCount; i++)
		{
			final String displayName = preferenceStore.getString(SR_CONFIG_DISPLAY_NAME_PROPERTY_PREFIX + i);
			final String hostName = preferenceStore.getString(SR_CONFIG_HOST_PROPERTY_PREFIX + i);
			final int portNumber = preferenceStore.getInt(SR_CONFIG_PORT_PROPERTY_PREFIX + i);
			final boolean useHttps = preferenceStore.getBoolean(SR_CONFIG_USEHTTPS_PROPERTY_PREFIX + i);
			boolean storeCredentials = preferenceStore.getBoolean(SR_CONFIG_STORECRED_PROPERTY_PREFIX + i);
			String userName = null;
			String password = null;
			if(storeCredentials)
			{
				Credentials credentials = passwordStore.obtainCredential(makeUrlString(hostName, portNumber), ROLE);
				if(credentials!= null)
				{	
					userName = credentials.getUser();
					password = credentials.getPassword();
				}
				else
				{
					//if user credentials checkbox was checked but credentials was somehow lost
					storeCredentials = false;
				}
			}
			
			result.add(new ServicesRegistrySystem(displayName, hostName, portNumber, useHttps, userName, storeCredentials, password));
		}
		
		//if the default configuration is not created
		if(!preferenceStore.getBoolean(SR_CONFIG_DEFAULT_CONFIG_CREATED))
		{
			//if configuration with the same host, port, app and pkg name already exists do not add default one
			if(!resultContainsSimilarToDefaultConfiguration(result))
			{
				result.add(new ServicesRegistrySystem(SR_DEFAULT_CONN_NAME, SR_DEFAULT_HOST, SR_DEFAULT_PORT, false, null, false, null));
			}
			
		}
		
		return result;
	}

	private boolean resultContainsSimilarToDefaultConfiguration(final Set<IServicesRegistrySystem> result) 
	{
		for(IServicesRegistrySystem config : result)
		{
			if(config.host().equalsIgnoreCase(SR_DEFAULT_HOST) && config.port() == SR_DEFAULT_PORT )
			{
				return true;
			}
		}
		return false;
	}

	public void storeConfigurations(final Set<IServicesRegistrySystem> configurations, final IUserCredentialsHandler handler) throws ConfigStoreException, ConfigLoadException
	{
		resetConfigurations();
		preferenceStore.setValue(SR_CONFIGS_COUNT_PROPERTY_NAME, configurations.size());
		//the default destination shall be added always on read method. to stop adding it this
		//constant shall be set to true. then when the user actually do something to the configurations
		//the changes shall be persisted and the default destination shall be saved along with the 
		//other destinations.
		preferenceStore.setValue(SR_CONFIG_DEFAULT_CONFIG_CREATED, true);
		
		final List<IServicesRegistrySystem> configsList = new ArrayList<IServicesRegistrySystem>(configurations);
		for (int i = 0; i < configsList.size(); i++)
		{
			final IServicesRegistrySystem cfg = configsList.get(i);
			preferenceStore.setValue(SR_CONFIG_DISPLAY_NAME_PROPERTY_PREFIX + i, cfg.displayName());
			preferenceStore.setValue(SR_CONFIG_HOST_PROPERTY_PREFIX + i, cfg.host());
			preferenceStore.setValue(SR_CONFIG_PORT_PROPERTY_PREFIX + i, cfg.port());
			preferenceStore.setValue(SR_CONFIG_USEHTTPS_PROPERTY_PREFIX + i, cfg.useHttps());
			preferenceStore.setValue(SR_CONFIG_STORECRED_PROPERTY_PREFIX + i, cfg.areCredentialsStored());
			
			if(cfg.areCredentialsStored())
			{
				
				final String url = makeUrlString(cfg.host(), cfg.port());
				Buttons shouldReplaceCredentials = Buttons.OVERRIDE;
					  
				if(passwordStore.obtainCredential(url, ROLE) != null)
				{
					shouldReplaceCredentials = handler.handleExistingCredentials();
				}
					  						
				if(shouldReplaceCredentials == Buttons.OVERRIDE)
				{	
					if(cfg.userName() != null && cfg.password() != null)
					{
						final Credentials credentials = new Credentials(cfg.userName(), cfg.password());
						passwordStore.putCredential(url, ROLE, credentials);
					}
					else 
					{
						//ckeckbox was checked but no credentials found - that's why checkbox have to be unchecked
						preferenceStore.setValue(SR_CONFIG_STORECRED_PROPERTY_PREFIX + i, false);
					}
				}
				 
			}
		}

		savePreferences();
	}


	public void resetConfigurations() throws ConfigStoreException, ConfigLoadException
	{
		final List<IServicesRegistrySystem> configurations = new ArrayList<IServicesRegistrySystem>(readConfigurations());
		preferenceStore.setToDefault(SR_CONFIGS_COUNT_PROPERTY_NAME);
		
		for (int i = 0; i < configurations.size(); i++)
		{
			preferenceStore.setToDefault(SR_CONFIG_DISPLAY_NAME_PROPERTY_PREFIX + i);
			preferenceStore.setToDefault(SR_CONFIG_HOST_PROPERTY_PREFIX + i);
			preferenceStore.setToDefault(SR_CONFIG_PORT_PROPERTY_PREFIX + i);
			preferenceStore.setToDefault(SR_CONFIG_STORECRED_PROPERTY_PREFIX + i);
			preferenceStore.setToDefault(SR_CONFIG_USEHTTPS_PROPERTY_PREFIX + i);
			passwordStore.resetCredential(makeUrlString(configurations.get(i).host(), configurations.get(i).port()), ROLE);
		}

		savePreferences();
	}
	
	private String makeUrlString(final String host, final int port)
	{
		return host+":"+port; //$NON-NLS-1$
	}
	
	private void savePreferences() throws ConfigStoreException
	{
		try
		{
			preferenceStore.save();
			notifyOfConfigChange();
		} catch (IOException e)
		{
			throw new ConfigStoreException(e);
		}
	}

	public IPersistentPreferenceStore getPreferenceStore()
	{
		return this.preferenceStore;
	}


	public void registerServerConfigChangeHandler(final IConfigChangeHandler handler)
	{
		ContractChecker.nullCheckParam(handler, "handler"); //$NON-NLS-1$
		this.serverConfigChangeHandlers.add(handler);
	}
	
	private void notifyOfConfigChange()
	{
		for(IConfigChangeHandler h : this.serverConfigChangeHandlers)
		{
			h.handleConfigChange();
		}
	}

	@Override
	public void unregisterServerConfigChangeHandler(final IConfigChangeHandler handler)
	{
		ContractChecker.nullCheckParam(handler, "handler"); //$NON-NLS-1$
		this.serverConfigChangeHandlers.remove(handler);
	}
}
