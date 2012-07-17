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
package org.eclipse.servicesregistry.search.ui.internal.result;

import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.platform.discovery.core.api.IContributedAction;
import org.eclipse.platform.discovery.ui.api.IMasterDiscoveryView;
import org.eclipse.platform.discovery.ui.api.IResultsViewAccessor;
import org.eclipse.platform.discovery.ui.api.ISearchConsoleCustomization;
import org.eclipse.platform.discovery.ui.api.ITooltipProvider;
import org.eclipse.servicesregistry.core.internal.logging.ILogger;
import org.eclipse.servicesregistry.core.internal.logging.Logger;
import org.eclipse.servicesregistry.search.core.internal.search.SrSearchProvider;
import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.IResolvable;
import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.OperationCancelledException;
import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;
import org.eclipse.servicesregistry.search.core.model.IServiceEndpoint;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.installers.ServiceDefinitionActionInstaller;
import org.eclipse.servicesregistry.search.ui.internal.result.actions.installers.ServiceEndpointActionInstaller;

public class ServicesRegistryViewCustomization implements ISearchConsoleCustomization
{
	private IMasterDiscoveryView masterView;

	@Override
	public ITreeContentProvider getContentProvider()
	{
		return new ServicesContentProvider();
	}

	@Override
	public ILabelProvider getLabelProvider()
	{
		return new ServicesLabelProvider();
	}

	@Override
	public ITooltipProvider getTooltipProvider()
	{
		return null;
	}

	@Override
	public void setMasterView(final IMasterDiscoveryView masterView)
	{
		this.masterView = masterView;
	}

	@Override
	public void installAction(final IContributedAction contributedAction, final IResultsViewAccessor viewAccessor)
	{
		final IStructuredSelection sel = (IStructuredSelection)viewAccessor.getTreeViewer().getSelection();
		if (sel.size() == 1) {
			installActionForSingleSelection(contributedAction, viewAccessor, sel.getFirstElement());
		}
	}

	@Override
	public IDoubleClickListener getDoubleClickListener()
	{
		return null;
	}

	@Override
	public void postResultDisplayed(IResultsViewAccessor viewAccessor)
	{
	}

	@Override
	public void selectionChanged(ISelection selection)
	{
		final Object firstElement = ((IStructuredSelection)selection).getFirstElement();
		if(isUnresolvedResolvable(firstElement))
		{
			resolve((IResolvable)firstElement);
		}
	}
	
	private void resolve(final IResolvable resolvable)
	{
		try
		{
			resolvable.resolve();
		}
		catch(OperationCancelledException e)
		{
			logger().logDebug("Resolve cancelled", e);
		}
	}
	
	private boolean isUnresolvedResolvable(final Object object)
	{
		if(!(object instanceof IResolvable))
		{
			return false;
		}
		return !((IResolvable)object).isResolved();
	}

	@Override
	public boolean acceptSearchProvider(final String searchProviderId)
	{
		return searchProviderId.equals(SrSearchProvider.SR_SEARCH_PROVIDER_ID);
	}
	
	private void installActionForSingleSelection(final IContributedAction contributedAction, final IResultsViewAccessor viewAccessor, final Object selectedElement)
	{
		if(selectedElement instanceof IServiceDefinition)
		{
			new ServiceDefinitionActionInstaller(masterView.getEnvironment()).install(contributedAction, (IServiceDefinition)selectedElement, viewAccessor);
		}
		else if(selectedElement instanceof IServiceEndpoint)
		{
			new ServiceEndpointActionInstaller(masterView.getEnvironment()).install(contributedAction, (IServiceEndpoint)selectedElement, viewAccessor);
		}
	}
	
	protected ILogger logger()
	{
		return Logger.instance();
	}
}
