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
package org.eclipse.servicesregistry.search.core.internal.search.result;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;

public class ServiceDefinitionsResult
{
	private final Collection<IServiceDefinition> serviceDefinitions;

	public ServiceDefinitionsResult(final Collection<IServiceDefinition> foundDefinitions)
	{
		serviceDefinitions = new LinkedList<IServiceDefinition>(foundDefinitions);
	}

	public Collection<IServiceDefinition> getServiceDefinitions()
	{
		return serviceDefinitions;
	}
}
