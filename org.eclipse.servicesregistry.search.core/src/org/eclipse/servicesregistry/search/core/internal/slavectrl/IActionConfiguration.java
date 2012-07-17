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
package org.eclipse.servicesregistry.search.core.internal.slavectrl;

import java.io.File;

import org.eclipse.platform.discovery.util.api.env.IErrorHandler;
import org.eclipse.platform.discovery.util.api.longop.ILongOperationRunner;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IExistingFilesProcessor;

public interface IActionConfiguration
{
	/**
	 * The URL of the WSDL to be loaded
	 */
	public String wsdlUrl();

	/**
	 * The name of the root WSDL document file which would result from calling the action
	 */
	public String rootWsdlFileName();

	/**
	 * Error handler
	 */
	public IErrorHandler errorHandler();

	/**
	 * The directory the WSDL will be stored into. <code>null</code> is NOT allowed
	 */
	public File saveDestination();

	/**
	 * The validator to use to deal with existing files. Used only if the file is to be saved
	 */
	public IExistingFilesProcessor existingFilesProcessor();

	/**
	 * Callback via which clients will be notified that the WSDL download process has been completed<br>
	 * The method will be invoked out of UI thread
	 * 
	 * @param wsdlContainer
	 *            the container for the downloaded WSDL
	 * @param operationRunner
	 *            operation runner
	 */
	public void wsdlDownloaded(final IWsdlWtpDescriptorContainer wsdlContainer, ILongOperationRunner operationRunner);
}