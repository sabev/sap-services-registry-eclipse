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
package org.eclipse.servicesregistry.wsdl.internal;

import org.eclipse.platform.discovery.util.internal.ContractChecker;



/**
 * This is base class for all CHECKED exceptions for the WS Tools
 * toolset. This exception correctly overrides getLocalizedMessage()
 * of Exception class and provide proper localized message.
 * To achieve that there is a limitation - it will be imposible to return localized
 * message if it haven't been passed to the constructor of exception, so there is no
 * constructor without localized message.
 * 
 * @author Mladen Tomov
 **/

public class LocalizedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private final String localizedMessage;
	
	/**
	 * Constructs exception with ability to provide localized message
	 * @param message - the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method)
	 * @param localizedMessage - the localized message (which is saved for later retrieval
     *         by the {@link #getLocalizedMessage()} method)
	 * 
	 * @throws NullPointerException  - in case localized message is null
	 * */
	public LocalizedException(String message, String localizedMessage) {
		super(message);
		ContractChecker.nullCheckParam(localizedMessage, "localizedMessage"); //$NON-NLS-1$
		this.localizedMessage = localizedMessage;
	}

	/**
	 * Constructs exception with ability to provide localized message
	 * @param message - the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method)
	 * @param localizedMessage - the localized message (which is saved for later retrieval
     *         by the {@link #getLocalizedMessage()} method)
	 * @param cause -  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).
	 * 
	 * @throws NullPointerException  - in case localized message is null
	 * */
	
	public LocalizedException(String message, String localizedMessage, Throwable cause) {
		super(message, cause);
		ContractChecker.nullCheckParam(localizedMessage, "localizedMessage"); //$NON-NLS-1$
		this.localizedMessage = localizedMessage;
	}
	
	/**
	 * Returns localized message with which the exception was constructed.*/
	@Override
	public String getLocalizedMessage() {
		return localizedMessage;
	}

}
