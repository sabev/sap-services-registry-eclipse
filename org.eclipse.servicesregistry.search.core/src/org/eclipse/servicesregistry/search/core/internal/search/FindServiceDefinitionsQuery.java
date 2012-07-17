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
package org.eclipse.servicesregistry.search.core.internal.search;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;

import javax.xml.ws.handler.MessageContext;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.platform.discovery.runtime.api.ISearchQuery;
import org.eclipse.platform.discovery.runtime.api.SearchCancelledException;
import org.eclipse.platform.discovery.runtime.api.SearchFailedException;
import org.eclipse.platform.discovery.runtime.api.impl.SearchQuery;
import org.eclipse.platform.discovery.util.api.longop.ILongOperation;
import org.eclipse.platform.discovery.util.api.longop.ILongOperationRunner;
import org.eclipse.platform.discovery.util.api.longop.LongOpCanceledException;
import org.eclipse.servicesregistry.core.ISrApi;
import org.eclipse.servicesregistry.core.classifications.IClassificationValueNode;
import org.eclipse.servicesregistry.core.internal.logging.ILogger;
import org.eclipse.servicesregistry.proxy.types.ServiceDefinitionInfo;
import org.eclipse.servicesregistry.proxy.types.ServiceDefinitionsList;
import org.eclipse.servicesregistry.search.core.internal.plugin.text.SearchCoreMessages;
import org.eclipse.servicesregistry.search.core.internal.search.result.ServiceDefinition;
import org.eclipse.servicesregistry.search.core.internal.search.result.ServiceDefinitionResolver;
import org.eclipse.servicesregistry.search.core.internal.search.result.ServiceDefinitionsResult;
import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;

public class FindServiceDefinitionsQuery extends SearchQuery implements ISearchQuery
{
	private static final int HTTP_UNAUTHORIZED_CODE = 401;
	private static final int HTTP_RESPONSE_CODE_UNKNOWN = Integer.MIN_VALUE;
	
	private final String keyword;
	private final ISrApi srApi;
	private final ILogger logger;
	private final Collection<IClassificationValueNode> classificationValues;

	public FindServiceDefinitionsQuery(final String keyword, final Collection<IClassificationValueNode> classificationValues, final ISrApi srApi, final ILogger logger)
	{
		this.keyword = keyword;
		this.srApi = srApi;
		this.logger = logger;
		this.classificationValues = classificationValues;
	}

	@Override
	public Object execute(final ILongOperationRunner lor) throws SearchFailedException, SearchCancelledException
	{
		final ILongOperation<ServiceDefinitionsList> searchOperation = new ILongOperation<ServiceDefinitionsList>()
		{
			@Override
			public ServiceDefinitionsList run(IProgressMonitor monitor) throws LongOpCanceledException, Exception
			{
				return srApi.findServiceDefinitions(keyword, classificationValues);
			}
		};

		final ILongOperation<ServiceDefinitionsList> operationDecorator = new OperationInParalelThread<ServiceDefinitionsList>(searchOperation){
			@Override
			public void beginTask(IProgressMonitor monitor)
			{
				monitor.beginTask(SearchCoreMessages.SEARCH_IN_PROGRESS, IProgressMonitor.UNKNOWN);
			}

			@Override
			public void done(IProgressMonitor monitor)
			{
				monitor.done();
			}};
		
		return doExecute(lor, operationDecorator);
	}
	
	private Object doExecute(final ILongOperationRunner lor, final ILongOperation<ServiceDefinitionsList> operation) throws SearchFailedException
	{
		try
		{
			return new ServiceDefinitionsResult(toServiceDefitions(lor.run(operation), lor));
		}
		catch (LongOpCanceledException e)
		{
			throw new SearchCancelledException(e);
		}
		catch (InvocationTargetException e)
		{
			if(isAuthorizationCancelled())
			{
				logger.logDebug("Authorization cancelled => cancelling search");
				throw new SearchCancelledException();
			}
			throw new SearchFailedException(e.getCause());
		}
	}

	private boolean isAuthorizationCancelled()
	{
		return httpResponseCode() == HTTP_UNAUTHORIZED_CODE;
	}

	private int httpResponseCode()
	{
		final Object responseCode = srApi.getProxyBindingProvider().getResponseContext().get(MessageContext.HTTP_RESPONSE_CODE);
		return responseCode == null ? HTTP_RESPONSE_CODE_UNKNOWN : (Integer)responseCode;
	}

	private Collection<IServiceDefinition> toServiceDefitions(ServiceDefinitionsList foundDefinitions, final ILongOperationRunner opRunner)
	{
		final Collection<IServiceDefinition> result = new LinkedList<IServiceDefinition>();
		for (ServiceDefinitionInfo defInfo : foundDefinitions.getServiceDefinitionInfos())
		{
			result.add(new ServiceDefinition(defInfo, new ServiceDefinitionResolver(srApi), opRunner));
		}
		return result;
	}
}
