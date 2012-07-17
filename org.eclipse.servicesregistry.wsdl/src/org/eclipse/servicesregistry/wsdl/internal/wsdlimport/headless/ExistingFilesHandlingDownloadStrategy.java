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

import org.eclipse.platform.discovery.util.internal.ContractChecker;
import org.eclipse.servicesregistry.wsdl.internal.wsdlimport.WsdlWtpStrategyDefault;

/**
 * WSDL download strategy which is similar to {@link WsdlWtpStrategyDefault} but handles cases when files already exist on the file system via delegating to the {@link IExistingFilesHandler} specified in the constructor
 * 
 * @author Danail Branekov
 * 
 */
public class ExistingFilesHandlingDownloadStrategy extends WsdlWtpStrategyDefault
{

	/**
	 * Constructor
	 * 
	 * @param existingFilesHandler
	 *            handler for existing files. Must not be null
	 */
	public ExistingFilesHandlingDownloadStrategy(final IExistingFilesHandler existingFilesHandler)
	{
		ContractChecker.nullCheckParam(existingFilesHandler, "existingFilesHandler"); //$NON-NLS-1$
		setExistingFilesProcessor(new ExistingFilesHandlerToProcessorAdapter(existingFilesHandler));
	}

}
