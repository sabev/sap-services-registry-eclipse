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

import java.io.File;
import java.net.URL;

import org.eclipse.servicesregistry.wsdl.internal.wsdlimport.headless.ExistingFilesHandlingDownloadStrategy;
import org.eclipse.servicesregistry.wsdl.internal.wsdlimport.headless.ExistingFilesHandlingException;
import org.eclipse.servicesregistry.wsdl.internal.wsdlimport.headless.IExistingFilesHandler;
import org.eclipse.servicesregistry.wsdl.wsdlimport.IWsdlDownloadManager;
import org.eclipse.servicesregistry.wsdl.wsdlimport.WsdlDownloadException;
import org.eclipse.servicesregistry.wsdl.wsdlimport.WsdlDownloadManagerFactory;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;

/**
 * Creates an instance of <c>IWSDLImporter</c>. The returned instance is head-less i.e. it executes without any
 * user interaction.
 * @author Hristo Sabev
 *
 */
public class HeadlessWsdlImporterFactory
{
	/**
	 * A factory method to create the head-less instance. The very wsdl importer does not show any
	 * ui, however it depends on the <c>java.net.Authenticator</c> set in the jvm to perform authentication.
	 * This means that if the <c>java.net.Authenticator</c> of the jvm shows UI to request user and pass, then
	 * the user-pass dialog or anything else shown by the <c>java.net.Authenticator</c> will be invoked by this
	 * method. The IWsdlImporter instance returned by this factory would try to create the folder passed as target
	 * folder by the <c>targetFolder</c> parameter if it doesn't exist<br>
	 * In case the importer would overwrite already existing files, it will invoke the <code>existingFilesHandler</code> callback 
	 * in order to let the caller decide how to deal with these files. Handler invocation is done in the caller thread. Keep this in mind with 
	 * respect to resource locking and showing UI.<br>
	 * In case the handler threw a {@link ExistingFilesHandlingException}, it would be wrapped in the {@link WsdlDownloadException} which is thrown by this method. 
	 * @param rootWsdlUrl - the url to the root wsdl of the wsdl graph
	 * @param targetFolder - the target folder where the wsdl graph shall be downloaded
	 * @param existingFilesHandler handler for handling files which already exist
	 * @return A head-less instance of <c>IWSDLImporter</c> interface. This method cannot return null
	 * @throws NullPointerException if <c>rootWsdlUrl</c> or <c>targetFolder</c> is null
	 */
	public IWsdlImporter createImporter(final URL rootWsdlUrl, final File targetFolder, final IExistingFilesHandler existingFilesHandler)
	{
		return new IWsdlImporter()
		{
			public IWsdlWtpDescriptorContainer download() throws WsdlDownloadException
			{
				final IWsdlDownloadManager mngr = WsdlDownloadManagerFactory.create();
				mngr.setWsdlImportToolStrategy(createExistingFilesStrategy(existingFilesHandler));
				return mngr.download(rootWsdlUrl, targetFolder);
			}
		};
	}
	
	private ExistingFilesHandlingDownloadStrategy createExistingFilesStrategy(final IExistingFilesHandler existingFilesHandler)
	{
		return new ExistingFilesHandlingDownloadStrategy(existingFilesHandler);
	}
}
