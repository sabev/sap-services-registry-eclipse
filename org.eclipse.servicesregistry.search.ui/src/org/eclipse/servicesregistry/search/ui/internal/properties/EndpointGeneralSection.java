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
package org.eclipse.servicesregistry.search.ui.internal.properties;

import org.eclipse.servicesregistry.search.core.model.IServiceEndpoint;
import org.eclipse.servicesregistry.search.ui.internal.text.SearchUIMessages;

public class EndpointGeneralSection extends GenericPropertiesSection<IServiceEndpoint>
{
	@Override
	protected void recreateProperties()
	{
		createTextProperty(SearchUIMessages.EndpointName, getInput().getEndpointName(), SearchUIMessages.EndpointName);
		createLinkProperty(SearchUIMessages.EndpointAddress, getInput().getEndpointAddress());
		createTextProperty(SearchUIMessages.EndpointService, getInput().getServiceQName().toString(), SearchUIMessages.EndpointService);
		createLinkProperty(SearchUIMessages.EndpointWsdlUrl, getInput().getBindingWsdlUrl());
		createTextProperty(SearchUIMessages.EndpointBinding, getInput().getBindingQName().toString(), SearchUIMessages.EndpointBinding);
	}
}
