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

/**
 * Exception thrown by {@link IExistingFilesHandler#handleExistingResources(java.util.Set)} when existing resource handling failed<br>
 * The exception is localized, i.e. overrides correctly {@link #getLocalizedMessage()}
 * @author Danail Branekov
 */
public class ExistingFilesHandlingException extends Exception
{
	private static final long serialVersionUID = 6019331337813916403L;
	private final String localizedMessage; 

	/**
	 * Constructor
	 * @param message exception message
	 * @param localizedMessage exception localized message. Must not be null
	 * @param cause cause
	 */
	public ExistingFilesHandlingException(String message, String localizedMessage, Throwable cause)
	{
		super(message, cause);
		this.localizedMessage = localizedMessage;
	}

	/**
	 * Constructor
	 * @param message exception message
	 * @param localizedMessage exception localized message. Must not be null
	 */
	public ExistingFilesHandlingException(String message, String localizedMessage)
	{
		super(message);
		this.localizedMessage = localizedMessage;
	}
	
	@Override
	public String getLocalizedMessage()
	{
		return this.localizedMessage;
	}
}
