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

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.servicesregistry.core.classifications.ITreeNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNodeList;

public class ClassificationsTreeContentProvider implements ITreeContentProvider
{
	private ITreeNodeList treeNodes;

	@Override
	public void dispose()
	{
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{
		assert newInput instanceof ITreeNodeList;
		this.treeNodes = (ITreeNodeList)newInput;
	}

	@Override
	public Object[] getElements(Object inputElement)
	{
		if(treeNodes == null)
		{
			return new Object[0];
		}
		return treeNodes.getRootNodes().toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement)
	{
		assert parentElement instanceof ITreeNode;
		return ((ITreeNode)parentElement).getChildNodes().toArray();
	}

	@Override
	public Object getParent(Object element)
	{
		assert element instanceof ITreeNode;
		return ((ITreeNode)element).getParentNode();
	}

	@Override
	public boolean hasChildren(Object element)
	{
		assert element instanceof ITreeNode;
		return !((ITreeNode)element).getChildNodes().isEmpty();
	}
}
