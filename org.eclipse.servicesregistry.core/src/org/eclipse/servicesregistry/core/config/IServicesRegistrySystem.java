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
package org.eclipse.servicesregistry.core.config;


/**
 * Interface describing a services registry system  configuration.
 * @author Danail Branekov
 */
public interface IServicesRegistrySystem
{
	public static final char DISPLAY_NAME_HOST_PORT_SEPARATOR = ':';

	/**
	 * The services registry system display name
	 */
	public String displayName();
	
	/**
	 * The services registry system host
	 */
	public String host();
	
	/**
	 * The services registry system port
	 */
	public int port();
	
	/**
	 * Whether HTTPS is used as communication protocol; If false, then the communication protocol is HTTP
	 */
	public boolean useHttps();
	
	/**
	 * The user name used for requesting the services registry system; can be <code>null</code> if credentials are not stored 
	 */
	public String userName();
	
	/**
	 * The password used for requesting the services registry system; can be <code>null</code>  if credentials are not stored 
	 */
	public String password();
	
	/**
	 * @return true if credentials are stored
	 */
	public boolean areCredentialsStored();
	
}
