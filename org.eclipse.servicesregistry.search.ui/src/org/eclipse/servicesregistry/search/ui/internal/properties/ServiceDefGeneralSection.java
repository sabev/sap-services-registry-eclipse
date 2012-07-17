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

import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.OperationCancelledException;
import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;
import org.eclipse.servicesregistry.search.ui.internal.text.SearchUIMessages;

public class ServiceDefGeneralSection extends GenericPropertiesSection<IServiceDefinition>
{
	@Override
	protected void recreateProperties()
	{
		createTextProperty(SearchUIMessages.ServiceDefinitionId, getInput().getId(), SearchUIMessages.ServiceDefinitionId);
		createTextProperty(SearchUIMessages.ServiceDefinitionName, getInput().getName(), SearchUIMessages.ServiceDefinitionName);
		createTextProperty(SearchUIMessages.ServicePorttype, getInput().getPorttypeQName().toString(), SearchUIMessages.ServicePorttype);
		createLinkProperty(SearchUIMessages.ServiceDocumentation, getInput().getDocumentationUrl());
		createLinkProperty(SearchUIMessages.ServiceDefinitionWsdlURL, getWsdlUrl());
		createTextProperty(SearchUIMessages.ServiceDefinitionPhysicalSystem, getPhysicalSystem(), SearchUIMessages.ServiceDefinitionPhysicalSystem);
	
	}
	
	private String getPhysicalSystem()
	{
		try
		{
			return getInput().getPhysicalSystem();
		}
		catch(OperationCancelledException e)
		{
			return "Not resolved yet";
		}
	}

	private String getWsdlUrl()
	{
		try
		{
			return getInput().getWsdlUrl();
		}
		catch(OperationCancelledException e)
		{
			return "Not resolved yet";
		}
	}

}
