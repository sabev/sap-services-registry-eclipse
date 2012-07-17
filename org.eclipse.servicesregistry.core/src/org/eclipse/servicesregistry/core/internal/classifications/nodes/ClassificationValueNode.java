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

import org.eclipse.servicesregistry.core.classifications.IClassificationSystemNode;
import org.eclipse.servicesregistry.core.classifications.IClassificationValueNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNode;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystemValue;

public class ClassificationValueNode implements IClassificationValueNode
{
	private final ITreeNode parent;
	private final List<ITreeNode> children;
	private final ClassificationSystemValue value;
	private final IClassificationSystemNode classificationSystem;
	private boolean selected;
	
	public ClassificationValueNode(final ClassificationSystemValue value, final IClassificationSystemNode system, final ITreeNode parent)
	{
		this.parent = parent;
		this.parent.getChildNodes().add(this);
		
		this.value = value;
		this.classificationSystem = system;
		this.children = new LinkedList<ITreeNode>();
		this.selected = false;
	}
	
	@Override
	public ITreeNode getParentNode()
	{
		return this.parent;
	}

	@Override
	public List<ITreeNode> getChildNodes()
	{
		return this.children;
	}

	@Override
	public String getName()
	{
		return value.getName();
	}

	@Override
	public String getCode()
	{
		return value.getCode();
	}

	@Override
	public IClassificationSystemNode getClassificationSystem()
	{
		return this.classificationSystem;
	}

	@Override
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	@Override
	public boolean isSelected()
	{
		return selected;
	}
}
