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
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDInclude;
import org.eclipse.xsd.XSDRedefine;
import org.eclipse.xsd.XSDSchemaDirective;

/**
 * Representation of {@link XSDImport}, {@link XSDInclude} or {@link XSDRedefine} objects in the WSDL model
 * 
 * @author Danail Branekov
 */
class XsdDirective implements IReferenceDirective
{
	private final XSDSchemaDirective schemaDirective;

	/**
	 * Constructor
	 * 
	 * @param schemaDirective
	 *            the schema directive
	 * @throws IllegalArgumentException
	 *             in case the schemaDirective is not instance of {@link XSDImport} or {@link XSDInclude}
	 */
	public XsdDirective(final XSDSchemaDirective schemaDirective)
	{
		this.schemaDirective = schemaDirective;
	}
	
	@Override
	public String getLocation()
	{
		return this.schemaDirective.getSchemaLocation();
	}

	@Override
	public void setLocation(final String location)
	{
		this.schemaDirective.setSchemaLocation(location);
	}
}
