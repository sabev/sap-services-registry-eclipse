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

import java.io.File;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.platform.discovery.util.internal.StatusUtils;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IExistingFilesProcessor;

public class ExistingFilesHandlerToProcessorAdapter implements IExistingFilesProcessor {

	private IExistingFilesHandler handler;

	public ExistingFilesHandlerToProcessorAdapter(IExistingFilesHandler handler) {
		this.handler = handler;
	}
	
	@Override
	public IStatus process(Set<File> existingFiles) {
		try{
			handler.handleExistingFiles(existingFiles);
			return Status.OK_STATUS;
		}catch(ExistingFilesHandlingException exception) {
			return StatusUtils.statusError(exception.getLocalizedMessage(), exception);
		}
	}

}
