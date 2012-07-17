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
package org.eclipse.servicesregistry.wsdl.wsdlimport.model;

/**
 * Directive via which a WSDL artifact is referenced. As per WSDL 1.1, the directive might be wsdl:import, xsd:include, xsd:import or xsd:redefine
 * @author Danail Branekov
 */
public interface IReferenceDirective
{
	/**
	 * Sets the directive location
	 * @param location the location as URI string
	 */
	public void setLocation(final String location);
	
	/**
	 * Gets the directive location
	 */
	public String getLocation();
}
