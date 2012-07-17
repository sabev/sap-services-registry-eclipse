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

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.swt.graphics.Image;

/**
 * The label provider used in the services registry configurations table
 * @author Danail Branekov
 */
public class SrConfigLabelProvider implements ITableLabelProvider
{
	private static int NAME_COLUMN_INDEX = 0;
	private static int HOST_COLUMN_INDEX = 1;
	private static int PORT_COLUMN_INDEX = 2;
	
	
	public Image getColumnImage(Object element, int columnIndex)
	{
		return null;
	}

	public String getColumnText(Object element, int columnIndex)
	{
		assert element instanceof IServicesRegistrySystem;
		
		if(columnIndex == NAME_COLUMN_INDEX)
			return ((IServicesRegistrySystem)element).displayName();
		if(columnIndex == HOST_COLUMN_INDEX)
			return ((IServicesRegistrySystem)element).host();
		if (columnIndex == PORT_COLUMN_INDEX)
			return ""+((IServicesRegistrySystem)element).port(); //$NON-NLS-1$
		else 
			throw new RuntimeException("Invalid column index"); //$NON-NLS-1$
	}

	public void addListener(ILabelProviderListener listener)
	{
	}

	public void dispose()
	{
	}

	public boolean isLabelProperty(Object element, String property)
	{
		return false;
	}

	public void removeListener(ILabelProviderListener listener)
	{
	}
}
