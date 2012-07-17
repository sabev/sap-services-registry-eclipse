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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.platform.discovery.core.api.ISearchContext;
import org.eclipse.servicesregistry.search.core.internal.search.result.ServiceDefinitionsResult;
import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.IResolvable;
import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.OperationCancelledException;
import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;
import org.eclipse.servicesregistry.search.core.model.IServiceEndpoint;
import org.eclipse.servicesregistry.search.ui.internal.result.ClickToResolveNode;
import org.eclipse.servicesregistry.search.ui.internal.result.ServicesContentProvider;
import org.eclipse.servicesregistry.search.ui.test.internal.IResolvableServiceDefinition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ServicesContentProviderTest
{
	private static final String SERVICE_DEFINITION_ID_1 = "first.definition";
	private static final String SERVICE_DEFINITION_ID_2 = "second.definition";
	private static final String ENDPOINT_1_NAME = "first.endpoint";
	private static final String ENDPOINT_2_NAME = "second.endpoint";

	@Mock
	private ISearchContext searchContext;
	@Mock
	private IResolvableServiceDefinition definition_1;
	@Mock
	private IResolvableServiceDefinition definition_2;

	private ServicesContentProvider provider;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		Mockito.stub(definition_1.getId()).toReturn(SERVICE_DEFINITION_ID_1);
		Mockito.stub(definition_2.getId()).toReturn(SERVICE_DEFINITION_ID_2);

		setupSearchContext();
		provider = new ServicesContentProvider();
	}

	public void testGetElementsWithUnknownObject()
	{
		assertEquals("Provider unexpectedly returned elements for unknown object", 0, provider.getElements(new Object()).length);
	}

	@Test
	public void testGetElements()
	{
		final Object[] elements = provider.getElements(searchContext);
		assertEquals("Unexpected objects count", 2, elements.length);
		assertEquals("Unexpected ID of the first definition", SERVICE_DEFINITION_ID_1, definition_1.getId());
		assertEquals("Unexpected ID of the second definition", SERVICE_DEFINITION_ID_2, definition_2.getId());
	}

	@Test
	public void testGetElementsForUnknownObject()
	{
		final Object[] elements = provider.getElements(new Object());
		assertEquals("Unexpected objects count", 0, elements.length);
	}

	@Test
	public void testGetChildrenForUnknownObject()
	{
		assertEquals("Provider unexpectedly returned elements for unknown object", 0, provider.getChildren(new Object()).length);
	}

	@Test
	public void testGetElementsForServiceWithoutEndpoints()
	{
		Mockito.stub(definition_1.getEndpoints()).toReturn(new ArrayList<IServiceEndpoint>());
		assertEquals("Provider unexpectedly returned children for service without endpoints", 0, provider.getChildren(definition_1).length);
	}

	@Test
	public void testGetElementsForServiceWithEndpoints()
	{
		stubEndpointsForDefinition(definition_1, ENDPOINT_1_NAME, ENDPOINT_2_NAME);

		final Object[] children = provider.getChildren(definition_1);
		assertEquals("Unexpected children count", 2, children.length);
		assertEquals("Unexpected endpoint", ENDPOINT_1_NAME, ((IServiceEndpoint) children[0]).getEndpointName());
		assertEquals("Unexpected endpoint", ENDPOINT_2_NAME, ((IServiceEndpoint) children[1]).getEndpointName());
	}

	@Test
	public void testHasChildrenForUnresolvedObject()
	{
		assertTrue("An unresolved object should be considered to have chidren", provider.hasChildren(mockResolvable(false)));
	}

	@Test
	public void testHasChildrenForResolvedServiceDefinitionWithoutEndpoints()
	{
		stubEndpointsForDefinition(definition_1);
		final Object resolvedDef = resolvableServiceDefinition(true, definition_1);

		assertFalse("Resolved definitions without endpoints do not have children", provider.hasChildren(resolvedDef));
	}

	@Test
	public void testHasChildrenForResolvedServiceDefinitionWithEndpoints()
	{
		stubEndpointsForDefinition(definition_1, ENDPOINT_1_NAME);
		final Object resolvedDef = resolvableServiceDefinition(true, definition_1);

		assertTrue("Resolved definitions with endpoints should have children", provider.hasChildren(resolvedDef));
	}

	@Test
	public void testHasChildernForEndpoint()
	{
		assertFalse("Endpoints do not have children", provider.hasChildren(mockEndpoint(ENDPOINT_1_NAME)));
	}
	
	@Test
	public void testHasChildrenForClickToResolveNode()
	{
		assertFalse("ClickToResolveNode is not expected to have children", provider.hasChildren(new ClickToResolveNode(null, null)));
	}
	
	@Test
	public void testCancelDefinitionResolutionOnGetChildren()
	{
		Mockito.stub(definition_1.getEndpoints()).toThrow(new OperationCancelledException());
		final Object[] children = provider.getChildren(resolvableServiceDefinition(false, definition_1));
		assertEquals("One child expected for definition which resolution has been cancelled", 1, children.length);
		assertEquals("Unexpected child", ClickToResolveNode.class, children[0].getClass());
	}
	

	private IServiceDefinition resolvableServiceDefinition(boolean resolved, IResolvableServiceDefinition def)
	{
		Mockito.stub(def.isResolved()).toReturn(resolved);
		return def;
	}

	private IResolvable mockResolvable(final boolean resolved)
	{
		final IResolvable resolvable = Mockito.mock(IResolvable.class);
		Mockito.stub(resolvable.isResolved()).toReturn(resolved);
		return resolvable;
	}

	private IServiceEndpoint mockEndpoint(final String endpointName)
	{
		final IServiceEndpoint ep = Mockito.mock(IServiceEndpoint.class);
		Mockito.stub(ep.getEndpointName()).toReturn(endpointName);
		return ep;
	}

	private void setupSearchContext()
	{
		final ServiceDefinitionsResult result = new ServiceDefinitionsResult(Arrays.asList(new IServiceDefinition[] { definition_1, definition_2 }));
		Mockito.stub(searchContext.searchResult()).toReturn(result);
	}

//	private Object resolvableServiceDefinition(final IResolvable resolvableDelegate, final IServiceDefinition definitionDelegate)
//	{
//		final InvocationHandler invHandler = new InvocationHandler()
//		{
//			@Override
//			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
//			{
//				if (method.getName().equals("isResolved"))
//				{
//					return method.invoke(resolvableDelegate, args);
//				}
//				if (method.getName().equals("resolve"))
//				{
//					return method.invoke(resolvableDelegate, args);
//				}
//				return method.invoke(definitionDelegate, args);
//			}
//		};
//
//		return Proxy.newProxyInstance(IResolvable.class.getClassLoader(), new Class[] { IResolvable.class, IServiceDefinition.class }, invHandler);
//	}

	private void stubEndpointsForDefinition(final IServiceDefinition definition, final String... epNames)
	{
		final Collection<IServiceEndpoint> endpoints = new LinkedList<IServiceEndpoint>();
		for (String ename : epNames)
		{
			endpoints.add(mockEndpoint(ename));
		}
		Mockito.stub(definition.getEndpoints()).toReturn(endpoints);
	}
}
