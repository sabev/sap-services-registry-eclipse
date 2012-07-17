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
package org.eclipse.servicesregistry.core.internal;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.ws.BindingProvider;

import org.eclipse.servicesregistry.core.ISrApi;
import org.eclipse.servicesregistry.core.PhysicalSystemNotFoundException;
import org.eclipse.servicesregistry.core.ServiceDefinitionNotFoundException;
import org.eclipse.servicesregistry.core.SrApiException;
import org.eclipse.servicesregistry.core.classifications.IClassificationValueNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNodeList;
import org.eclipse.servicesregistry.core.internal.classifications.IClassificationsTreeBuilder;
import org.eclipse.servicesregistry.proxy.ServicesRegistrySi;
import org.eclipse.servicesregistry.proxy.types.ClassificationReference;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystemKey;
import org.eclipse.servicesregistry.proxy.types.Classifications;
import org.eclipse.servicesregistry.proxy.types.FindServiceDefinitionsFault;
import org.eclipse.servicesregistry.proxy.types.FindServiceEndpointsFault;
import org.eclipse.servicesregistry.proxy.types.GetClassificationSystemValuesFault;
import org.eclipse.servicesregistry.proxy.types.GetClassificationSystemsGreaterThanVersionFault;
import org.eclipse.servicesregistry.proxy.types.GetPhysicalSystemsFault;
import org.eclipse.servicesregistry.proxy.types.GetServiceDefinitionsFault;
import org.eclipse.servicesregistry.proxy.types.PhysicalSystem;
import org.eclipse.servicesregistry.proxy.types.PhysicalSystemKey;
import org.eclipse.servicesregistry.proxy.types.ServiceDefinition;
import org.eclipse.servicesregistry.proxy.types.ServiceDefinitionKey;
import org.eclipse.servicesregistry.proxy.types.ServiceDefinitionSearchAttributes;
import org.eclipse.servicesregistry.proxy.types.ServiceDefinitionsList;
import org.eclipse.servicesregistry.proxy.types.ServiceEndpointResult;
import org.eclipse.servicesregistry.proxy.types.ServiceEndpointSearchAttributes;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySifindServiceDefinitionsFault;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySifindServiceDefinitionsFault1;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySifindServiceDefinitionsFault12;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySifindServiceEndpointsFault;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySifindServiceEndpointsFault1;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySifindServiceEndpointsFault12;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetClassificationSystemValuesFault;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetPhysicalSystemsFault;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetPhysicalSystemsFault1;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetPhysicalSystemsFault12;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetServiceDefinitionsFault;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetServiceDefinitionsFault1;
import org.eclipse.servicesregistry.proxy.types.ServicesRegistrySigetServiceDefinitionsFault12;

public class SrApiImpl implements ISrApi
{
	private final ServicesRegistrySi srProxy;
	private final IClassificationsTreeBuilder classificationsTreeBuilder;

	public SrApiImpl(final ServicesRegistrySi srProxy, final IClassificationsTreeBuilder classificationsTreeBuilder)
	{
		this.srProxy = srProxy;
		this.classificationsTreeBuilder = classificationsTreeBuilder;
	}

	@Override
	public ServiceDefinitionsList findServiceDefinitions(final String keyword, final Collection<IClassificationValueNode> selectedClassificationValues) throws SrApiException
	{
		try
		{
			return srProxy.findServiceDefinitions(createSearviceDefinitionsSearchAttributes(keyword, selectedClassificationValues));
		}
		catch (FindServiceDefinitionsFault e)
		{
			throw new SrApiException(e);
		}
		catch (ServicesRegistrySifindServiceDefinitionsFault e)
		{
			throw new SrApiException(e);
		}
		catch (ServicesRegistrySifindServiceDefinitionsFault1 e)
		{
			throw new SrApiException(e);
		}
		catch (ServicesRegistrySifindServiceDefinitionsFault12 e)
		{
			throw new SrApiException(e);
		}
	}

	@Override
	public ServiceDefinition getServiceDefinition(String serviceDefUddiKey) throws SrApiException, ServiceDefinitionNotFoundException
	{
		final ServiceDefinitionKey serviceDefKey = new ServiceDefinitionKey();
		serviceDefKey.setUddiKey(serviceDefUddiKey);

		final List<ServiceDefinitionKey> serviceDefKeys = new LinkedList<ServiceDefinitionKey>();
		serviceDefKeys.add(serviceDefKey);

		try
		{
			final List<ServiceDefinition> result = srProxy.getServiceDefinitions(serviceDefKeys);
			if(result.isEmpty())
			{
				throw new ServiceDefinitionNotFoundException(serviceDefUddiKey);
			}
			return result.iterator().next();
		}
		catch (GetServiceDefinitionsFault e)
		{
			throw new SrApiException(e);
		}
		catch (ServicesRegistrySigetServiceDefinitionsFault e)
		{
			throw new SrApiException(e);
		}
		catch (ServicesRegistrySigetServiceDefinitionsFault1 e)
		{
			throw new SrApiException(e);
		}
		catch (ServicesRegistrySigetServiceDefinitionsFault12 e)
		{
			throw new SrApiException(e);
		}
	}

