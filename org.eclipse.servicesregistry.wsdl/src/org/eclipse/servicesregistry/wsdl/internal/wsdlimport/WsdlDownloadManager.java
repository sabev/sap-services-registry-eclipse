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
import java.io.IOException;
import java.net.URL;

import org.eclipse.servicesregistry.wsdl.internal.plugin.text.WsdlMessages;
import org.eclipse.servicesregistry.wsdl.wsdlimport.IWsdlDownloadManager;
import org.eclipse.servicesregistry.wsdl.wsdlimport.IWsdlWtpImportTool;
import org.eclipse.servicesregistry.wsdl.wsdlimport.WsdlDownloadException;
import org.eclipse.servicesregistry.wsdl.wsdlimport.WsdlWtpImportToolFactory;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlStrategyException;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlWtpStrategy;


public class WsdlDownloadManager implements IWsdlDownloadManager
{
	private WsdlWtpStrategy _wsdlImportToolStrategy;

	public WsdlDownloadManager()
	{
		// dc
	}

	public IWsdlWtpDescriptorContainer download(final URL wsdlUrl, final File targetDirectory) throws WsdlDownloadException
	{
		if (wsdlUrl == null)
		{
			throw new NullPointerException("wsdlUrl"); //$NON-NLS-1$
		}
		if (targetDirectory == null)
		{
			throw new NullPointerException("targetDirectory"); //$NON-NLS-1$
		}
		
		try
		{
			final IWsdlWtpImportTool importTool = createImportTool(wsdlUrl, this.getWsdlImportToolStrategy());
			return importTool.downloadWsdls(targetDirectory);

		} catch (WsdlStrategyException e)
		{
			WsdlDownloadException wde = new WsdlDownloadException(e.getMessage(), e.getLocalizedMessage(), e); //this exception message is localized
			wde.setWsdlUrl(wsdlUrl.toString());
			throw wde;
		}
		 catch (IOException e)
		{
			WsdlDownloadException wde = new WsdlDownloadException("The wsdl file can not be processed",  //$NON-NLS-1$
											WsdlMessages.WsdlDownloadManager_WsdlFileCannotBeProcessedMsg, e);
			wde.setWsdlUrl(wsdlUrl.toString());
			throw wde;
		}
		
	}
	
	public final void setWsdlImportToolStrategy(final WsdlWtpStrategy strategy)
	{
		this._wsdlImportToolStrategy = strategy;
	}
	
	public WsdlWtpStrategy getWsdlImportToolStrategy()
	{
		return this._wsdlImportToolStrategy;
	}
	
	/**
	 * Creates an import tool
	 */
	protected IWsdlWtpImportTool createImportTool(final URL wsdlUrl, final WsdlWtpStrategy strategy)
	{
		if (strategy != null)
		{
			return WsdlWtpImportToolFactory.createWsdlWtpImportTool(wsdlUrl, strategy);
		}
		
		return WsdlWtpImportToolFactory.createWsdlWtpImportTool(wsdlUrl);
	}

}
