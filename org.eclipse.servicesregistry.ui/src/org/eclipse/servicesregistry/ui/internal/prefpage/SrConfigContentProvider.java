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
package org.eclipse.servicesregistry.ui.internal.prefpage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.core.config.persistency.IPreferencesController;

/**
 * The content provider used in the services registry configurations table
 * 
 * @author Danail Branekov
 */
public class SrConfigContentProvider implements IStructuredContentProvider
{
	private final IPreferencesController configController;

	public SrConfigContentProvider(final IPreferencesController configController)
	{
		this.configController = configController;
	}

	public Object[] getElements(Object inputElement)
	{
		final List<IServicesRegistrySystem> configs = new ArrayList<IServicesRegistrySystem>(configController.getConfigurations());
		Collections.sort(configs, new Comparator<IServicesRegistrySystem>()
		{
			public int compare(IServicesRegistrySystem o1, IServicesRegistrySystem o2)
			{
				return o1.displayName().compareTo(o2.displayName());
			}
		});
		return configs.toArray();
	}

	public void dispose()
	{
		// nothing to do
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{
		// nothing to do
	}
}
