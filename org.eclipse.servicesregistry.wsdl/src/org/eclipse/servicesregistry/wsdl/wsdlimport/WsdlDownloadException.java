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
package org.eclipse.servicesregistry.wsdl.wsdlimport;

import org.eclipse.servicesregistry.wsdl.internal.LocalizedException;

/**
 * Indicates a problem during the download of wsdl files.
 * 
 * @author Joerg Dehmel 
 */
public class WsdlDownloadException extends LocalizedException
{
	private String wsdlUrl;
	
	public static final long serialVersionUID = 0l;

	/**
	 * Creates an instance with a message and a localized message.
	 * 
	 * @param message
	 *            error message
	 * @param localizedMessage
	 *            localized message
	 */
	public WsdlDownloadException(final String message, final String localizedMessage)
	{
		super(message, localizedMessage);
	}

	/**
	 * Creates an instance with message, localized message and causing exception.
	 * 
	 * @param message
	 *            error message
	 * @param localizedMessage
	 *            localized message
	 * @param cause
	 *            causing exception
	 */
	public WsdlDownloadException(final String message, final String localizedMessage, final Throwable cause)
	{
		super(message, localizedMessage, cause);		
	}

	public void setWsdlUrl(String wsdlUrl) {
		this.wsdlUrl = wsdlUrl;
	}

	public String getWsdlUrl() {
		return wsdlUrl;
	}
}
