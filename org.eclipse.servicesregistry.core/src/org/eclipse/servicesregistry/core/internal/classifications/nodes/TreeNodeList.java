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
package org.eclipse.servicesregistry.core.internal.classifications.nodes;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.servicesregistry.core.classifications.IClassificationValueNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNodeList;

public class TreeNodeList implements ITreeNodeList
{
	private final List<ITreeNode> rootNodes = new LinkedList<ITreeNode>();
	
	@Override
	public List<ITreeNode> getRootNodes()
	{
		return rootNodes;
	}

	@Override
	public boolean isRootNode(final ITreeNode node)
	{
		return rootNodes.contains(node);
	}

	@Override
	public List<IClassificationValueNode> getSelectedNodes()
	{
		return getSelectedNodes(rootNodes);
	}

	private List<IClassificationValueNode> getSelectedNodes(final List<ITreeNode> nodes)
	{
		final List<IClassificationValueNode> result = new LinkedList<IClassificationValueNode>();
		for(ITreeNode n : nodes)
		{
			if(isSelected(n))
			{
				result.add((IClassificationValueNode)n);
			}
			result.addAll(getSelectedNodes(n.getChildNodes()));
		}
		return result;
	}

	private boolean isSelected(final ITreeNode node)
	{
		if(node instanceof IClassificationValueNode)
		{
			return ((IClassificationValueNode)node).isSelected();
		}
		return false;
	}
}
