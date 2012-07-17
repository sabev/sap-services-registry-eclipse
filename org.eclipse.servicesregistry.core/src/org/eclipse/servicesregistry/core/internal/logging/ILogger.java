/*******************************************************************************
 * Copyright (c) 2010, 2012 SAP AG, Walldorf.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.servicesregistry.core.internal.logging;

/**
 * Interface for logging facility
 */
public interface ILogger
{
	/**
	 * Logs a message with severity error.
	 * 
	 * @param message
	 *            message to be logged
	 */
	public void logError(final String message);

	/**
	 * Logs a message with severity error.
	 * 
	 * @param message
	 *            message to be logged
	 * @param cause
	 *            causing exception
	 */
	public void logError(final String message, final Throwable cause);

	/**
	 * Logs a message with severity warning.
	 * 
	 * @param message
	 *            message to be logged
	 */
	public void logWarn(final String message);

	/**
	 * Logs a message with severity debug.
	 * 
	 * @param message
	 *            message to be logged
	 */
	public void logDebug(final String message);

	/**
	 * Logs a message with severity debug.
	 * 
	 * @param message
	 *            message to be logged
	 * @param cause
	 *            the message cause
	 */
	public void logDebug(String message, Throwable cause);
}
