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
package org.eclipse.servicesregistry.wsdl.wsdlimport.strategy;

import java.io.File;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;

public interface IExistingFilesProcessor 
{
	/**
	 * 
	 * @param existingFiles the existing files to process, set which is never null or empty
	 * @return a status with a localized message representing the outcome. Severity must be either ERROR, CANCEL or OK
	 */
	public IStatus process(Set<File> existingFiles); 
}
