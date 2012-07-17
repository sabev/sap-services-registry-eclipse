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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.platform.discovery.util.internal.ContractChecker;

/**
 * Implementation of the {@link IURLUtils} interface
 * 
 * @author Danail Branekov
 */
public class URLUtils implements IURLUtils
{
	public URL resolveStringToUriCompliantURL(String urlAsString) throws MalformedURLException, URISyntaxException
	{
		ContractChecker.nullCheckParam(urlAsString, "urlAsString"); //$NON-NLS-1$
		try
		{
			final URL url = new URL(urlAsString);
			if (isRFC2396CompliantURL(url))
			{
				return url;
			}

			final URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
			return uri.toURL();
		} catch (MalformedURLException e)
		{
			return resolveStringToFileURL(urlAsString);
		}
	}

	/**
	 * Resolves a file location string to URL
	 * 
	 * @throws MalformedURLException
	 */
	private URL resolveStringToFileURL(final String urlAsString) 
	{
		try
		{
			final File file = new File(urlAsString);
			if(!file.isAbsolute())
			{
				throw new IllegalArgumentException(urlAsString + "is not an absolute pathspec"); //$NON-NLS-1$
			}
			return file.toURI().toURL();
		} catch(MalformedURLException ex)
		{
			//unexpected, because this is a 'file:///' uri
			throw new IllegalStateException("MalformedURL was unexpected at this point", ex); //$NON-NLS-1$
		}
		
	}

	public boolean isRFC2396CompliantURL(final URL url)
	{
		try
		{
			url.toURI();
			return true;
		} catch (URISyntaxException e)
		{
			return false;
		}
	}
}
