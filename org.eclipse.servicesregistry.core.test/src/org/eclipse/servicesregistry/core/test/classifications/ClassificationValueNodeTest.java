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
import org.eclipse.servicesregistry.core.classifications.IClassificationValueNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNode;
import org.eclipse.servicesregistry.core.internal.classifications.nodes.ClassificationValueNode;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystemValue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ClassificationValueNodeTest
{
	private static final String VALUE_CODE = "valcode";
	private static final String VALUE_NAME = "valname";

	@Mock
	private ITreeNode parentNode;
	@Mock
	private IClassificationSystemNode classifSystem;

	private List<ITreeNode> parentChildren;
	private ClassificationSystemValue classifSystemValue;

	private IClassificationValueNode valueNode;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		parentChildren = new ArrayList<ITreeNode>();
		Mockito.stub(parentNode.getChildNodes()).toReturn(parentChildren);

		classifSystemValue = new ClassificationSystemValue();
		classifSystemValue.setCode(VALUE_CODE);
		classifSystemValue.setName(VALUE_NAME);

		valueNode = new ClassificationValueNode(classifSystemValue, classifSystem, parentNode);
	}

	@Test
	public void testNotSelectedByDefault()
	{
		assertFalse("Node should not be selected by default", valueNode.isSelected());
	}

	@Test
	public void testSetSelected()
	{
		valueNode.setSelected(true);
		assertTrue("Node should be selected node", valueNode.isSelected());
	}

	@Test
	public void testNodeIsRegisteredAsParentChild()
	{
		assertTrue("Node is not registered as child", parentChildren.contains(valueNode));
	}

	@Test
	public void testGetCode()
	{
		assertEquals("Unexpected code", VALUE_CODE, valueNode.getCode());
	}

	@Test
	public void testGetName()
	{
		assertEquals("Unexpected code", VALUE_NAME, valueNode.getName());
	}

	@Test
	public void testGetClassificationSystem()
	{
		assertSame("Unexpected classification system", classifSystem, valueNode.getClassificationSystem());
	}
}
