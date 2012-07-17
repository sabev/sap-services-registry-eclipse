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
package org.eclipse.servicesregistry.core;

/**
 * Exception which indicates that a service definition could not be found 
 * @author Danail Branekov
 */
public class ServiceDefinitionNotFoundException extends Exception
{
	private static final long serialVersionUID = 9042509705799533620L;
	private final String serviceDefinitionUddiKey;
	
	public ServiceDefinitionNotFoundException(final String serviceDefinitionUddiKey)
	{
		super();
		this.serviceDefinitionUddiKey = serviceDefinitionUddiKey;
	}
	
	/**
	 * Returns the service definition UDDI key which could not be found
	 */
	public String serviceDefinitionUddiKey()
	{
		return this.serviceDefinitionUddiKey;
	}
}
