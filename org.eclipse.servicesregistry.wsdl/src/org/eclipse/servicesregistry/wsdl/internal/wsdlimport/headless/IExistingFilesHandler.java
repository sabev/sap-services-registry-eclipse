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
package org.eclipse.servicesregistry.wsdl.internal.wsdlimport.headless;

import java.io.File;
import java.util.Set;

/**
 * Interface for handling files which already exist
 * @author Danail Branekov
 */
public interface IExistingFilesHandler
{
	/**
	 * Handle files which already exist in the workspace. Implementers should provide that upon successful handling that these files 
	 * can be overwritten via either deleting them, or making them writable
	 * @param existingFiles a set of resources which already exist in the workspace; never null and never empty set
	 * @throws ExistingFilesHandlingException when existing resource handling failed
	 */
	public void handleExistingFiles(final Set<File> existingFiles) throws ExistingFilesHandlingException;
}
