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

import java.lang.reflect.Method;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.eclipse.platform.discovery.util.api.longop.ILongOperationRunner;
import org.eclipse.servicesregistry.proxy.types.ServiceDefinitionInfo;
import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.ArtifactProxy;
import org.eclipse.servicesregistry.search.core.internal.search.result.resolve.UnresolvedInvocationImpossibleException;
import org.eclipse.servicesregistry.search.core.model.IServiceDefinition;
import org.eclipse.servicesregistry.search.core.model.IServiceEndpoint;

public class ServiceDefinition extends ArtifactProxy<IServiceDefinition> implements IServiceDefinition
{
	private final ServiceDefinitionInfo defInfo;
	private final IServiceDefinition proxyDefinition;

	public ServiceDefinition(final ServiceDefinitionInfo defInfo, final OnDemandArtifactQuery<IServiceDefinition> resolver, final ILongOperationRunner opRunner)
	{
		super(resolver, opRunner);
		this.defInfo = defInfo;
		this.proxyDefinition = this.create(IServiceDefinition.class, IServiceDefinition.class.getClassLoader());
		
	}

	@Override
	public String getId()
	{
		return proxyDefinition.getId();
	}

	@Override
	public QName getPorttypeQName()
	{
		return proxyDefinition.getPorttypeQName();
	}

	@Override
	public String getDocumentationUrl()
	{
		return proxyDefinition.getDocumentationUrl();
	}

	@Override
	public Collection<IServiceEndpoint> getEndpoints()
	{
		return proxyDefinition.getEndpoints();
	}
	
	@Override
	public String getWsdlUrl()
	{
		return proxyDefinition.getWsdlUrl();
	}

	@Override
	public String getPhysicalSystem()
	{
		return proxyDefinition.getPhysicalSystem();
	}
	
	@Override
	protected IServiceDefinition artifact()
	{
		return this;
	}

	@Override
	public String getName()
	{
		return proxyDefinition.getName();
	}
	
	@Override
	protected Object unresolvedInvocation(final Object proxy, final Method method, final Object[] args) throws UnresolvedInvocationImpossibleException
	{
		if(method.getName().equals("getId"))
		{
			return defInfo.getKey();
		}
		if(method.getName().equals("getName"))
		{
			return defInfo.getName();
		}
		throw new UnresolvedInvocationImpossibleException();
	}
}
