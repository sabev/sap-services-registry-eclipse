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

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;
import org.eclipse.servicesregistry.search.core.model.IServiceEndpoint;
import org.eclipse.servicesregistry.search.ui.internal.text.SearchUIMessages;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

public class ServicesLabelProvider implements ILabelProvider
{
	private final ResourceManager resourceManager;
	
	public ServicesLabelProvider()
	{
		this.resourceManager = createResourceManager();
	}
	
	@Override
	public void addListener(ILabelProviderListener listener)
	{
	}

	@Override
	public void dispose()
	{
		resourceManager.dispose();
	}

	@Override
	public boolean isLabelProperty(Object element, String property)
	{
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener)
	{
	}

	@Override
	public Image getImage(final Object element)
	{
		return resourceManager.createImage(imgDescriptorFor(element));
	}

	@Override
	public String getText(final Object element)
	{
		if(element instanceof IServiceDefinition)
		{
			return ((IServiceDefinition)element).getName();
		}
		if(element instanceof IServiceEndpoint)
		{
			return ((IServiceEndpoint)element).getEndpointName();
		}
		if(element instanceof ClickToResolveNode)
		{
			return SearchUIMessages.ClickToResolveNodeText;
		}
		return null;
	}
	
	private ResourceManager createResourceManager()
	{
		return new LocalResourceManager(JFaceResources.getResources(PlatformUI.getWorkbench().getDisplay()));
	}
	
	private URL getIcon(final String relFileName)
	{
		return getClass().getClassLoader().getResource("/icons/" + relFileName); //$NON-NLS-1$
	}
	
	private URL iconURLFor(final Object element)
	{
		if(element instanceof IServiceDefinition)
		{
			return getIcon("service-definition.gif");
		}
		if(element instanceof IServiceEndpoint)
		{
			return getIcon("service-endpoint.gif"); 
		}
		if(element instanceof ClickToResolveNode)
		{
			return getIcon("refresh_icon.gif");
		}
		return null;
	}
	
	private ImageDescriptor imgDescriptorFor(final Object element)
	{
		return ImageDescriptor.createFromURL(iconURLFor(element));
	}
	
}
