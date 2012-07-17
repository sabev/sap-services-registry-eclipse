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
package org.eclipse.servicesregistry.wsdl.endpoint;

import java.net.URL;

import javax.xml.namespace.QName;

/**
 * Interface which contains input data needed for refactoring to endpoint WSDL
 * @author Danail Branekov
 */
public interface IEndpointWsdlData
{
	/**
	 * The binding WSDL URL
	 */
	URL bindingWsdlUrl();
	
	/**
	 * The QName of the wsdl:service
	 */
	QName serviceQName();
	
	/**
	 * The QName of the wsdl:portType
	 */
	QName porttypeQName();
	
	/**
	 * The QName of the wsdl:binding
	 */
	QName bindingQName();
	
	/**
	 * The endpoint name
	 */
	String endpointName();
	
	/**
	 * The endpoint address
	 */
	String endpointAddress();
}