	@Override
	public List<ServiceEndpointResult> findServiceEndpoints(final String serviceDefUddiKey) throws SrApiException
	{
		final ServiceDefinitionKey serviceDefKey = new ServiceDefinitionKey();
		serviceDefKey.setUddiKey(serviceDefUddiKey);

		final ServiceEndpointSearchAttributes epSearchAttributes = new ServiceEndpointSearchAttributes();
		epSearchAttributes.getServiceDefinitionKey().add(serviceDefKey);

		try
		{
			return srProxy.findServiceEndpoints(epSearchAttributes).getServiceEndpoint();
		}
		catch (FindServiceEndpointsFault e)
		{
			throw new SrApiException(e);
		}
		catch (ServicesRegistrySifindServiceEndpointsFault e)
		{
			throw new SrApiException(e);
		}
		catch (ServicesRegistrySifindServiceEndpointsFault1 e)
		{
			throw new SrApiException(e);
		}
		catch (ServicesRegistrySifindServiceEndpointsFault12 e)
		{
			throw new SrApiException(e);
		}
	}

	@Override
	public PhysicalSystem getPhysicalSystem(final PhysicalSystemKey physicalSystemKey) throws SrApiException, PhysicalSystemNotFoundException
	{
		final List<PhysicalSystemKey> physicalSystemKeys = new LinkedList<PhysicalSystemKey>();
		physicalSystemKeys.add(physicalSystemKey);

		try
		{
			final List<PhysicalSystem> result = srProxy.getPhysicalSystems(physicalSystemKeys);
			if(result.isEmpty())
			{
				throw new PhysicalSystemNotFoundException(physicalSystemKey);
			}
			return result.iterator().next();
		}
		catch (GetPhysicalSystemsFault e)
		{
			throw new SrApiException(e);
		}
		catch (ServicesRegistrySigetPhysicalSystemsFault e)
		{
			throw new SrApiException(e);
		}
		catch (ServicesRegistrySigetPhysicalSystemsFault1 e)
		{
			throw new SrApiException(e);
		}
		catch (ServicesRegistrySigetPhysicalSystemsFault12 e)
		{
			throw new SrApiException(e);
		}
	}

	@Override
	public BindingProvider getProxyBindingProvider()
	{
		return (BindingProvider)this.srProxy;
	}

	@Override
	public ITreeNodeList getClassifications() throws SrApiException
	{
		try
		{
			return classificationsTreeBuilder.buildTree();
		}
		catch (GetClassificationSystemsGreaterThanVersionFault e)
		{
			throw new SrApiException(e);
		}
		catch (ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault e)
		{
			throw new SrApiException(e);
		}
		catch (GetClassificationSystemValuesFault e)
		{
			throw new SrApiException(e);
		}
		catch (ServicesRegistrySigetClassificationSystemValuesFault e)
		{
			throw new SrApiException(e);
		}
	}
	
	private ServiceDefinitionSearchAttributes createSearviceDefinitionsSearchAttributes(final String keyword, final Collection<IClassificationValueNode> selectedClassificationValues)
	{
		final ServiceDefinitionSearchAttributes attributes = new ServiceDefinitionSearchAttributes();
		attributes.setName(keyword);
		attributes.setClassifcations(createClassifications(selectedClassificationValues));
		return attributes;
	}

	private Classifications createClassifications(final Collection<IClassificationValueNode> selectedClassificationValues)
	{
		final Classifications result = new Classifications();
		result.getClassificationReferences().addAll(createClassificationReferences(selectedClassificationValues));
		return result;
	}

	private Collection<ClassificationReference> createClassificationReferences(Collection<IClassificationValueNode> selectedClassificationValues)
	{
		final Collection<ClassificationReference> result = new LinkedList<ClassificationReference>();
		for(IClassificationValueNode classifValue : selectedClassificationValues)
		{
			result.add(createClassificationReference(classifValue));
		}
		return result;
	}

	private ClassificationReference createClassificationReference(final IClassificationValueNode classifValue)
	{
		final ClassificationSystemKey systemKey = new ClassificationSystemKey();
		systemKey.setQname(classifValue.getClassificationSystem().getQname());
		
		final ClassificationReference ref = new ClassificationReference();
		ref.setClassificationSystemKey(systemKey);
		ref.setValue(classifValue.getCode());
		
		return ref;
	}
}
