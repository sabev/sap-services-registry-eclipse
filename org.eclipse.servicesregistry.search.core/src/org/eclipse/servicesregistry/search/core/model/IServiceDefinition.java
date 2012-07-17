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
package org.eclipse.servicesregistry.search.core.model;

import java.util.Collection;

import javax.xml.namespace.QName;

/**
 * Interface representing a service definition
 * @author Momchil Atanassov, Nedelcho Delchev, Danail Branekov
 *
 */
public interface IServiceDefinition {
	/**
	 * The service definition ID which represents the UDDI tModel key of the ServiceDefinition
	 */
	public String getId();
	
	/**
	 * The service definition portType Qname
	 */
	public QName getPorttypeQName();
	
	/**
	 * URL to service definition documentation
	 */
	public String getDocumentationUrl();
	
	/**
	 * A (possibly empty) collection of endpoints belonging to this service definition 
	 * @return
	 */
	public Collection<IServiceEndpoint> getEndpoints();

	/**
	 * URL to the porttype WSDL
	 */
	public String getWsdlUrl();
	
	/**
	 * The physical system of the service definition
	 */
	public String getPhysicalSystem();
	
	/**
	 * The portType local name of the Service Definition
	 */
	public String getName();
}
