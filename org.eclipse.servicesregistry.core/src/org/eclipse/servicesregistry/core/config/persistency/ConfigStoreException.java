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


/**
 * Doesn't provide localized message
 * */
public class ConfigStoreException extends Exception
{
	private static final long serialVersionUID = -2422854308888594808L;

	public ConfigStoreException()
	{
		super();
	}

	public ConfigStoreException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ConfigStoreException(String message)
	{
		super(message);
	}

	public ConfigStoreException(Throwable cause)
	{
		super(cause);
	}
}
