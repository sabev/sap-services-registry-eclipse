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
package org.eclipse.servicesregistry.search.ui.internal.result.actions.installers;

import org.eclipse.platform.discovery.core.api.IContributedAction;
import org.eclipse.platform.discovery.ui.api.IResultsViewAccessor;
import org.eclipse.platform.discovery.util.api.env.IDiscoveryEnvironment;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.GetEndpointWsdlContributedAction;
import org.eclipse.servicesregistry.search.core.model.IServiceEndpoint;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.GetEndpointWsdlAction;

public class ServiceEndpointActionInstaller implements IActionInstaller<IServiceEndpoint>
{
	private final IDiscoveryEnvironment env;

	public ServiceEndpointActionInstaller(final IDiscoveryEnvironment env)
	{
		this.env = env;
	}

	@Override
	public void install(final IContributedAction action, final IServiceEndpoint serviceEndpoint, final IResultsViewAccessor viewAccessor)
	{
		if (action.getActionId().equals(GetEndpointWsdlContributedAction.ACTION_ID))
		{
			installGetEndpointAction((GetEndpointWsdlContributedAction) action, serviceEndpoint, viewAccessor);
		}
	}

	private void installGetEndpointAction(final GetEndpointWsdlContributedAction action, final IServiceEndpoint serviceEndpoint, final IResultsViewAccessor viewAccessor)
	{
		viewAccessor.getMenuManager().add(new GetEndpointWsdlAction(serviceEndpoint, action, env));
	}

}
