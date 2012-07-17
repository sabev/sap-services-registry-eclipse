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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.servicesregistry.core.classifications.IClassificationSystemNode;
import org.eclipse.servicesregistry.core.classifications.IHierarchicalClassificationValueNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNode;
import org.eclipse.servicesregistry.core.internal.classifications.nodes.HierarchicalValueNode;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystemValue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class HierarchicalValueNodeTest
{
	@Mock
	private IClassificationSystemNode classifSystem;
	@Mock
	private IHierarchicalClassificationValueNode parentHierarchicalValueNode;

	private List<ITreeNode> classificationSystemChildren;
	private List<ITreeNode> parentNodeChildren;
	private ClassificationSystemValue value;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		classificationSystemChildren = new ArrayList<ITreeNode>();
		Mockito.stub(classifSystem.getChildNodes()).toReturn(classificationSystemChildren);

		parentNodeChildren = new ArrayList<ITreeNode>();
		Mockito.stub(parentHierarchicalValueNode.getChildNodes()).toReturn(parentNodeChildren);

		value = new ClassificationSystemValue();

	}

	@Test
	public void testGetParentValue()
	{
		final IHierarchicalClassificationValueNode node = new HierarchicalValueNode(value, classifSystem, parentHierarchicalValueNode);
		assertSame("Unexpected parent hierarchical value node", parentHierarchicalValueNode, node.getParentValue());
	}

	@Test
	public void testRegisterAsClassificationSystemChildWithoutParentHierarchicalNode()
	{
		final IHierarchicalClassificationValueNode node = new HierarchicalValueNode(value, classifSystem, null);
		assertTrue("Node not registered as classification system child", classificationSystemChildren.contains(node));
	}

	@Test
	public void testRegisterAsClassificationSystemChildWithParentHierarchicalNode()
	{
		final IHierarchicalClassificationValueNode node = new HierarchicalValueNode(value, classifSystem, parentHierarchicalValueNode);
		assertFalse("Node unexpectedly registered as classification system child", classificationSystemChildren.contains(node));
		assertTrue("Node not registered as parent value child", parentNodeChildren.contains(node));
	}

	@Test
	public void testGetParentNodeWithoutParentHierarchicalNode()
	{
		final IHierarchicalClassificationValueNode node = new HierarchicalValueNode(value, classifSystem, null);
		assertSame("Unexpected paerent node", classifSystem, node.getParentNode());
	}

	@Test
	public void testGetParentNodeWithParentHierarchicalNode()
	{
		final IHierarchicalClassificationValueNode node = new HierarchicalValueNode(value, classifSystem, parentHierarchicalValueNode);
		assertSame("Unexpected paerent node", parentHierarchicalValueNode, node.getParentNode());
	}

	@Test
	public void testGetChildValues()
	{
		final IHierarchicalClassificationValueNode node = new HierarchicalValueNode(value, classifSystem, null);
		assertEquals("No child values expected by default", 0, node.getChildValues().size());

		final IHierarchicalClassificationValueNode child = Mockito.mock(IHierarchicalClassificationValueNode.class);
		node.getChildValues().add(child);
		assertEquals("1 child value expected", 1, node.getChildValues().size());
		assertSame("Unexpected child value", child, node.getChildValues().iterator().next());
	}

}
