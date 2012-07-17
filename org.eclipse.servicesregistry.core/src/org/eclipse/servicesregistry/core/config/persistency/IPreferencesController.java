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
 * Interface for the services registry preferences controller which bridges the preferences clients, preferences business logic and preferences storage
 * 
 * @author Danail Branekov
 */
public interface IPreferencesController
{
	/**
	 * Gets the currently available SR server configurations<br>
	 * Do note that this method will returns a "working copy" of the server configurations, i.e. the result will reflect invocations of the "create",
	 * "delete" and "edit" method of the interface
	 * 
	 * @return a set of configurations or an empty set if none available
	 * @see #createNewConfiguration()
	 * @see #editConfiguration(IServicesRegistrySystem)
	 * @see #deleteConfiguration(IServicesRegistrySystem)
	 */
	public Set<IServicesRegistrySystem> getConfigurations();

	/**
	 * Creates a new SR server configuration<br>
	 * Do note that the newly created configuration will not be persisted to the preference store without invoking
	 * {@link #storeConfigurations()}
	 * 
	 * @return the newly created SR server config
	 * @throws ConfigCreationCanceledException
	 *             when configuration creation has been canceled
	 */
	public IServicesRegistrySystem createNewConfiguration() throws ConfigCreationCanceledException;

	/**
	 * Edit the SR configuration specified<br>
	 * Do note that the changes in the configuration will not be persisted to the preference store without invoking
	 * {@link #storeConfigurations()}
	 * 
	 * @param config
	 *            the configuration to edit
	 */
	public void editConfiguration(final IServicesRegistrySystem config);

	/**
	 * Deletes the SR server configuration<br>
	 * Do note that the deleted configuration will not be persisted to the preference store without invoking {@link #storeConfigurations()}
	 * 
	 * @param config
	 *            the configuration to delete
	 */
	public void deleteConfiguration(final IServicesRegistrySystem config);

	/**
	 * Stores the Sr configurations to the preference store
	 * 
	 * @throws ConfigStoreException
	 * @throws ConfigLoadException 
	 */
	public void storeConfigurations() throws ConfigStoreException, ConfigLoadException;
}
