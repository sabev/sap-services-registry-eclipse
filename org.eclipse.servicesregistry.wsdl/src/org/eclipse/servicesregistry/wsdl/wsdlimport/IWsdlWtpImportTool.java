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

import java.io.File;
import java.io.IOException;

import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlStrategyException;

/**
* Interface, which provides all required functionality for downloading WSDLs/Schemas on the File system.
* @author Plamen Pavlov
*/
public interface IWsdlWtpImportTool
{
	/**
	* Loads root WSDL with all imported WSDLs/Schemas from wsdlLocation. If exception occurs during the downloading process, propper exception will be thrown.
	*
	* @return IWsdlWtpDescriptorContainer
	*
	* @throws WsdlStrategyException - exception thrown by the strategy during one of the callbacks: preLoad(), postLoad(); 
	* @throws IOException - if the exception occurs during the load of the WSDL/Schema from the specified URL.
	*/
	public IWsdlWtpDescriptorContainer loadWsdls() throws WsdlStrategyException, IOException;


	/**
	* Saves all loaded WSDLs/Schemas to the specified targerFolder. If exception occurs during the downloading process, proper exception will be thrown.
	* Exception could be thrown depending on the strategy, which is used during the downloading process.
	*
	* @throws WsdlStrategyException - exception thrown by the strategy during one of the callbacks: preSave(), postSave(); 
	* @throws IOException - if the exception occurs during the save on the file system of the loaded WSDL/Schema files.
	*/
	public void saveWsdls(File targetFolder) throws WsdlStrategyException, IOException;

	/**
	* Loads root WSDL with all imported WSDLs/Schemas from wsdlLocation and saves them on the Fyle system (this is combination of loadWsdls() and saveWsdls() methods). If exception occurs during the downloading process, propper exception will be thrown.
	*
	* @return IWsdlWtpDescriptorContainer
	*
	* @throws WsdlStrategyException - exception thrown by the strategy during one of the callbacks: preLoad(), postLoad(), preSave(), postSave(); 
	* @throws IOException - if the exception occurs during the load of the WSDL/Schema from the specified URL or save on the file system.
	*/
	public IWsdlWtpDescriptorContainer downloadWsdls(File targetFolder) throws WsdlStrategyException, IOException;
	
}
