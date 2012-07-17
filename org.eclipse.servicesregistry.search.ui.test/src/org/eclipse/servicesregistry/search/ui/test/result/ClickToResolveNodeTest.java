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
package org.eclipse.servicesregistry.search.ui.test.result;

import static org.junit.Assert.assertFalse;

import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.IResolvable;
import org.eclipse.servicesregistry.search.ui.internal.result.ClickToResolveNode;
import org.eclipse.servicesregistry.search.ui.internal.result.ClickToResolveNode.IPostResolveCallback;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ClickToResolveNodeTest
{
	@Mock
	private IResolvable resolvableParent;
	@Mock
	private IPostResolveCallback postResolveCallback;
	
	private ClickToResolveNode node;
	
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		
		node = new ClickToResolveNode(resolvableParent, postResolveCallback);
	}
	
	@Test
	public void testIsResolved()
	{
		assertFalse("This node must be never resolved", node.isResolved());
	}
	
	@Test
	public void testResolve()
	{
		node.resolve();
		Mockito.verify(resolvableParent, Mockito.times(1)).resolve();
		Mockito.verify(postResolveCallback, Mockito.times(1)).postResolve(Mockito.same(resolvableParent));
	}
}
