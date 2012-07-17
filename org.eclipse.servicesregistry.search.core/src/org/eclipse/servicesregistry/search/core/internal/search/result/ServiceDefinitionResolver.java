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
package org.eclipse.servicesregistry.search.core.internal.search.result;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.platform.discovery.util.api.longop.ILongOperation;
import org.eclipse.platform.discovery.util.api.longop.LongOpCanceledException;
import org.eclipse.servicesregistry.core.ISrApi;
import org.eclipse.servicesregistry.core.PhysicalSystemNotFoundException;
import org.eclipse.servicesregistry.core.ServiceDefinitionNotFoundException;
import org.eclipse.servicesregistry.core.SrApiException;
import org.eclipse.servicesregistry.proxy.types.PhysicalSystemKey;
import org.eclipse.servicesregistry.proxy.types.ServiceDefinition;
import org.eclipse.servicesregistry.proxy.types.ServiceDefinitionKey;
import org.eclipse.servicesregistry.proxy.types.ServiceEndpointResult;
import org.eclipse.servicesregistry.proxy.types.ServiceEndpointSearchAttributes;
import org.eclipse.servicesregistry.search.core.internal.plugin.text.SearchCoreMessages;
import org.eclipse.servicesregistry.search.core.internal.search.OperationInParalelThread;
import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.ArtifactProxy.OnDemandArtifactQuery;
import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.ResolveException;
import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;
import org.eclipse.servicesregistry.search.core.model.IServiceEndpoint;

public class ServiceDefinitionResolver implements OnDemandArtifactQuery<IServiceDefinition>
{
	private final ISrApi srApi;

	public ServiceDefinitionResolver(final ISrApi srApi)
	{
		this.srApi = srApi;
	}

	@Override
	public IServiceDefinition resolve(final IServiceDefinition serviceDef, final IProgressMonitor monitor) throws LongOpCanceledException
	{
		final ILongOperation<IServiceDefinition> resolveOperation = new ILongOperation<IServiceDefinition>()
		{
			@Override
			public IServiceDefinition run(IProgressMonitor monitor) throws LongOpCanceledException, Exception
			{
				final ServiceDefinition serviceDefinition = getServiceDefinition(serviceDef);
				final List<ServiceEndpointResult> foundEndpoints = getServiceEndpoints(serviceDef);

				final IServiceDefinition resolvedDefinition = new ResolvedServiceDefinition(serviceDefinition, getPhysicalSystemName(serviceDefinition));
				resolvedDefinition.getEndpoints().addAll(resolvedEndpoints(foundEndpoints, resolvedDefinition));

				return resolvedDefinition;
			}
		};
		try
		{
			return new OperationInParalelThread<IServiceDefinition>(resolveOperation)
			{
				@Override
				public void beginTask(IProgressMonitor monitor)
				{
					// do nothing - task progress tracking is performed in super
				}

				@Override
				public void done(IProgressMonitor monitor)
				{
					// do nothing - task progress tracking is performed in super
				}
			}.run(monitor);
		}
		catch(LongOpCanceledException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new ResolveException(e);
		}
	}

	private Collection<IServiceEndpoint> resolvedEndpoints(final Collection<ServiceEndpointResult> foundEndpoints, final IServiceDefinition definition)
	{
		final Collection<IServiceEndpoint> result = new LinkedList<IServiceEndpoint>();
		for (ServiceEndpointResult endpoint : foundEndpoints)
		{
			result.add(new ResolvedEndpoint(endpoint, definition));
		}
		return result;
	}

	private ServiceDefinition getServiceDefinition(final IServiceDefinition serviceDef)
	{
		final ServiceDefinitionKey serviceDefKey = new ServiceDefinitionKey();
		serviceDefKey.setUddiKey(serviceDef.getId());

		final List<ServiceDefinitionKey> serviceDefKeys = new LinkedList<ServiceDefinitionKey>();
		serviceDefKeys.add(serviceDefKey);

		try
		{
			return srApi.getServiceDefinition(serviceDef.getId());
		}
		catch (SrApiException e)
		{
			throw new ResolveException(e);
		}
		catch (ServiceDefinitionNotFoundException e)
		{
			throw new ResolveException(e);
		}
	}

	private List<ServiceEndpointResult> getServiceEndpoints(final IServiceDefinition serviceDef)
	{
		final ServiceDefinitionKey serviceDefKey = new ServiceDefinitionKey();
		serviceDefKey.setUddiKey(serviceDef.getId());

		final ServiceEndpointSearchAttributes epSearchAttributes = new ServiceEndpointSearchAttributes();
		epSearchAttributes.getServiceDefinitionKey().add(serviceDefKey);

		try
		{
			return srApi.findServiceEndpoints(serviceDef.getId());
		}
		catch (SrApiException e)
		{
			throw new ResolveException(e);
		}
	}
	
	private String getPhysicalSystemName(final ServiceDefinition definition)
	{
		final PhysicalSystemKey systemKey = definition.getPhysicalSystemKey();
		if(systemKey == null)
		{
			return SearchCoreMessages.PhysicalSystemNotFound;
		}
		
		try
		{
			return srApi.getPhysicalSystem(systemKey).getSystemName();
		}
		catch (SrApiException e)
		{
			throw new ResolveException(e);			
		}
		catch (PhysicalSystemNotFoundException e)
		{
			throw new ResolveException(e);			
		}
	}

	private class ResolvedServiceDefinition implements IServiceDefinition
	{
		private final ServiceDefinition serviceDefinition;
		private final Collection<IServiceEndpoint> serviceEndpoints;
		private final String physicalSystemName;

		public ResolvedServiceDefinition(final ServiceDefinition resolvedServiceDefinition, final String physicalSystemName)
		{
			this.serviceDefinition = resolvedServiceDefinition;
			this.serviceEndpoints = new LinkedList<IServiceEndpoint>();
			this.physicalSystemName = physicalSystemName;
		}

		public String getId()
		{
			return serviceDefinition.getServiceDefinitionKey().getUddiKey();
		}

		public QName getPorttypeQName()
		{
			return serviceDefinition.getQname();
		}

		@Override
		public String getDocumentationUrl()
		{
			return serviceDefinition.getDocumentation();
		}

		@Override
		public Collection<IServiceEndpoint> getEndpoints()
		{
			return this.serviceEndpoints;
		}

		@Override
		public String getWsdlUrl()
		{
			return serviceDefinition.getWsdlURL();
		}

		@Override
		public String getPhysicalSystem()
		{
			return this.physicalSystemName;
		}

		@Override
		public String getName()
		{
			return serviceDefinition.getQname().getLocalPart();
		}
	}

	public class ResolvedEndpoint implements IServiceEndpoint
	{
		private IServiceDefinition serviceDefinition;
		private ServiceEndpointResult endpoint;

		public ResolvedEndpoint(final ServiceEndpointResult endpoint, final IServiceDefinition definition)
		{
			this.serviceDefinition = definition;
			this.endpoint = endpoint;
		}

		@Override
		public String getEndpointName()
		{
			return endpoint.getEndpointName();
		}

		@Override
		public String getBindingWsdlUrl()
		{
			return endpoint.getBindingWSDL();
		}

		@Override
		public IServiceDefinition getServiceDefinition()
		{
			return this.serviceDefinition;
		}

		@Override
		public String getEndpointAddress()
		{
			return endpoint.getEndpointTargetAddress();
		}

		@Override
		public QName getBindingQName()
		{
			return endpoint.getBindingQName();
		}

		@Override
		public QName getServiceQName()
		{
			return endpoint.getServiceQName();
		}
	}

}
