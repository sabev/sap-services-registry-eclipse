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

import javax.xml.namespace.QName;

import org.eclipse.servicesregistry.core.classifications.IClassificationSystemNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNode;
import org.eclipse.servicesregistry.core.internal.classifications.ClassificationSystemType;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystem;

public class ClassificationSystemNode implements IClassificationSystemNode
{
	private final ClassificationSystem classifSystem;
	private final List<ITreeNode> children;

	public ClassificationSystemNode(final ClassificationSystem classifSystem)
	{
		this.classifSystem = classifSystem;
		children = new LinkedList<ITreeNode>();
	}

	@Override
	public ITreeNode getParentNode()
	{
		return null;
	}

	@Override
	public List<ITreeNode> getChildNodes()
	{
		return children;
	}

	@Override
	public String getName()
	{
		final QName qname = getQname();
		return qname.getLocalPart() + " {" + qname.getNamespaceURI() + "}";
	}

	@Override
	public QName getQname()
	{
		return classifSystem.getQname();
	}

	@Override
	public ClassificationSystemType getType()
	{
		return ClassificationSystemType.findByType(classifSystem.getType());
	}
}