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

import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IReferenceDirective;
import org.eclipse.wst.wsdl.Import;

/**
 * Reference directive which represents {@link Import} WSDL objects in the WSDL model
 * @author Danail Branekov
 *
 */
class WsdlImportDirective implements IReferenceDirective
{
	private final Import wsdlImport;
	
	public WsdlImportDirective(final Import wsdlImport)
	{
		this.wsdlImport = wsdlImport;
	}
	
	@Override
	public void setLocation(String location)
	{
		wsdlImport.setLocationURI(location);
	}
	
	@Override
	public String getLocation()
	{
		return wsdlImport.getLocationURI();
	}
}
