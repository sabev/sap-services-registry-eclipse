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


public interface IPasswordStore 
{
	/**
	 * Returns the security credentials associated with the URL and the role.
	 * The secure store is searched and if a matching credential is
	 * found, the unencrypted value is returned. If no matching password is
	 * found, the user will be prompted to enter the value, and given the option
	 * to store the value in the secure store.
	 *
	 * @param url identifies the server and protocol, may not be null.
	 * @param role identifies the access level or scenario for which permission is required.
	 * @return
	 * @throws CoreException
	 */
	public Credentials obtainCredential(String url, String role);
	
	/**
	 * Writes (or overwrites) credentials into the secure stor (the eclipse secure store).
	 * @param url identifies the server and protocol, may not be null.
	 * @param role
	 * @param credentials that values	 to be stored
	 */
	public void putCredential(String url, String role, Credentials credentials);
	
	/**
	 * Removes the credentials from the store, if found.
	 * @param url identifies the server and protocol, may not be null.
	 * @param role
	 * @return the old credentials, if found, otherwise null.
	 */
	public Credentials resetCredential(String url, String role);
	
}
