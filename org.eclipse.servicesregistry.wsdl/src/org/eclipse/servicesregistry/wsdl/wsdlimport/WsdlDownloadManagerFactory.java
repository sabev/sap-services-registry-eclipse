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

import org.eclipse.servicesregistry.wsdl.internal.wsdlimport.WsdlDownloadManager;


/**
 * Creates instances of type <code>com.sap.ide.jee.ws.common.core.wsdlimport.IWsdlDownloadManager</code>.
 * 
 * @author Joerg Dehmel 
 */
public final class WsdlDownloadManagerFactory
{
	/** Prohibit instantiation. */
	private WsdlDownloadManagerFactory()
	{
		// dc
	}

	/**
	 * Creates new download manager instances.
	 * 
	 * @return new download manager instance
	 */
	public static IWsdlDownloadManager create()
	{
		return new WsdlDownloadManager();
	}
}
