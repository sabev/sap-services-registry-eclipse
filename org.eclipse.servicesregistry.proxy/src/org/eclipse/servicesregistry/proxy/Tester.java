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
package org.eclipse.servicesregistry.proxy;

import java.net.MalformedURLException;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.eclipse.servicesregistry.proxy.types.GetVersionInfoFault;

public class Tester
{

	/**
	 * @param args
	 * @throws MalformedURLException 
	 * @throws GetVersionInfoFault 
	 */
	public static void main(String[] args) throws MalformedURLException, GetVersionInfoFault
	{
		final ServicesRegistrySiService service = new ServicesRegistrySiService();
		final ServicesRegistrySi port = service.getServicesRegistrySiPort();
		final BindingProvider bp = (BindingProvider)port;
		final Map<String, Object> reqContext = bp.getRequestContext();
		reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://10.66.186.128:50000/ServicesRegistrySiService/ServicesRegistrySiPort");
		reqContext.put(BindingProvider.USERNAME_PROPERTY, "Administrator");
		reqContext.put(BindingProvider.PASSWORD_PROPERTY, "abcd1234");
		
		System.out.println(port.getVersionInfo());

	}

}
