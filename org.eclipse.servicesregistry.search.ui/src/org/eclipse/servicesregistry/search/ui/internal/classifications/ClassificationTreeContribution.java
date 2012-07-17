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
package org.eclipse.servicesregistry.search.ui.internal.classifications;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.platform.discovery.runtime.api.ISearchDestination;
import org.eclipse.platform.discovery.ui.api.IAdvancedSearchParamsUiContributor;
import org.eclipse.platform.discovery.ui.api.IViewUiContext;
import org.eclipse.platform.discovery.ui.api.impl.AdvancedSearchUiContributor;
import org.eclipse.platform.discovery.util.api.env.IDiscoveryEnvironment;
import org.eclipse.platform.discovery.util.api.longop.ILongOperation;
import org.eclipse.platform.discovery.util.api.longop.LongOpCanceledException;
import org.eclipse.servicesregistry.core.ISrApi;
import org.eclipse.servicesregistry.core.SrApiFactory;
import org.eclipse.servicesregistry.core.classifications.IClassificationValueNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNodeList;
import org.eclipse.servicesregistry.search.core.destinations.IServicesRegistryDestination;
import org.eclipse.servicesregistry.search.core.internal.search.OperationInParalelThread;
import org.eclipse.servicesregistry.search.ui.internal.classifications.SrClassificationTreeFactory.IServicesRegistryTree;
import org.eclipse.servicesregistry.search.ui.internal.text.SearchUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Custom search parameters contribution to the search console. The contribution contains the classification values tree
 * 
 * @author Joerg Dehmel, Danail Branekov 
 */
public class ClassificationTreeContribution extends AdvancedSearchUiContributor implements IAdvancedSearchParamsUiContributor
{
	private static final String CLASSIFICATION_VALUES_PROPERTY_NAME = "CLASSIFICATION_VALUES_SELECTION"; //$NON-NLS-1$

	private final SrClassificationTreeFactory treeFactory;
	private IServicesRegistryTree srTree;
	private IServicesRegistryDestination searchDestination;
	private IDiscoveryEnvironment environment;
	private ITreeNodeList classificationValues;

	public ClassificationTreeContribution()
	{
		treeFactory = createTreeFactory();
	}

	protected SrClassificationTreeFactory createTreeFactory()
	{
		return new SrClassificationTreeFactory();
	}

	public void createUi(Composite parent, ISearchDestination searchDestination, FormToolkit formToolkit, IDiscoveryEnvironment environment, IViewUiContext uiContext)
	{
		final Composite containerComposite = formToolkit.createComposite(parent);
		containerComposite.setLayout(new FillLayout(SWT.VERTICAL));
		final FormData containerLayoutData = UiUtils.fillingParentFormLayoutData();
		containerComposite.setLayoutData(containerLayoutData);

		assert searchDestination instanceof IServicesRegistryDestination;
		this.searchDestination = (IServicesRegistryDestination) searchDestination;

		this.environment = environment;
		srTree = treeFactory.createServicesRegistryTree(containerComposite);
	}

	@Override
	public Map<Object, Object> getParameters()
	{
		final Map<Object, Object> paramsMap = new HashMap<Object, Object>();
		final java.util.List<IClassificationValueNode> selectedNodes = srTree.getSelectedClassificationValues();
		paramsMap.put(CLASSIFICATION_VALUES_PROPERTY_NAME, selectedNodes);
		return paramsMap;
	}

	@Override
	public void setEnabled(final boolean enable)
	{
		srTree.setReadOnly(!enable);
	}

	@Override
	public void handleVisibilityChange(boolean visible)
	{
		if (visible)
		{
			if (this.classificationValues == null)
			{
				try
				{
					classificationValues = loadClassificationValues();
					srTree.update(classificationValues);
				}
				catch (LongOpCanceledException e)
				{
					// load cancelled, nothing to do
				}
				catch (InvocationTargetException e)
				{
					environment.errorHandler().handleException(e);
				}
			}
		}
	}

	private ITreeNodeList loadClassificationValues() throws LongOpCanceledException, InvocationTargetException
	{
		final OperationInParalelThread<ITreeNodeList> paralelOperation = new OperationInParalelThread<ITreeNodeList>(loadClassificationsOperation())
		{
			@Override
			public void beginTask(IProgressMonitor monitor)
			{
				monitor.beginTask(SearchUIMessages.ClassificationValues_Loading, IProgressMonitor.UNKNOWN);
			}
			
			@Override
			public void done(IProgressMonitor monitor)
			{
				monitor.done();
			}
			
		};
		return environment.operationRunner().run(paralelOperation);
	}
	
	private ILongOperation<ITreeNodeList> loadClassificationsOperation()
	{
		return new ILongOperation<ITreeNodeList>()
		{
			public ITreeNodeList run(IProgressMonitor monitor) throws LongOpCanceledException, Exception
			{
				return srApi().getClassifications();
			}
		};
	}

	protected ISrApi srApi()
	{
		return new SrApiFactory().create(searchDestination.servicesRegistrySystem());
	}
}
