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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.servicesregistry.wsdl.internal.util.FileUtils;
import org.eclipse.servicesregistry.wsdl.internal.util.IFileUtils;
import org.eclipse.servicesregistry.wsdl.internal.util.IURLUtils;
import org.eclipse.servicesregistry.wsdl.internal.util.URLUtils;
import org.eclipse.servicesregistry.wsdl.internal.wsdlimport.WsdlWtpImportToolImpl;
import org.eclipse.servicesregistry.wsdl.internal.wsdlimport.WsdlWtpStrategyDefault;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlWtpStrategy;


/**
* Factory for creation of IWsdlWtpImportTool implementation instances.
* @author Plamen Pavlov
*/
public class WsdlWtpImportToolFactory
{
	/**
	* Create an instance of IWsdlWtpImportTool implementation, default Strategy will be used.
	* 
	* @param rootWsdlUrl – The WSDL used as basis for the WSDL Importer.
	*
	* @return IWsdlWtpImportTool
	*
	* @throws NullPointerException – rootWsdlUrl is <code>null</code>.
	*/
	public static IWsdlWtpImportTool createWsdlWtpImportTool(final URL rootWsdlUrl)
	{
		return createWsdlWtpImportTool(rootWsdlUrl, null);
	}

	/**
	* Create an instance of IWsdlWtpImportTool implementation.
	* 
	* @param rootWsdlUrl – The WSDL used as basis for the WSDL Importer.
	* @param strategy – Strategy, which will be used during the download process. If the paramer is <code>null</code>, default Strategy will be used.
	*
	* @return IWsdlWtpImportTool
	*
	* @throws NullPointerException – rootWsdlUrl  is <code>null</code>.
	*/
	public static IWsdlWtpImportTool createWsdlWtpImportTool(final URL rootWsdlUrl, final WsdlWtpStrategy strategy)
	{
		if(rootWsdlUrl == null)
		{
			throw new NullPointerException("rootWsdlUrl"); //$NON-NLS-1$
		}
		
		URL ourUrl = encodeIfFile(rootWsdlUrl);
		if(strategy == null)
		{
			return new WsdlWtpImportToolImpl(ourUrl);
		}
		else
		{
			return new WsdlWtpImportToolImpl(ourUrl, strategy);
		}
	}
	
	private static URL encodeIfFile(URL rootWsdlUrl) {

		String spec = rootWsdlUrl.toExternalForm();
		try {
			//we only want to encode the URL if it is a file URL, so that subsequent invocations of URL.toURI() do not
			//fail with URISyntaxException when calculating the root wsdl file name
			if(fileUtils().isFile(rootWsdlUrl)) {
				return urlUtils().resolveStringToUriCompliantURL(spec);
			}
			
			return rootWsdlUrl; 
			
		} catch (MalformedURLException e) {
			throw new IllegalStateException("Spec has file protocol but MalformedURLException was thrown:"+spec, e); //$NON-NLS-1$
		} catch (URISyntaxException e) {
			throw new IllegalStateException("Spec has file protocol but URISyntaxException was thrown:"+spec, e); //$NON-NLS-1$
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("Spec has protocol but SpecNotAbsoluteException was thrown:"+spec, e); //$NON-NLS-1$
		}
	}

	public static WsdlWtpStrategy createDefaultStrategy()
	{
		return new WsdlWtpStrategyDefault();
	}
	
	private static IFileUtils fileUtils()
	{
		return new FileUtils();
	}
	
	private static IURLUtils urlUtils()
	{
		return new URLUtils();
	}
	
}
