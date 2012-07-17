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
import java.net.URL;

import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlDefinition;
import org.eclipse.wst.wsdl.Definition;

class WsdlDefinition extends WsdlArtifact<Definition> implements IWsdlDefinition
{
	private Definition defs;
	
	public WsdlDefinition(final Definition defs, final File wsdlFile, final ARTIFACT_TYPE wsdlType, final URL wsdlUrl, final String defaultFileName)
	{
		super(wsdlFile, wsdlUrl, defaultFileName, wsdlType);
		this.defs = defs;
	}

	@Override
	public Definition getEObject()
	{
		return this.defs;
	}
}
