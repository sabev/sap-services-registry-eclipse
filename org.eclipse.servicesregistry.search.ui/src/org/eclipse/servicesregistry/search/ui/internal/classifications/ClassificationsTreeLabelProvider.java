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

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.servicesregistry.core.classifications.IClassificationSystemNode;
import org.eclipse.servicesregistry.core.classifications.IClassificationValueNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNode;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

public class ClassificationsTreeLabelProvider extends LabelProvider
{
	private final ResourceManager resourceManager;
	
	public ClassificationsTreeLabelProvider()
	{
		resourceManager = createResourceManager();
	}
	
	@Override
	public String getText(Object element)
	{
		assert element instanceof ITreeNode;
		
		return ((ITreeNode)element).getName();
	}
	
	@Override
	public Image getImage(Object element)
	{
		return resourceManager.createImage(imgDescriptorFor(element));		
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		resourceManager.dispose();
	}
	
	private ResourceManager createResourceManager()
	{
		return new LocalResourceManager(JFaceResources.getResources(PlatformUI.getWorkbench().getDisplay()));
	}
	
	private URL iconURLFor(final Object element)
	{
		if(element instanceof IClassificationSystemNode)
		{
			return getIcon("classification-system.gif");
		}
		
		if(element instanceof IClassificationValueNode)
		{
			return iconUrlForClassificationValue((IClassificationValueNode)element);
		}
		
		return null;
	}
	
	private URL iconUrlForClassificationValue(final IClassificationValueNode element)
	{
		return element.isSelected() ? getIcon("selected-classification-value.gif") : getIcon("unselected-classification-value.gif");
	}

	private ImageDescriptor imgDescriptorFor(final Object element)
	{
		return ImageDescriptor.createFromURL(iconURLFor(element));
	}
	
	private URL getIcon(final String relFileName)
	{
		return getClass().getClassLoader().getResource("/icons/" + relFileName); //$NON-NLS-1$
	}
}
