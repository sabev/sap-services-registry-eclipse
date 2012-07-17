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

import javax.xml.namespace.QName;

/**
 * Interface representing a service endpoint
 * @author Momchil Atanassov, Nedelcho Delchev, Danail Branekov
 *
 */
public interface IServiceEndpoint {
	
	/**
	 * The name of the endpoint, i.e. WSDL Port Name
	 */
	public String getEndpointName();

	/**
	 * The URL to the binding WSDL
	 */
	public String getBindingWsdlUrl();

	/**
	 * The service definition this endpoint belongs to
	 */
	public IServiceDefinition getServiceDefinition();

	/**
	 * The target endpoint physical address
	 */
	public String getEndpointAddress();

	/**
	 * The QName of the binding this endpoint is associated with
	 */
	public QName getBindingQName();
	
	/**
	 * The QName of the service this endpoint is associated with
	 */
	public QName getServiceQName();
}
