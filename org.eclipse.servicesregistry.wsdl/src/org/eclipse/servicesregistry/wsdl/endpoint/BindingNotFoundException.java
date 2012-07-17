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
package org.eclipse.servicesregistry.wsdl.endpoint;

import org.eclipse.servicesregistry.wsdl.internal.LocalizedException;

public class BindingNotFoundException extends LocalizedException
{
	private static final long serialVersionUID = 6807743945547002795L;
	
	public BindingNotFoundException(String message, String localizedMessage)
	{
		super(message, localizedMessage);
	}
}
