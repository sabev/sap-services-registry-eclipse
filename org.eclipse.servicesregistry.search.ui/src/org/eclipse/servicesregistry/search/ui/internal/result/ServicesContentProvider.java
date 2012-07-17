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

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.platform.discovery.core.api.ISearchContext;
import org.eclipse.servicesregistry.search.core.internal.search.result.ServiceDefinitionsResult;
import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.IResolvable;
import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.OperationCancelledException;
import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;
import org.eclipse.servicesregistry.search.core.model.IServiceEndpoint;

public class ServicesContentProvider implements ITreeContentProvider
{
	private StructuredViewer viewer;

	@Override
	public void dispose()
	{
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
	{
		assert viewer instanceof StructuredViewer;
		this.viewer = (StructuredViewer) viewer;
	}

	@Override
	public Object[] getElements(Object inputElement)
	{
		if (inputElement instanceof ISearchContext)
		{
			final ServiceDefinitionsResult searchResult = (ServiceDefinitionsResult) ((ISearchContext) inputElement).searchResult();
			final Collection<IServiceDefinition> definitions = searchResult.getServiceDefinitions();
			return definitions.toArray(new IServiceDefinition[definitions.size()]);
		}

		return new Object[0];
	}

	@Override
	public Object[] getChildren(final Object parentElement)
	{
		if (isServiceDefinition(parentElement))
		{
			return getServiceDefinitionChildren((IServiceDefinition) parentElement);
		}

		return new Object[0];
	}

	private Object[] getServiceDefinitionChildren(final IServiceDefinition definition)
	{
		try
		{
			final Collection<IServiceEndpoint> endpoints = definition.getEndpoints();
			return endpoints.toArray(new IServiceEndpoint[endpoints.size()]);
		}
		catch (OperationCancelledException e)
		{
			return new Object[] { new ClickToResolveNode((IResolvable) definition, new RefreshParentNodeCallback(viewer)) };
		}
	}

	@Override
	public Object getParent(Object element)
	{
		return null;
	}

	@Override
	public boolean hasChildren(Object element)
	{
		if (isClickToResolveNode(element))
		{
			return false;
		}

		if (isUnresolvedResolvable(element))
		{
			return true;
		}

		if (isServiceDefinition(element))
		{
			return !((IServiceDefinition) element).getEndpoints().isEmpty();
		}

		return false;
	}

	private boolean isServiceDefinition(Object element)
	{
		return element instanceof IServiceDefinition;
	}

	private boolean isUnresolvedResolvable(Object element)
	{
		if (element instanceof IResolvable)
		{
			if (!((IResolvable) element).isResolved())
			{
				return true;
			}
		}
		return false;
	}

	private boolean isClickToResolveNode(final Object element)
	{
		return element instanceof ClickToResolveNode;
	}
}
