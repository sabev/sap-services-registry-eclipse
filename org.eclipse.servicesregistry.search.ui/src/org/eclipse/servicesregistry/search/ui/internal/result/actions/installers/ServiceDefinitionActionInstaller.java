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
import org.eclipse.servicesregistry.search.core.internal.slavectrl.DownloadWsdlContributedAction;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.OpenWsdlContributedAction;
import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.OpenWsdlInEditorAction;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.SaveWsdlInWorkspaceAction;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.config.OpenWsdlInEditorConfig;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.config.SaveWsdlConfig;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.config.OpenWsdlInEditorConfig.IWsdlUrlProvider;

public class ServiceDefinitionActionInstaller implements IActionInstaller<IServiceDefinition>
{
	private final IDiscoveryEnvironment env;

	public ServiceDefinitionActionInstaller(final IDiscoveryEnvironment environment)
	{
		this.env = environment;
	}

	@Override
	public void install(final IContributedAction action, final IServiceDefinition serviceDefinition, final IResultsViewAccessor viewAccessor)
	{
		if(action.getActionId().equals(DownloadWsdlContributedAction.DOWNLOAD_WSDL_CONTRIBUTED_ACTION))
		{
			installDownloadWsdlAction((DownloadWsdlContributedAction)action, serviceDefinition, viewAccessor);
		}
		else if(action.getActionId().equals(OpenWsdlContributedAction.OPEN_WSDL_EXPLORER_ACTION))
		{
			installOpenWsdlAction((OpenWsdlContributedAction)action, serviceDefinition, viewAccessor);
		}
		
	}

	private void installOpenWsdlAction(final OpenWsdlContributedAction contributedAction, final IServiceDefinition serviceDefinition, final IResultsViewAccessor viewAccessor)
	{
		viewAccessor.getMenuManager().add(new OpenWsdlInEditorAction(new OpenWsdlInEditorConfig(serviceDefinitionWsdlUrlProvider(serviceDefinition), env), contributedAction, env));
	}
	
	private IWsdlUrlProvider serviceDefinitionWsdlUrlProvider(final IServiceDefinition definition)
	{
		return new IWsdlUrlProvider()
		{
			@Override
			public String getWsdlUrl()
			{
				return definition.getWsdlUrl();
			}
		};
	}

	private void installDownloadWsdlAction(final DownloadWsdlContributedAction contributedAction, final IServiceDefinition serviceDefinition, final IResultsViewAccessor viewAccessor)
	{
		viewAccessor.getMenuManager().add(new SaveWsdlInWorkspaceAction(createActionConfig(serviceDefinition), contributedAction, env));
		
	}

	private SaveWsdlConfig createActionConfig(final IServiceDefinition serviceDefinition)
	{
		return new SaveWsdlConfig(serviceDefinition, env)
		{
			@Override
			public String wsdlUrl()
			{
				return serviceDefinition.getWsdlUrl();
			}
			
			@Override
			public String rootWsdlFileName()
			{
				return serviceDefinition.getPorttypeQName().getLocalPart();
			}
		};
	}
	
	
	

}
