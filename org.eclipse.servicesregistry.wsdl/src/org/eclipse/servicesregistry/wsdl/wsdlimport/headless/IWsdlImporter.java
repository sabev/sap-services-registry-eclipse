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
package org.eclipse.servicesregistry.wsdl.wsdlimport.headless;

import org.eclipse.servicesregistry.wsdl.wsdlimport.WsdlDownloadException;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;


/**
 * Interface for a facility that could download a WSDL file and all it's referenced files.
 * @author Hristo Sabev
 *
 */
public interface IWsdlImporter
{
	/**
	 * Downloads the given WSDL file all files references by it to the given folder.
	 * @return <c>IWsdlDescriptorContainer</c> instance describing the downloaded files. The returned value cannot be null.
	 * @throws WSDLImportException - thrown if the wsdl could not be downloaded. This is exception is thrown for every reason
	 * i.e. if the root wsdl url is not accessible, or if for some reason the wsdl file could not be parsed, or if a file was
	 * already existed. A localized message could be retrieved by calling <c>WSDLImportException.getLocalizedMessage()</c>.
	 * The exact reason of the exception depends on the concrete implementation.
	 * 
	 * This interface is not intended to be implemented by clients
	 */
	IWsdlWtpDescriptorContainer download() throws WsdlDownloadException;
}
