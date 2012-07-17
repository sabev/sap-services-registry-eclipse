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
import org.eclipse.servicesregistry.core.classifications.IHierarchicalClassificationValueNode;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystemValue;

public class HierarchicalValueNode extends ClassificationValueNode implements IHierarchicalClassificationValueNode
{
	private final List<IHierarchicalClassificationValueNode> hierarchicalChildren;
	private final IHierarchicalClassificationValueNode parentHierarchicalValue;

	public HierarchicalValueNode(ClassificationSystemValue value, IClassificationSystemNode system, IHierarchicalClassificationValueNode parent)
	{
		super(value, system, parent == null ? system : parent);
		this.parentHierarchicalValue = parent;
		this.hierarchicalChildren = new LinkedList<IHierarchicalClassificationValueNode>();
	}
	
	@Override
	public List<IHierarchicalClassificationValueNode> getChildValues()
	{
		return this.hierarchicalChildren;
	}

	@Override
	public IHierarchicalClassificationValueNode getParentValue()
	{
		return parentHierarchicalValue;
	}
}
