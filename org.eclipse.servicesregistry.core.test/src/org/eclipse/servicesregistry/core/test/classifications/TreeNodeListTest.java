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
package org.eclipse.servicesregistry.core.test.classifications;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.servicesregistry.core.classifications.IClassificationSystemNode;
import org.eclipse.servicesregistry.core.classifications.IClassificationValueNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNode;
import org.eclipse.servicesregistry.core.internal.classifications.nodes.TreeNodeList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class TreeNodeListTest
{
	@Mock
	private IClassificationSystemNode classifSystem;

	@Mock
	private IClassificationValueNode parentValue;

	@Mock
	private IClassificationValueNode firstChild;

	@Mock
	private IClassificationValueNode secondChild;

	private TreeNodeList nodeList;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		Mockito.stub(classifSystem.getChildNodes()).toReturn(toNodeList(parentValue));
		Mockito.stub(parentValue.getChildNodes()).toReturn(toNodeList(firstChild, secondChild));

		Mockito.stub(parentValue.isSelected()).toReturn(true);
		Mockito.stub(firstChild.isSelected()).toReturn(true);
		Mockito.stub(secondChild.isSelected()).toReturn(false);

		nodeList = new TreeNodeList();
		nodeList.getRootNodes().add(classifSystem);
	}

	@Test
	public void testGetSelectedNodes()
	{
		final List<IClassificationValueNode> selection = nodeList.getSelectedNodes();
		assertEquals("2 nodes expected", 2, selection.size());
		assertTrue("Parent node not in selection", selection.contains(parentValue));
		assertTrue("First child node not in selection", selection.contains(firstChild));
	}

	private List<ITreeNode> toNodeList(final IClassificationValueNode... valueNodes)
	{
		final List<ITreeNode> result = new ArrayList<ITreeNode>();
		for (IClassificationValueNode node : valueNodes)
		{
			result.add(node);
		}
		return result;
	}
}
