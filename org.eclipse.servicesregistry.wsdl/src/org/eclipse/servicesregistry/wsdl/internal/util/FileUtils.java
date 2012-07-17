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
 * Implementation for file utilities interface
 * 
 * @author Danail Branekov
 * 
 */
public class FileUtils implements IFileUtils {
	private static IFileUtils utils = null;

	public FileUtils() {
	}

	/**
	 * The factory method
	 * 
	 * @return a FileUtils instance
	 */
	public static IFileUtils getInstance() {
		if (utils == null) {
			utils = new FileUtils();
		}

		return utils;
	}

	@Override
	public boolean isFile(final URL url)
	{
		return url.getProtocol().equalsIgnoreCase("file"); //$NON-NLS-1$
	}
	
	@Override
	public String getFileExtension(final File file)
	{
		final String name = file.getName();
		int index = name.lastIndexOf('.');
		if (index == -1)
			return ""; //$NON-NLS-1$
		if (index == (name.length() - 1))
			return ""; //$NON-NLS-1$
		return name.substring(index + 1);
	}

	@Override
	public String getFileNameWithoutExtension(final File file)
	{
		int index = file.getName().lastIndexOf('.');
		if (index > 0 && index <= file.getName().length() - 1)
		{
			return file.getName().substring(0, index);
		}
		return file.getName();
	}
	
	@Override
	public File systemTempDir()
	{
		return new File(System.getProperty("java.io.tmpdir")); //$NON-NLS-1$
	}
}
