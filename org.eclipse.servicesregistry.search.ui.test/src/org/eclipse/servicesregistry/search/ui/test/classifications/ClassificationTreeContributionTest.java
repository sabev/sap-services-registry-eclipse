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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.platform.discovery.util.api.env.IDiscoveryEnvironment;
import org.eclipse.platform.discovery.util.internal.longop.CurrentThreadOperationRunner;
import org.eclipse.servicesregistry.core.ISrApi;
import org.eclipse.servicesregistry.core.SrApiException;
import org.eclipse.servicesregistry.core.classifications.IClassificationSystemNode;
import org.eclipse.servicesregistry.core.classifications.IClassificationValueNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNodeList;
import org.eclipse.servicesregistry.search.ui.internal.classifications.ClassificationTreeContribution;
import org.eclipse.servicesregistry.search.ui.test.classifications.pageobjects.ClassificationsTreePageObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ClassificationTreeContributionTest
{
	private static final String VALUE_NODE_NAME = "my_value";
	private static final String CLASSIFICATION_SYSTEM_NAME = "MySystem";

	@Mock
	private ISrApi srApi;
	@Mock
	private ITreeNodeList nodeList;
	@Mock
	private IClassificationSystemNode classificationSystem;
	@Mock
	private IClassificationValueNode valueNode;
	@Mock
	private IDiscoveryEnvironment environment;

	private ClassificationTreeContribution contribution;
	private ClassificationsTreePageObject pageObject;

	@Before
	public void setUp() throws SrApiException
	{
		MockitoAnnotations.initMocks(this);
		contribution = new ClassificationTreeContribution()
		{
			protected ISrApi srApi()
			{
				return srApi;
			};
		};

		valueNode = stubValueNode(VALUE_NODE_NAME);
		classificationSystem = stubClassificationSystem(CLASSIFICATION_SYSTEM_NAME, valueNode);
		Mockito.stub(nodeList.getRootNodes()).toReturn(Arrays.asList(new ITreeNode[] { classificationSystem }));

		Mockito.stub(srApi.getClassifications()).toReturn(nodeList);
		Mockito.stub(environment.operationRunner()).toReturn(new CurrentThreadOperationRunner(new NullProgressMonitor()));

		pageObject = new ClassificationsTreePageObject(contribution, environment);
	}

	@After
	public void tearDown()
	{
		pageObject.dispose();
	}

	private IClassificationSystemNode stubClassificationSystem(String classificationSystemName, final IClassificationValueNode child)
	{
		final IClassificationSystemNode system = Mockito.mock(IClassificationSystemNode.class);
		Mockito.stub(system.getName()).toReturn(classificationSystemName);
		Mockito.stub(system.getChildNodes()).toReturn(Arrays.asList(new ITreeNode[] { child }));
		return system;
	}

	private IClassificationValueNode stubValueNode(final String nodeName)
	{
		final IClassificationValueNode node = Mockito.mock(IClassificationValueNode.class);
		Mockito.stub(node.getName()).toReturn(nodeName);
		return node;
	}

	@Test
	public void testNodesVisible()
	{
		assertTrue("Classification system is not displayed", pageObject.isClassificationSystemVisible(CLASSIFICATION_SYSTEM_NAME));
		assertTrue("Classification node is not visible", pageObject.isClassificationValueVisible(CLASSIFICATION_SYSTEM_NAME, VALUE_NODE_NAME));
	}

	@Test
	public void testToggleChild()
	{
		pageObject.toggleValue(CLASSIFICATION_SYSTEM_NAME, VALUE_NODE_NAME);
		Mockito.verify(valueNode, Mockito.times(1)).setSelected(Mockito.eq(true));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetParams()
	{
		Mockito.stub(nodeList.getSelectedNodes()).toReturn(Arrays.asList(new IClassificationValueNode[] { valueNode }));
		final Map<Object, Object> params = contribution.getParameters();
		assertEquals("One key in map expected", 1, params.keySet().size());
		assertEquals("Unexpected key", "CLASSIFICATION_VALUES_SELECTION", (String) params.keySet().iterator().next());

		final List<IClassificationValueNode> selectedNodes = (List<IClassificationValueNode>) params.get(params.keySet().iterator().next());
		assertEquals("One selected node expected", 1, selectedNodes.size());
		assertSame("Unexpected selected value", valueNode, selectedNodes.iterator().next());
	}

}
