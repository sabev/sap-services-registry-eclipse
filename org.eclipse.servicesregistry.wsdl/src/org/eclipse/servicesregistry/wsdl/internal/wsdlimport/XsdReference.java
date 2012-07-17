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
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.ISchemaDefinition;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifactReference;


/**
 * A relation which represents a "xsd:import", "xsd:include" or "xsd:redefine" directive
 * @author Danail Branekov
 */
class XsdReference implements IWsdlArtifactReference
{
	private final IReferenceDirective referenceDirective;
	private final ISchemaDefinition referencedSchema;

	/**
	 * Constructor
	 * @param referencedSchema the schema referenced
	 * @param schemaDirective the directive via which the schema is imported/included/redefined
	 */
	public XsdReference(final ISchemaDefinition referencedSchema, final IReferenceDirective referenceDirective)
	{
		this.referencedSchema = referencedSchema;
		this.referenceDirective = referenceDirective;
	}

	@Override
	public String getLocation()
	{
		return this.referenceDirective.getLocation();
	}

	@Override
	public void setLocation(String location)
	{
		this.referenceDirective.setLocation(location);
	}
	
	@Override
	public ISchemaDefinition referencedArtifact()
	{
		return this.referencedSchema;
	}
}
