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
package org.eclipse.servicesregistry.search.core.test.search;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.platform.discovery.util.api.longop.LongOpCanceledException;
import org.eclipse.servicesregistry.core.ISrApi;
import org.eclipse.servicesregistry.core.PhysicalSystemNotFoundException;
import org.eclipse.servicesregistry.core.ServiceDefinitionNotFoundException;
import org.eclipse.servicesregistry.core.SrApiException;
import org.eclipse.servicesregistry.proxy.types.PhysicalSystem;
import org.eclipse.servicesregistry.proxy.types.PhysicalSystemKey;
import org.eclipse.servicesregistry.proxy.types.ServiceDefinition;
import org.eclipse.servicesregistry.proxy.types.ServiceDefinitionKey;
import org.eclipse.servicesregistry.proxy.types.ServiceEndpointResult;
import org.eclipse.servicesregistry.search.core.internal.search.result.ServiceDefinitionResolver;
import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.ResolveException;
import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;
import org.eclipse.servicesregistry.search.core.model.IServiceEndpoint;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ServiceDefinitionResolverTest
{
	private static final String SERVICE_DEF_ID = "test.service.definition.id";
	private static final String ENDPOINT_1_NAME = "first.endpoint";
	private static final String ENDPOINT_2_NAME = "second.endpoint";
	private static final String PHYSICAL_SYSTEM_NAME = "MySystem";
	private static final String PHYSICAL_SYSTEM_ID = "my.system.id";

	@Mock
	private ISrApi srApi;
	@Mock
	private IProgressMonitor monitor;
	@Mock
	private IServiceDefinition definitionToResolve;

	private ServiceDefinitionResolver resolver;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		Mockito.stub(definitionToResolve.getId()).toReturn(SERVICE_DEF_ID);
		resolver = new ServiceDefinitionResolver(srApi);
	}

	@Test
	public void testResolveServiceDefinition() throws SrApiException, LongOpCanceledException, PhysicalSystemNotFoundException, ServiceDefinitionNotFoundException
	{
		stubServiceDefinitionResolution();
		final IServiceDefinition resolvedDefinition = resolver.resolve(definitionToResolve, monitor);
		assertEquals("Unexpected resolved definition ID", SERVICE_DEF_ID, resolvedDefinition.getId());
		assertEquals("Unexpected service definition physical system", PHYSICAL_SYSTEM_NAME, resolvedDefinition.getPhysicalSystem());
		assertEquals("Unexpected endpoints count", 2, resolvedDefinition.getEndpoints().size());

		final Iterator<IServiceEndpoint> epIterator = resolvedDefinition.getEndpoints().iterator();
		final IServiceEndpoint firstEp = epIterator.next();
		final IServiceEndpoint secondEp = epIterator.next();
		assertEquals("Unexpected endpoint", ENDPOINT_1_NAME, firstEp.getEndpointName());
		assertEquals("Unexpected endpoint", ENDPOINT_2_NAME, secondEp.getEndpointName());
	}

	@Test(expected = ResolveException.class)
	public void testResolveFails() throws SrApiException, LongOpCanceledException, ServiceDefinitionNotFoundException
	{
		Mockito.stub(srApi.getServiceDefinition(Mockito.anyString())).toThrow(new SrApiException(null));
		resolver.resolve(definitionToResolve, monitor);
	}
	
	@Test(expected = ResolveException.class)
	public void testResolveFailsDueToServiceNotFound() throws SrApiException, LongOpCanceledException, ServiceDefinitionNotFoundException
	{
		Mockito.stub(srApi.getServiceDefinition(Mockito.anyString())).toThrow(new ServiceDefinitionNotFoundException(null));
		resolver.resolve(definitionToResolve, monitor);
	}
	
	private void stubServiceDefinitionResolution() throws SrApiException, PhysicalSystemNotFoundException, ServiceDefinitionNotFoundException
	{
		stubGetServiceDefinition();
		stubGetEndpoints();
		stubGetPhysicalSystemName();
	}

	private void stubGetPhysicalSystemName() throws SrApiException, PhysicalSystemNotFoundException
	{
		final PhysicalSystem system = new PhysicalSystem();
		system.setSystemName(PHYSICAL_SYSTEM_NAME);
		Mockito.stub(srApi.getPhysicalSystem(Mockito.argThat(physicalSystemKeyMatcher()))).toReturn(system);
	}

	private Matcher<PhysicalSystemKey> physicalSystemKeyMatcher()
	{
		return new BaseMatcher<PhysicalSystemKey>()
		{
			@Override
			public boolean matches(Object item)
			{
				return ((PhysicalSystemKey) item).getUddiKey().equals(PHYSICAL_SYSTEM_ID);
			}

			@Override
			public void describeTo(Description description)
			{
			}
		};
	}

	private void stubGetServiceDefinition() throws SrApiException, ServiceDefinitionNotFoundException
	{
		final ServiceDefinitionKey defKey = new ServiceDefinitionKey();
		defKey.setUddiKey(SERVICE_DEF_ID);

		final ServiceDefinition resolvedDefinition = new ServiceDefinition();
		resolvedDefinition.setServiceDefinitionKey(defKey);
		final PhysicalSystemKey systemKey = new PhysicalSystemKey();
		systemKey.setUddiKey(PHYSICAL_SYSTEM_ID);
		resolvedDefinition.setPhysicalSystemKey(systemKey);

		Mockito.stub(srApi.getServiceDefinition(Mockito.eq(SERVICE_DEF_ID))).toReturn(resolvedDefinition);
	}

	private void stubGetEndpoints() throws SrApiException
	{
		final ServiceEndpointResult firstEp = new ServiceEndpointResult();
		firstEp.setEndpointName(ENDPOINT_1_NAME);

		final ServiceEndpointResult secondEp = new ServiceEndpointResult();
		secondEp.setEndpointName(ENDPOINT_2_NAME);

		final List<ServiceEndpointResult> epList = new ArrayList<ServiceEndpointResult>();
		epList.add(firstEp);
		epList.add(secondEp);

		Mockito.stub(srApi.findServiceEndpoints(Mockito.eq(SERVICE_DEF_ID))).toReturn(epList);
	}
}
