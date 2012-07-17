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
package org.eclipse.servicesregistry.search.core.destinations;

import org.eclipse.platform.discovery.runtime.api.ISearchDestination;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;

/**
 * A destination describing a concrete services registry search destination
 * @author Danail Branekov
 *
 */
public interface IServicesRegistryDestination extends ISearchDestination
{
	/**
	 * Services registry system information 
	 */
	public IServicesRegistrySystem servicesRegistrySystem();
}
