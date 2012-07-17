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

import org.eclipse.servicesregistry.proxy.types.PhysicalSystemKey;

/**
 * Exception which indicates that a physical system could not be found on the services registry
 * @author Danail Branekov
 */
public class PhysicalSystemNotFoundException extends Exception
{
	private static final long serialVersionUID = -1224589896746695851L;
	private final PhysicalSystemKey systemUddiKey;
	
	public PhysicalSystemNotFoundException(final PhysicalSystemKey physicalSystemKey)
	{
		this.systemUddiKey = physicalSystemKey; 
	}
	
	/**
	 * Returns the UDDI key of the physical system which could not be found on the services registry
	 */
	public PhysicalSystemKey getSystemUddiKey()
	{
		return systemUddiKey;
	}
}
