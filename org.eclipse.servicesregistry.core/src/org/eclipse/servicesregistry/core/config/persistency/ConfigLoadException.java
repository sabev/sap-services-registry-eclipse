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
public class ConfigLoadException extends Exception
{
	private static final long serialVersionUID = 683757953934533481L;

	public ConfigLoadException()
	{
		super();
	}

	public ConfigLoadException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ConfigLoadException(String message)
	{
		super(message);
	}

	public ConfigLoadException(Throwable cause)
	{
		super(cause);
	}
}
