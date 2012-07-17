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
package org.eclipse.servicesregistry.core.config.persistency;

import java.util.Set;

import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;

/**
 * Interface for storing services registry server configurations in the preference store
 * 
 * @author Danail Branekov
 * @see IServicesRegistrySystem
 */
public interface IConfigStorage
{
	/**
	 * Reads the services registry server configurations from the preference store
	 * 
	 * @return a set of services registry server configurations
	 */
	public Set<IServicesRegistrySystem> readConfigurations() throws ConfigLoadException;

	/**
	 * Stores the specified set of services registry server configurations
	 * 
	 * @param configurations
	 *            the configurations to store
	 * @param handler 
	 * 			  the handler for credentials collisions 
	 * @throws ConfigStoreException
	 *             when storing configurations fails
	 * @throws ConfigLoadException
	 */
	public void storeConfigurations(Set<IServicesRegistrySystem> configurations, final IUserCredentialsHandler handler) throws ConfigStoreException, ConfigLoadException;

	/**
	 * Resets the SR preferences
	 * 
	 * @throws ConfigStoreException
	 * @throws ConfigLoadException
	 */
	public void resetConfigurations() throws ConfigStoreException, ConfigLoadException;

	/**
	 * Register a services registry server configurations change handler. It will be notified whenever there is a change in the server configuration
	 * 
	 * @param handler
	 *            the change handler
	 */
	public void registerServerConfigChangeHandler(final IConfigChangeHandler handler);
	
	/**
	 * Unregisters the services registry server configuration change handler specified
	 * @param handler
	 */
	public void unregisterServerConfigChangeHandler(final IConfigChangeHandler handler);
}
