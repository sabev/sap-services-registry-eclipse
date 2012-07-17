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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import junit.framework.Assert;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.platform.discovery.runtime.api.ISearchParameters;
import org.eclipse.platform.discovery.runtime.api.SearchCancelledException;
import org.eclipse.platform.discovery.runtime.api.SearchFailedException;
import org.eclipse.platform.discovery.util.api.longop.ILongOperation;
import org.eclipse.platform.discovery.util.api.longop.ILongOperationRunner;
import org.eclipse.platform.discovery.util.api.longop.LongOpCanceledException;
import org.eclipse.servicesregistry.core.ISrApi;
import org.eclipse.servicesregistry.core.SrApiException;
import org.eclipse.servicesregistry.core.classifications.IClassificationValueNode;
import org.eclipse.servicesregistry.core.config.IServicesRegistrySystem;
import org.eclipse.servicesregistry.proxy.types.ServiceDefinitionInfo;
import org.eclipse.servicesregistry.proxy.types.ServiceDefinitionsList;
import org.eclipse.servicesregistry.search.core.destinations.IServicesRegistryDestination;
import org.eclipse.servicesregistry.search.core.internal.plugin.text.SearchCoreMessages;
import org.eclipse.servicesregistry.search.core.internal.search.FindServiceDefinitionsQuery;
import org.eclipse.servicesregistry.search.core.internal.search.SrSearchProvider;
import org.eclipse.servicesregistry.search.core.internal.search.result.ServiceDefinitionsResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class FindServiceDefinitionsQueryTest
{
	private static final String KEYWORD = "where are my services?";

	@Mock
	private ILongOperationRunner opRunner;
	@Mock
	private ISrApi srApi;
	@Mock
	private BindingProvider bindingProvider;
	@Mock
	private IProgressMonitor progressMonitor;
	@Mock
	private IServicesRegistryDestination searchDestination;
	@Mock
	private IServicesRegistrySystem srSystem;

	private boolean isCancelled;
	private Collection<IClassificationValueNode> classifications;
	
	private FindServiceDefinitionsQuery query;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		isCancelled = false;
		classifications = new ArrayList<IClassificationValueNode>();
		Mockito.stub(srApi.getProxyBindingProvider()).toReturn(bindingProvider);
		

		setupSearchDestination();
		setupQuery();
		setupOperationRunner();
		setupProgressMonitor();
	}

	@Test
	public void testPerformSearch() throws SrApiException, SearchCancelledException, SearchFailedException
	{
		final String defId = "org.eclipse.sr.test.mydefinition";
		Mockito.stub(srApi.findServiceDefinitions(Mockito.eq(KEYWORD), Mockito.same(classifications))).toReturn(newServiceDefinitionsList(defId));
		final ServiceDefinitionsResult result = (ServiceDefinitionsResult)query.execute(opRunner);
		verifyMonitorStartedAndStopped();
		
		assertEquals("Unexpected result size", 1, result.getServiceDefinitions().size());
		assertEquals("Unexcpected service defintion ID in result", defId, result.getServiceDefinitions().iterator().next().getId());
	}

	@Test
	public void testCancelSearch() throws SearchFailedException
	{
		isCancelled = true;
		try
		{
			query.execute(opRunner);
			Assert.fail("SearchCancelledException not thrown");
		}
		catch (SearchCancelledException e)
		{
			// expected
		}
		verifyMonitorStartedAndStopped();
	}

	@Test
	public void testSearchFailed() throws SrApiException
	{
		Mockito.stub(srApi.findServiceDefinitions(Mockito.eq(KEYWORD), Mockito.same(classifications))).toThrow(new SrApiException(null));
		try
		{
			query.execute(opRunner);
			Assert.fail("SearchFailedException not thrown");
		}
		catch (SearchFailedException e)
		{
			// expected
		}
		verifyMonitorStartedAndStopped();
	}
	
	@Test(expected=SearchCancelledException.class)
	public void testAuthenticationCancelled() throws SrApiException, SearchCancelledException, SearchFailedException
	{
		Mockito.stub(srApi.findServiceDefinitions(Mockito.eq(KEYWORD), Mockito.same(classifications))).toThrow(new RuntimeException());
		final Map<String, Object> responseContext = new HashMap<String, Object>();
		responseContext.put(MessageContext.HTTP_RESPONSE_CODE, 401);
		Mockito.stub(bindingProvider.getResponseContext()).toReturn(responseContext);
		
		query.execute(opRunner);
	}

	private void verifyMonitorStartedAndStopped()
	{
		Mockito.verify(progressMonitor, Mockito.times(1)).beginTask(Mockito.eq(SearchCoreMessages.SEARCH_IN_PROGRESS), Mockito.eq(IProgressMonitor.UNKNOWN));
		Mockito.verify(progressMonitor, Mockito.times(1)).done();
	}

	private void setupQuery()
	{
		final ISearchParameters paramsMock = Mockito.mock(ISearchParameters.class);
		Mockito.stub(paramsMock.getKeywordString()).toReturn(KEYWORD);
		Mockito.stub(paramsMock.getSearchDestination()).toReturn(searchDestination);
		Mockito.stub(paramsMock.getCustomParameters()).toReturn(customParamsMap());
		query = (FindServiceDefinitionsQuery) new SrSearchProvider()
		{
			protected ISrApi createSrApi(IServicesRegistrySystem srSystem)
			{
				return srApi;
			};
		}.createQuery(paramsMock);
	}

	private Map<Object, Object> customParamsMap()
	{
		final Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("CLASSIFICATION_VALUES_SELECTION", classifications);
		return map;
	}

	@SuppressWarnings("unchecked")
	private void setupOperationRunner() throws LongOpCanceledException, InvocationTargetException
	{
		Mockito.stub(opRunner.run(Mockito.isA(ILongOperation.class))).toAnswer(new Answer<Object>()
		{
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable
			{
				return ((ILongOperation<Object>) invocation.getArguments()[0]).run(progressMonitor);
			}
		});
	}

	private void setupProgressMonitor()
	{
		Mockito.stub(progressMonitor.isCanceled()).toAnswer(new Answer<Boolean>()
		{
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable
			{
				return isCancelled;
			}
		});
	}

	private void setupSearchDestination()
	{
		Mockito.stub(searchDestination.servicesRegistrySystem()).toReturn(srSystem);
	}
	
	private ServiceDefinitionsList newServiceDefinitionsList(final String... defIds)
	{
		final ServiceDefinitionsList result = new ServiceDefinitionsList();
		for(String id : defIds)
		{
			final ServiceDefinitionInfo definitionInfo = new ServiceDefinitionInfo();
			definitionInfo.setKey(id);
			result.getServiceDefinitionInfos().add(definitionInfo);
		}
		return result;
	}
}
