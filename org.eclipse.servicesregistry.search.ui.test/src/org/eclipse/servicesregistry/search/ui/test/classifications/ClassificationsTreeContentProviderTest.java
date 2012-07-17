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
package org.eclipse.servicesregistry.search.ui.test.classifications;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.servicesregistry.core.classifications.ITreeNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNodeList;
import org.eclipse.servicesregistry.search.ui.internal.classifications.ClassificationsTreeContentProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ClassificationsTreeContentProviderTest
{
	@Mock
	private ITreeNodeList nodeList;
	@Mock
	private ITreeNode rootNode;
	@Mock
	private ITreeNode childNode;
	
	private ClassificationsTreeContentProvider contentProvider;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		contentProvider = new ClassificationsTreeContentProvider();
		contentProvider.inputChanged(null, null, nodeList);
		
		Mockito.stub(rootNode.getChildNodes()).toReturn(Arrays.asList(new ITreeNode[]{childNode}));
		Mockito.stub(childNode.getChildNodes()).toReturn(new ArrayList<ITreeNode>());
		Mockito.stub(childNode.getParentNode()).toReturn(rootNode);
		
	}
	
	@Test
	public void testGetElementsNoInput()
	{
		assertEquals("No elements expected when input is null", 0, new ClassificationsTreeContentProvider().getElements(null).length);
	}
	
	@Test
	public void testGetChildrenForRootNode()
	{
		assertEquals("One child expected", 1, contentProvider.getChildren(rootNode).length);
		assertSame("Unexpected child", childNode, contentProvider.getChildren(rootNode)[0]);
	}
	
	@Test
	public void testGetChildrenForChildNode()
	{
		assertEquals("No children expected", 0, contentProvider.getChildren(childNode).length);
	}	
	
	@Test
	public void testGetParent()
	{
		assertSame("Unexepected parent", rootNode, contentProvider.getParent(childNode));
	}
	
	@Test
	public void testHasChildrenForRootNode()
	{
		assertTrue("Root is expected to have children", contentProvider.hasChildren(rootNode));
	}
	
	@Test
	public void testHasChildrenForChildNode()
	{
		assertFalse("Child is not expected to have children", contentProvider.hasChildren(childNode));
	}
}
