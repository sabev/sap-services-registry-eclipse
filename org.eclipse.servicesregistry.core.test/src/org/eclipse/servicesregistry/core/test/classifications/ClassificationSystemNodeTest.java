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
import static org.junit.Assert.assertNull;

import javax.xml.namespace.QName;

import org.eclipse.servicesregistry.core.internal.classifications.ClassificationSystemType;
import org.eclipse.servicesregistry.core.internal.classifications.nodes.ClassificationSystemNode;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystem;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ClassificationSystemNodeTest extends ClassificationsTestFixture
{
	private static final String SYSTEM_NS = "test";
	private static final String SYSTEM_NAME = "MySystem";
	private ClassificationSystem system;
	private ClassificationSystemNode node;
	
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		system = mockSystem(new QName(SYSTEM_NS, SYSTEM_NAME), "FLAT");
		node = new ClassificationSystemNode(system);
	}
	
	@Test
	public void testGetName()
	{
		final String expectedName = SYSTEM_NAME + " {" + SYSTEM_NS + "}";
		assertEquals("Unexpected name", expectedName, node.getName());
	}
	
	@Test
	public void testGetQName()
	{
		assertEquals("Unexpected qname", system.getQname(), node.getQname());
	}
	
	@Test
	public void testGetType()
	{
		assertEquals("Unexpected system type", ClassificationSystemType.FLAT, node.getType());
	}
	
	@Test
	public void testGetParent()
	{
		assertNull("Unexpected parent", node.getParentNode());
	}
}
