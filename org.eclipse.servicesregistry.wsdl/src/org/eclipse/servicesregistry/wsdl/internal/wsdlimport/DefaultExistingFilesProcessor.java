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
package org.eclipse.servicesregistry.wsdl.internal.wsdlimport;

import java.io.File;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.platform.discovery.util.internal.StatusUtils;
import org.eclipse.servicesregistry.wsdl.internal.plugin.text.WsdlMessages;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IExistingFilesProcessor;

class DefaultExistingFilesProcessor implements IExistingFilesProcessor {

	@Override
	public IStatus process(Set<File> existingFiles)
	{
		return StatusUtils.statusError(getLocalizedMessage(existingFiles));
	}

	private String getLocalizedMessage(Set<File> existingFiles)
	{
		StringBuilder stringBuilder = new StringBuilder(WsdlMessages.WsdlWtpStrategyDefault_WsdlorShemaCannotBeStoredMsg);
		for(File file : existingFiles)
		{
			stringBuilder.append(file.getAbsolutePath()).append("; "); //$NON-NLS-1$
		}
		return stringBuilder.toString();
	}

}
