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
package org.eclipse.servicesregistry.wsdl.internal.util;

import java.io.File;
import java.net.URL;

/**
 * Interface that defines common utilies for file manipulation
 * 
 * @author Danail Branekov
 */
public interface IFileUtils {

	/**
	 * Retrieves the name of a file without its extension
	 */
	public String getFileNameWithoutExtension(final File file);

	/**
	 * Retrieves the file extension
	 * @return the file extension or empty string if the file has no extension
	 */
	public String getFileExtension(final File file);
	
	/**
	 * Checks whether the URL specified represents a file
	 * @param url the url to check.
	 * @return true in case the URL specifies a file; false otherwise. Note the file is not required to actually exist.
	 */
	public boolean isFile(final URL url);
	
	/**
	 * Returns the system temporary directory as defined by "java.io.tmpdir" system property
	 */
	public File systemTempDir();
}
