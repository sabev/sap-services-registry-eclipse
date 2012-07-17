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
package org.eclipse.servicesregistry.core;

import java.util.Collection;
import java.util.List;

import javax.xml.ws.BindingProvider;

import org.eclipse.servicesregistry.core.classifications.IClassificationValueNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNodeList;
import org.eclipse.servicesregistry.proxy.ServicesRegistrySi;
import org.eclipse.servicesregistry.proxy.types.PhysicalSystem;
import org.eclipse.servicesregistry.proxy.types.PhysicalSystemKey;
import org.eclipse.servicesregistry.proxy.types.ServiceDefinition;
import org.eclipse.servicesregistry.proxy.types.ServiceDefinitionsList;
import org.eclipse.servicesregistry.proxy.types.ServiceEndpointResult;

/**
 * Interface for wrapping services registry API to make it more convenient for search console clients
 * 
 * @author Danail Branekov
 */
public interface ISrApi
{
	/**
	 * Wrapper for the {@link ServicesRegistrySi#findServiceDefinitions(org.eclipse.servicesregistry.proxy.types.ServiceDefinitionSearchAttributes)}
	 * method
	 * 
	 * @param keyword
	 *            - the keyword to search for
	 * @param classificationValues
	 *            a collection of selected classification values; empty list if none
	 */
	ServiceDefinitionsList findServiceDefinitions(final String keyword, final Collection<IClassificationValueNode> classificationValues) throws SrApiException;

	/**
	 * Wrapper for the {@link ServicesRegistrySi#getServiceDefinitions(List)} method
	 * 
	 * @return the {@link ServiceDefinition} specified by the <code>serviceDefUddiKey</code>
	 * @throws ServiceDefinitionNotFoundException
	 *             when a service definition with the specified UDDI key could not be found
	 */
	ServiceDefinition getServiceDefinition(final String serviceDefUddiKey) throws SrApiException, ServiceDefinitionNotFoundException;

	/**
	 * Wrapper for the {@link ServicesRegistrySi#getServiceDefinitions(List)} method
	 * 
	 * @return the a list of endpoints for the service definition with the <code>serviceDefUddiKey</code> specified
	 */
	List<ServiceEndpointResult> findServiceEndpoints(final String serviceDefUddiKey) throws SrApiException;

	/**
	 * Wrapper for the {@link ServicesRegistrySi#getPhysicalSystems(List)} method
	 * 
	 * @return the physical system specified by the <code>physicalSystemKey</code>
	 * @throws PhysicalSystemNotFoundException
	 *             when the physical system with the specified UDDI key could not be found
	 */
	PhysicalSystem getPhysicalSystem(final PhysicalSystemKey physicalSystemKey) throws SrApiException, PhysicalSystemNotFoundException;

	/**
	 * Gets the {@link BindingProvider} associated with the underlying services registry proxy. Users can use the binding context the same way they
	 * would do when using {@link ServicesRegistrySi} instance directly
	 */
	BindingProvider getProxyBindingProvider();

	/**
	 * Retrieves all classifications and their values available in the services registry system
	 */
	ITreeNodeList getClassifications() throws SrApiException;
}
