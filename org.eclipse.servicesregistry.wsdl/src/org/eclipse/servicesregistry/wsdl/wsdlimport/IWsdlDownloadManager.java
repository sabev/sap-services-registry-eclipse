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
import java.net.URL;

import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlWtpStrategy;


/**
 * Manager that controls the download of wsdl file to be performed as final step of the wsdl file import.
 * 
 * @author Joerg Dehmel 
 */
public interface IWsdlDownloadManager
{
	/**
	 * Downloads the specified wsdl file into the specified target directory.
	 * 
	 * @param wsdlUrl
	 *            url of the wsdl to be downloaded
	 * @param targetDirectory
	 *            download target
	 * @return detailed information about the downloaded file
	 * @throws WsdlDownloadException
	 *             if anything went wrong during the download, e.g. the remote site couldn't be connected for download.
	 */
	IWsdlWtpDescriptorContainer download(URL wsdlUrl, File targetDirectory) throws WsdlDownloadException;
	
	void setWsdlImportToolStrategy(WsdlWtpStrategy strategy);
	
	/**
	 * Gets the WSDL import strategy. Clients may find getting the download strategy useful when they want to decorate the strategy already set
	 * @return the import tool strategy or null is strategy is not set 
	 */
	public WsdlWtpStrategy getWsdlImportToolStrategy();
}
