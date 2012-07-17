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

/**
 * Utilities for dealing with URLs
 * 
 * @author Danail Branekov
 */
public interface IURLUtils
{
	/**
	 * Resolves a string to URL which complies to RFC2396 (URI) section 2.1 rules (http://www.ietf.org/rfc/rfc2396.txt)<br>
	 * This method can also deal with ordinary file locations e.g. <i>C:\dir1\dir2</i> or <i>C:/dir1/dir2</i> 
	 * @param spec
	 *            the string to be resolved to URL; must not be null
	 * @return URL which complies to RFC2396 section 2.1 rules
	 * @throws MalformedURLException if the string <code>urlAsString</code> specifies an unknown protocol
	 * @throws URISyntaxException if the string specified represents URL with protocol other than "file" and is not formatted strictly according to RFC2396 and cannot be converted to a URI
	 * @throws IllegalArgumentException - If spec neither has a protocol, nor represents an absolute pathname 
	 * 
	 * @see URI#toURL()
	 * @see File#toURI()
	 * @see URL#toURI()
	 */
	public URL resolveStringToUriCompliantURL(final String spec) throws MalformedURLException, URISyntaxException, IllegalArgumentException;
	
	/**
	 * Checks whether the URL specified complies to the RFC2396 section 2.1 naming rules
	 * @param url the URL to check
	 * @return whether the URL specified complies to the RFC2396 section 2.1 naming rules
	 * @see URL#toURI()
	 */
	public boolean isRFC2396CompliantURL(final URL url);
	
}
