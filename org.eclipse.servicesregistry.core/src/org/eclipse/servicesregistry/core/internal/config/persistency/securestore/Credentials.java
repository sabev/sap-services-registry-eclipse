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
package org.eclipse.servicesregistry.core.internal.config.persistency.securestore;

public class Credentials 
{
	private String user;
	private String password;
	
	public Credentials(final String user, final String password) 
	{
		this.user = user;
		this.password = password;
	}

	public String getPassword() {
		if (password==null)
		{
			return ""; //$NON-NLS-1$
		}
		return password;
	}

	public String getUser() {
		if (user==null) 
		{
			return ""; //$NON-NLS-1$
		}
		return user;
	}

}
