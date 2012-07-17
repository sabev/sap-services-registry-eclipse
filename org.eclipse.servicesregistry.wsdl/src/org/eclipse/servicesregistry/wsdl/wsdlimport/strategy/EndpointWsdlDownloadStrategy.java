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

import org.eclipse.servicesregistry.wsdl.endpoint.BindingNotFoundException;
import org.eclipse.servicesregistry.wsdl.endpoint.EndpointWsdlRefactorer;
import org.eclipse.servicesregistry.wsdl.endpoint.IEndpointWsdlData;
import org.eclipse.servicesregistry.wsdl.endpoint.UnsupportedBindingException;
import org.eclipse.servicesregistry.wsdl.internal.wsdlimport.WsdlWtpStrategyDefault;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;

/**
 * WSDL download strategy which would result in downloading an endpoint WSDL<br>
 * The original WSDL document is manipulated on {@link #postLoad(IWsdlWtpDescriptorContainer)} invocation
 * 
 * @author Danail Branekov
 */
public class EndpointWsdlDownloadStrategy extends WsdlWtpStrategyDefault
{
	final IEndpointWsdlData endpointData;

	/**
	 * Constructor
	 * 
	 * @param endpointData
	 *            - the data of the endpoint which should result in the downloaded WSDL
	 */
	public EndpointWsdlDownloadStrategy(final IEndpointWsdlData endpointData)
	{
		super();
		this.endpointData = endpointData;
	}

	@Override
	public void postLoad(IWsdlWtpDescriptorContainer wsdlDescriptorsContainer) throws WsdlStrategyException
	{
		super.postLoad(wsdlDescriptorsContainer);
		try
		{
			(new EndpointWsdlRefactorer()).refactorToEndpointWsdl(wsdlDescriptorsContainer, endpointData);
		} catch (BindingNotFoundException e)
		{
			throw new WsdlStrategyException(e.getMessage(), e.getLocalizedMessage(), e);
		} catch (UnsupportedBindingException e)
		{
			throw new WsdlStrategyException(e.getMessage(), e.getLocalizedMessage(), e);
		}
	}
}
