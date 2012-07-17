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

import org.eclipse.platform.discovery.util.internal.ContractChecker;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;


public abstract class WsdlWtpStrategy
{

	protected IExistingFilesProcessor existingFilesProcessor;

	public abstract boolean isSaveAllowed();
	
	public abstract void setOverwriteFiles(boolean overwriteFiles);
	
	/**
	* Functionality, which will be executed before the save process of the loaded WSDLs/Schemas.
	* 
	* @param wsdlDescriptorsContainer – Container, for all the downloaded WSDLs/Schemas, which will be saved.
	*  
	* @throws WsdlStrategyException
	*/
	public abstract void preSave(IWsdlWtpDescriptorContainer wsdlDescriptorsContainer, File targetFolder) throws WsdlStrategyException;


	/**
	* Functionality, which will be executed after the save process of the loaded WSDLs/Schemas.
	* 
	* @param wsdlDescriptorsContainer – Container, for all the downloaded WSDLs/Schemas, which will be saved.
	*  
	* @throws WsdlStrategyException
	*/
	public abstract void postSave(IWsdlWtpDescriptorContainer wsdlDescriptorsContainer) throws WsdlStrategyException;


	/**
	* Returns the SubFolder in which all loaded Schemas will be saved.
	* @return String value, which represents the SubFolder.
	*/
	public abstract String getSchemaSubFolder();
	
	
	/**
	 * Functionality, which will be executed before the load process of the WSDLs/Schemas.
	 * 
	 * @param file – Location, where all loaded WSDLs/Schemas should be stored later.
	 * 
	 * @throws WsdlStrategyException
	 */
	public abstract void preLoad() throws WsdlStrategyException;

	/**
	* Functionality, which will be executed after the load process of the WSDLs/Schemas.
	* 
	* @param wsdlDescriptorsContainer – Container, for all the loaded WSDLs/Schemas.
	*  
	* @throws WsdlStrategyException
	*/
	public abstract void postLoad(IWsdlWtpDescriptorContainer wsdlDescriptorsContainer) throws WsdlStrategyException;
	
	/**
	 * Creates a utility which would propose default file name for a WSDL artifact
	 */
	public abstract IWsdlArtifactFileNameCalculator createWsdlFileNameCalculator();
	
	
	public void setExistingFilesProcessor(IExistingFilesProcessor existingFilesProcessor) {
		ContractChecker.nullCheckParam(existingFilesProcessor, "existingFilesProcessor"); //$NON-NLS-1$
		this.existingFilesProcessor = existingFilesProcessor;
	}
}
