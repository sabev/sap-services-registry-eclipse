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
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifactReference;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlDefinition;


/**
 * A reference which represents a "wsdl:import" statement
 * @author Danail Branekov
 *
 */
class WsdlImportReference implements IWsdlArtifactReference
{
	private final IWsdlDefinition referencedArtifact;
	private final IReferenceDirective refDirective;

	public WsdlImportReference(final IWsdlDefinition referencedArtifact, final IReferenceDirective refDirective)
	{
		this.refDirective = refDirective;
		this.referencedArtifact = referencedArtifact;
	}
	
	@Override
	public String getLocation()
	{
		return this.refDirective.getLocation();
	}

	@Override
	public void setLocation(final String location)
	{
		this.refDirective.setLocation(location);
	}

	@Override
	public IWsdlDefinition referencedArtifact()
	{
		return this.referencedArtifact;
	}

}
