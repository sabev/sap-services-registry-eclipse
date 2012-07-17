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
package org.eclipse.servicesregistry.search.core.internal.slavectrl;

import org.eclipse.platform.discovery.util.api.longop.ILongOperationRunner;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.EndpointWsdlDownloadStrategy;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlStrategyException;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlWtpStrategy;

public class GetEndpointWsdlContributedAction extends GetWsdlContributedAction<IEndpointActionConfiguration>
{
	public static final String ACTION_ID = GetEndpointWsdlContributedAction.class.getName();

	@Override
	public Object getActionId()
	{
		return ACTION_ID;
	}

	@Override
	protected WsdlWtpStrategy getWsdlDownloadStrategy(IEndpointActionConfiguration actionConfig, final ILongOperationRunner operationRunner)
	{
		final WsdlWtpStrategy parentStrategy = super.getWsdlDownloadStrategy(actionConfig, operationRunner);
		final EndpointWsdlDownloadStrategy endpointStrategy = new EndpointWsdlDownloadStrategy(actionConfig.endpointData());

		return new DelegatingWsdlDownloadStrategy(parentStrategy)
		{
			public void postLoad(IWsdlWtpDescriptorContainer wsdlDescriptorsContainer) throws WsdlStrategyException
			{
				endpointStrategy.postLoad(wsdlDescriptorsContainer);
				super.postLoad(wsdlDescriptorsContainer);
			};
		};

	}
}
