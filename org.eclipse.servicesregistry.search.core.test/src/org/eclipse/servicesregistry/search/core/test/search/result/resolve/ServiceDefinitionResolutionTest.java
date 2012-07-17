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
package org.eclipse.servicesregistry.search.core.test.search.result.resolve;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.platform.discovery.util.api.longop.LongOpCanceledException;
import org.eclipse.platform.discovery.util.internal.longop.CurrentThreadOperationRunner;
import org.eclipse.servicesregistry.proxy.types.ServiceDefinitionInfo;
import org.eclipse.servicesregistry.search.core.internal.search.result.ServiceDefinition;
import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.ArtifactProxy.OnDemandArtifactQuery;
import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.OperationCancelledException;
import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;
import org.eclipse.servicesregistry.search.core.model.IServiceEndpoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ServiceDefinitionResolutionTest
{
	private static final String DEF_ID = "my.definition";

	@Mock
	private OnDemandArtifactQuery<IServiceDefinition> resolver;
	@Mock
	private IServiceDefinition resolvedDefinition;
	@Mock
	private IServiceEndpoint endpoint;

	private ServiceDefinition serviceDefinitionProxy;

	@Before
	public void setUp() throws LongOpCanceledException
	{
		MockitoAnnotations.initMocks(this);
		Mockito.stub(resolvedDefinition.getId()).toReturn(DEF_ID);

		final ServiceDefinitionInfo sDefInfo = new ServiceDefinitionInfo();
		sDefInfo.setKey(DEF_ID);
		serviceDefinitionProxy = new ServiceDefinition(sDefInfo, resolver, new CurrentThreadOperationRunner(new NullProgressMonitor()));
	}

	@Test
	public void testInvocationPossibleWhenUnresolved() throws LongOpCanceledException
	{
		assertEquals("Unexpected service definition ID", DEF_ID, serviceDefinitionProxy.getId());
		Mockito.verify(resolver, Mockito.never()).resolve(Mockito.any(IServiceDefinition.class), Mockito.any(IProgressMonitor.class));
	}

	@Test
	public void testInvocationCausingResolution() throws LongOpCanceledException
	{
		stubResolve();
		assertSame("Unexpected service definition endpoint", endpoint, serviceDefinitionProxy.getEndpoints().iterator().next());
	}

	@Test
	public void testMultimpleInvocationsCauseSingleResolution() throws LongOpCanceledException
	{
		stubResolve();
		serviceDefinitionProxy.getEndpoints();
		serviceDefinitionProxy.getEndpoints();
		serviceDefinitionProxy.getEndpoints();

		Mockito.verify(resolver, Mockito.times(1)).resolve(Mockito.any(IServiceDefinition.class), Mockito.any(IProgressMonitor.class));
	}

	@Test(expected = OperationCancelledException.class)
	public void testCancelResolution() throws LongOpCanceledException
	{
		stubCancelResolveFirstTime();
		serviceDefinitionProxy.getEndpoints();
	}

	@Test
	public void testResolveAfterCancellation() throws LongOpCanceledException
	{
		stubCancelResolveFirstTime();
		try
		{
			serviceDefinitionProxy.getEndpoints();
		}
		catch (OperationCancelledException e)
		{
			// expected
		}
		assertSame("Unexpected service definition endpoint", endpoint, serviceDefinitionProxy.getEndpoints().iterator().next());
	}

	private void stubResolve() throws LongOpCanceledException
	{
		Mockito.stub(resolver.resolve(Mockito.same(serviceDefinitionProxy), Mockito.isA(IProgressMonitor.class))).toAnswer(resolveAnswer());
	}

	private void stubCancelResolveFirstTime() throws LongOpCanceledException
	{
		Mockito.when(resolver.resolve(Mockito.same(serviceDefinitionProxy), Mockito.isA(IProgressMonitor.class))).thenThrow(new LongOpCanceledException()).thenAnswer(resolveAnswer());
	}

	private Answer<IServiceDefinition> resolveAnswer()
	{
		return new Answer<IServiceDefinition>()
		{
			@Override
			public IServiceDefinition answer(InvocationOnMock invocation) throws Throwable
			{
				Mockito.stub(resolvedDefinition.getEndpoints()).toReturn(Arrays.asList(new IServiceEndpoint[] { endpoint }));
				return resolvedDefinition;
			}
		};
	}
}
