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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.servicesregistry.core.classifications.IClassificationValueNode;
import org.eclipse.servicesregistry.core.classifications.IHierarchicalClassificationValueNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNodeList;
import org.eclipse.servicesregistry.core.internal.classifications.ClassificationsTreeBuilder;
import org.eclipse.servicesregistry.core.internal.classifications.finders.IClassificationSystemsFinder;
import org.eclipse.servicesregistry.core.internal.classifications.finders.IClassificationValuesFinder;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystem;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystemValue;
import org.eclipse.servicesregistry.proxy.types.GetClassificationSystemValuesFault;
import org.eclipse.servicesregistry.proxy.types.GetClassificationSystemsGreaterThanVersionFault;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetClassificationSystemValuesFault;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ClassificationsTreeBuilderTest extends ClassificationsTestFixture
{
	@Mock
	private IClassificationSystemsFinder systemsFinder;
	@Mock
	private IClassificationValuesFinder valuesFinder;

	private ClassificationsTreeBuilder treeBuilder;

	private ClassificationSystem flatSystem;
	private ClassificationSystemValue fSystem_value1;
	private ClassificationSystemValue fSystem_value2;

	private ClassificationSystem hierarchicalSystem;
	private ClassificationSystemValue hSystem_value1;
	private ClassificationSystemValue hSystem_value2;

	@Before
	public void setUp() throws GetClassificationSystemsGreaterThanVersionFault, ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault, GetClassificationSystemValuesFault, ServicesRegistrySigetClassificationSystemValuesFault
	{
		MockitoAnnotations.initMocks(this);
		treeBuilder = new ClassificationsTreeBuilder(systemsFinder, valuesFinder);

		flatSystem = mockSystem(new QName("FLAT"), "FLAT");
		hierarchicalSystem = mockSystem(new QName("HIERARCHICAL"), "HIERARCHY");

		fSystem_value1 = value("fSystem_value1");
		fSystem_value2 = value("fSystem_value2");

		hSystem_value1 = value("hSystem_value1");
		hSystem_value2 = value("hSystem_value2");
		hSystem_value1.getChildren().add(hSystem_value2);

		Mockito.stub(systemsFinder.findClassificationSystems()).toReturn(Arrays.asList(new ClassificationSystem[] { flatSystem, hierarchicalSystem }));
		Mockito.stub(valuesFinder.findValues(Mockito.eq(flatSystem))).toReturn(Arrays.asList(new ClassificationSystemValue[] { fSystem_value1, fSystem_value2 }));
		Mockito.stub(valuesFinder.findValues(Mockito.eq(hierarchicalSystem))).toReturn(Arrays.asList(new ClassificationSystemValue[] { hSystem_value1 }));
	}

	@Test
	public void testBuildTree() throws GetClassificationSystemsGreaterThanVersionFault, ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault, GetClassificationSystemValuesFault, ServicesRegistrySigetClassificationSystemValuesFault
	{
		final ITreeNodeList tree = treeBuilder.buildTree();
		verifyRootNodes(tree);
		verifyFlatSystemChildren(findTreeNodeAmong(classificationNodeName(flatSystem.getQname()), tree.getRootNodes()), tree);
		verifyHierarchicalSystemChildren(findTreeNodeAmong(classificationNodeName(hierarchicalSystem.getQname()), tree.getRootNodes()), tree);
	}

	private void verifyRootNodes(final ITreeNodeList tree)
	{
		assertEquals("2 root node expected", 2, tree.getRootNodes().size());
		final ITreeNode flatSystemNode = findTreeNodeAmong(classificationNodeName(flatSystem.getQname()), tree.getRootNodes());
		assertNotNull("Flat system not contained in root nodes", flatSystemNode);
		assertTrue("Flat system is not considered as root node", tree.isRootNode(flatSystemNode));
		
		final ITreeNode hSystemNode = findTreeNodeAmong(classificationNodeName(hierarchicalSystem.getQname()), tree.getRootNodes());
		assertNotNull("Hierarchical system not contained in root nodes", findTreeNodeAmong(classificationNodeName(hierarchicalSystem.getQname()), tree.getRootNodes()));
		assertTrue("Hierarchical system is not considered as root node", tree.isRootNode(hSystemNode));
	}

	private void verifyFlatSystemChildren(final ITreeNode systemNode, ITreeNodeList tree)
	{
		final List<ITreeNode> children = systemNode.getChildNodes();
		for (ITreeNode child : children)
		{
			assertSame("Unexpected parent", systemNode, child.getParentNode());
		}

		assertEquals("2 children expected", 2, children.size());

		final IClassificationValueNode firstNode = (IClassificationValueNode) findTreeNodeAmong(fSystem_value1.getName(), children);
		verifyFlatValueNode(systemNode, firstNode, tree);

		final IClassificationValueNode secondNode = (IClassificationValueNode) findTreeNodeAmong(fSystem_value2.getName(), children);
		verifyFlatValueNode(systemNode, secondNode, tree);
	}

	private void verifyHierarchicalSystemChildren(final ITreeNode system, ITreeNodeList tree)
	{
		final List<ITreeNode> systemChildren = system.getChildNodes();
		assertEquals("1 child expected", 1, systemChildren.size());

		final IHierarchicalClassificationValueNode topLevelChild = (IHierarchicalClassificationValueNode) systemChildren.iterator().next();
		verifyHierarchicalValueNode(topLevelChild, system, system, null, "hSystem_value1", tree);

		final List<ITreeNode> topLevelNodeChildren = topLevelChild.getChildNodes();
		assertEquals("1 child of hierarchical value node expected", 1, topLevelNodeChildren.size());
		final IHierarchicalClassificationValueNode nestedChild = (IHierarchicalClassificationValueNode) topLevelNodeChildren.iterator().next();
		verifyHierarchicalValueNode(nestedChild, system, topLevelChild, topLevelChild, "hSystem_value2", tree);
	}

	private void verifyHierarchicalValueNode(final IHierarchicalClassificationValueNode nodeToVerify, final ITreeNode system, final ITreeNode parentNode, final IHierarchicalClassificationValueNode parentValueNode, final String expectedName, ITreeNodeList tree)
	{
		assertEquals("Unexpected child", expectedName, nodeToVerify.getName());
		assertEquals("Unexpected parent", parentNode, nodeToVerify.getParentNode());
		assertSame("Unexpected classification system", system, nodeToVerify.getClassificationSystem());
		assertEquals("Unexpected parent hierarchical value", parentValueNode, nodeToVerify.getParentValue());
		assertFalse("Value node considered as root node", tree.isRootNode(nodeToVerify));
	}

	private String classificationNodeName(final QName qname)
	{
		return qname.getLocalPart() + " {" + qname.getNamespaceURI() + "}";
	}

	private ITreeNode findTreeNodeAmong(final String name, final Collection<ITreeNode> nodes)
	{
		for (ITreeNode n : nodes)
		{
			if (n.getName().equals(name))
			{
				return n;
			}
		}

		return null;
	}

	private ClassificationSystemValue value(final String name)
	{
		final ClassificationSystemValue val = new ClassificationSystemValue();
		val.setName(name);
		return val;
	}

	private void verifyFlatValueNode(final ITreeNode systemNode, final IClassificationValueNode valueNode, ITreeNodeList tree)
	{
		assertNotNull("Flat value node not found", valueNode);
		assertSame("Unexpected parent", systemNode, valueNode.getParentNode());
		assertSame("Unexpected classification system", systemNode, valueNode.getClassificationSystem());
		assertFalse("Value node considered as root node", tree.isRootNode(valueNode));
	}
}
