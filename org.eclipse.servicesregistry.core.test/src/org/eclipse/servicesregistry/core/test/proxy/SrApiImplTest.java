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
package org.eclipse.servicesregistry.core.test.proxy;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.servicesregistry.core.ISrApi;
import org.eclipse.servicesregistry.core.PhysicalSystemNotFoundException;
import org.eclipse.servicesregistry.core.ServiceDefinitionNotFoundException;
import org.eclipse.servicesregistry.core.SrApiException;
import org.eclipse.servicesregistry.core.classifications.IClassificationSystemNode;
import org.eclipse.servicesregistry.core.classifications.IClassificationValueNode;
import org.eclipse.servicesregistry.core.classifications.ITreeNodeList;
import org.eclipse.servicesregistry.core.internal.SrApiImpl;
import org.eclipse.servicesregistry.core.internal.classifications.IClassificationsTreeBuilder;
import org.eclipse.servicesregistry.proxy.ServicesRegistrySi;
import org.eclipse.servicesregistry.proxy.types.ClassificationReference;
import org.eclipse.servicesregistry.proxy.types.ClassificationSystemKey;
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
import org.eclipse.servicesregistry.proxy.types.ServiceEndpointList;
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
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class SrApiImplTest
{
	private static final String SEARCH_TERM = "my.search.term";
	private static final QName CLASSIF_SYSTEM_QNAME = new QName("testns", "testsystem");
	private static final String CLASSIFICATION_VALUE_CODE = "test_classif_value_code";
	
	@Mock
	private ServicesRegistrySi srProxy;
	@Mock
	private IClassificationsTreeBuilder classifFinder;
	@Mock
	private IClassificationValueNode classificationValue;
	@Mock
	private IClassificationSystemNode classificationSystem;

	private Collection<IClassificationValueNode> classificationValuesList;
	private ISrApi srApi;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		
		Mockito.stub(classificationValue.getClassificationSystem()).toReturn(classificationSystem);
		Mockito.stub(classificationValue.getCode()).toReturn(CLASSIFICATION_VALUE_CODE);
		
		Mockito.stub(classificationSystem.getQname()).toReturn(CLASSIF_SYSTEM_QNAME);
		
		srApi = new SrApiImpl(srProxy, classifFinder);
		classificationValuesList = new ArrayList<IClassificationValueNode>();
		classificationValuesList.add(classificationValue);
	}

	@Test
	public void testFindServiceDefinitions() throws FindServiceDefinitionsFault, ServicesRegistrySifindServiceDefinitionsFault, ServicesRegistrySifindServiceDefinitionsFault1, ServicesRegistrySifindServiceDefinitionsFault12, SrApiException
	{
		final ServiceDefinitionsList expectedResult = new ServiceDefinitionsList();
		Mockito.stub(srProxy.findServiceDefinitions(Mockito.argThat(sdefSearchAttrMatcher()))).toReturn(expectedResult);
		assertSame("Unexpected service definition returned", expectedResult, srApi.findServiceDefinitions(SEARCH_TERM, classificationValuesList));
	}

	@Test(expected = SrApiException.class)
	public void testFindServiceDefinitionsThrowingExc() throws FindServiceDefinitionsFault, ServicesRegistrySifindServiceDefinitionsFault, ServicesRegistrySifindServiceDefinitionsFault1, ServicesRegistrySifindServiceDefinitionsFault12, SrApiException
	{
		Mockito.stub(srProxy.findServiceDefinitions(Mockito.argThat(sdefSearchAttrMatcher()))).toThrow(new FindServiceDefinitionsFault(null, null));
		srApi.findServiceDefinitions(SEARCH_TERM, classificationValuesList);
	}

	@Test
	public void testGetServiceDefinition() throws SrApiException, GetServiceDefinitionsFault, ServicesRegistrySigetServiceDefinitionsFault, ServicesRegistrySigetServiceDefinitionsFault1, ServicesRegistrySigetServiceDefinitionsFault12, ServiceDefinitionNotFoundException
	{
		final ServiceDefinition expectedResult = new ServiceDefinition();
		Mockito.stub(srProxy.getServiceDefinitions(Mockito.argThat(serviceDefKeyMatcher()))).toReturn(serviceDefinitionList(expectedResult));
		assertSame("Unexpected result", expectedResult, srApi.getServiceDefinition(SEARCH_TERM));
	}

	@Test(expected = SrApiException.class)
	public void testGetServiceDefinitionThrowingExc() throws SrApiException, GetServiceDefinitionsFault, ServicesRegistrySigetServiceDefinitionsFault, ServicesRegistrySigetServiceDefinitionsFault1, ServicesRegistrySigetServiceDefinitionsFault12, ServiceDefinitionNotFoundException
	{
		Mockito.stub(srProxy.getServiceDefinitions(Mockito.argThat(serviceDefKeyMatcher()))).toThrow(new GetServiceDefinitionsFault(null, null));
		srApi.getServiceDefinition(SEARCH_TERM);
	}

	@Test
	public void testFindServiceEndpoints() throws FindServiceEndpointsFault, ServicesRegistrySifindServiceEndpointsFault, ServicesRegistrySifindServiceEndpointsFault1, ServicesRegistrySifindServiceEndpointsFault12, SrApiException
	{
		final ServiceEndpointResult expectedEndpoint = new ServiceEndpointResult();
		Mockito.stub(srProxy.findServiceEndpoints(Mockito.argThat(endpointSearchArgMatcher()))).toReturn(serviceEndpointList(expectedEndpoint));
		assertTrue("Unexpected result", srApi.findServiceEndpoints(SEARCH_TERM).contains(expectedEndpoint));
	}

	@Test(expected = SrApiException.class)
	public void testFindServiceEndpointsThrowingExc() throws FindServiceEndpointsFault, ServicesRegistrySifindServiceEndpointsFault, ServicesRegistrySifindServiceEndpointsFault1, ServicesRegistrySifindServiceEndpointsFault12, SrApiException
	{
		Mockito.stub(srProxy.findServiceEndpoints(Mockito.argThat(endpointSearchArgMatcher()))).toThrow(new FindServiceEndpointsFault(null, null));
		srApi.findServiceEndpoints(SEARCH_TERM);
	}

	@Test
	public void testGetPhysicalSystem() throws GetPhysicalSystemsFault, ServicesRegistrySigetPhysicalSystemsFault, ServicesRegistrySigetPhysicalSystemsFault1, ServicesRegistrySigetPhysicalSystemsFault12, SrApiException, PhysicalSystemNotFoundException
	{
		final PhysicalSystem expectedResult = new PhysicalSystem();
		final PhysicalSystemKey systemKey = new PhysicalSystemKey();
		Mockito.stub(srProxy.getPhysicalSystems(Mockito.argThat(physicalSystemKeyMatcher(systemKey)))).toReturn(Arrays.asList(new PhysicalSystem[] { expectedResult }));
		assertSame("Unexpected result", expectedResult, srApi.getPhysicalSystem(systemKey));
	}

	@Test(expected = SrApiException.class)
	public void testGetPhysicalSystemThrowingExc() throws GetPhysicalSystemsFault, ServicesRegistrySigetPhysicalSystemsFault, ServicesRegistrySigetPhysicalSystemsFault1, ServicesRegistrySigetPhysicalSystemsFault12, SrApiException, PhysicalSystemNotFoundException
	{
		final PhysicalSystemKey systemKey = new PhysicalSystemKey();
		Mockito.stub(srProxy.getPhysicalSystems(Mockito.argThat(physicalSystemKeyMatcher(systemKey)))).toThrow(new GetPhysicalSystemsFault(null, null));
		srApi.getPhysicalSystem(systemKey);
	}

	@Test
	public void testGetClassifications() throws GetClassificationSystemsGreaterThanVersionFault, ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault, GetClassificationSystemValuesFault, ServicesRegistrySigetClassificationSystemValuesFault, SrApiException
	{
		final ITreeNodeList classifTree = Mockito.mock(ITreeNodeList.class);
		Mockito.stub(classifFinder.buildTree()).toReturn(classifTree);
		assertSame("Unexpected classifications result", classifTree, srApi.getClassifications());
	}
	
	@Test(expected=SrApiException.class)
	public void testGetClassificationsWithError() throws GetClassificationSystemsGreaterThanVersionFault, ServicesRegistrySigetClassificationSystemsGreaterThanVersionFault, GetClassificationSystemValuesFault, ServicesRegistrySigetClassificationSystemValuesFault, SrApiException
	{
		final GetClassificationSystemsGreaterThanVersionFault exc = new GetClassificationSystemsGreaterThanVersionFault(null, null);
		Mockito.stub(classifFinder.buildTree()).toThrow(exc);
		srApi.getClassifications();
	}
	
	
	@Test(expected=ServiceDefinitionNotFoundException.class)
	public void testGetServiceDefinitionNothingFound() throws GetServiceDefinitionsFault, ServicesRegistrySigetServiceDefinitionsFault, ServicesRegistrySigetServiceDefinitionsFault1, ServicesRegistrySigetServiceDefinitionsFault12, SrApiException, ServiceDefinitionNotFoundException
	{
		Mockito.stub(srProxy.getServiceDefinitions(Mockito.argThat(serviceDefKeyMatcher()))).toReturn(new ArrayList<ServiceDefinition>());
		srApi.getServiceDefinition(SEARCH_TERM);
	}
	
	@Test(expected=PhysicalSystemNotFoundException.class)
	public void testGetPhysicalSystemNothingFound() throws GetPhysicalSystemsFault, ServicesRegistrySigetPhysicalSystemsFault, ServicesRegistrySigetPhysicalSystemsFault1, ServicesRegistrySigetPhysicalSystemsFault12, SrApiException, PhysicalSystemNotFoundException
	{
		final PhysicalSystemKey systemKey = new PhysicalSystemKey();
		Mockito.stub(srProxy.getPhysicalSystems(Mockito.argThat(physicalSystemKeyMatcher(systemKey)))).toReturn(new ArrayList<PhysicalSystem>());
		srApi.getPhysicalSystem(systemKey);
	}
	
	
	private Matcher<List<PhysicalSystemKey>> physicalSystemKeyMatcher(final PhysicalSystemKey systemKey)
	{
		return new BaseMatcher<List<PhysicalSystemKey>>()
		{

			@SuppressWarnings("unchecked")
			@Override
			public boolean matches(Object item)
			{
				return ((List<PhysicalSystemKey>) item).iterator().next() == systemKey;
			}

			@Override
			public void describeTo(Description description)
			{
			}
		};
	}

	private Matcher<ServiceEndpointSearchAttributes> endpointSearchArgMatcher()
	{
		return new BaseMatcher<ServiceEndpointSearchAttributes>()
		{
			@Override
			public boolean matches(Object item)
			{
				return ((ServiceEndpointSearchAttributes) item).getServiceDefinitionKey().iterator().next().getUddiKey().equals(SEARCH_TERM);
			}

			@Override
			public void describeTo(Description description)
			{
			}
		};
	}

	private ServiceEndpointList serviceEndpointList(ServiceEndpointResult expectedResult)
	{
		final ServiceEndpointList list = new ServiceEndpointList();
		list.getServiceEndpoint().add(expectedResult);
		return list;
	}

	private List<ServiceDefinition> serviceDefinitionList(ServiceDefinition expectedResult)
	{
		return Arrays.asList(new ServiceDefinition[] { expectedResult });
	}

	private Matcher<List<ServiceDefinitionKey>> serviceDefKeyMatcher()
	{
		return new BaseMatcher<List<ServiceDefinitionKey>>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public boolean matches(Object item)
			{
				return ((List<ServiceDefinitionKey>) item).iterator().next().getUddiKey().equals(SEARCH_TERM);
			}

			@Override
			public void describeTo(Description description)
			{
			}
		};
	}
	
	private Matcher<ServiceDefinitionSearchAttributes> sdefSearchAttrMatcher()
	{
		return new BaseMatcher<ServiceDefinitionSearchAttributes>()
		{
			@Override
			public boolean matches(Object item)
			{
				final ServiceDefinitionSearchAttributes argument = (ServiceDefinitionSearchAttributes)item;
				if(!argument.getName().equals(SEARCH_TERM))
				{
					return false;
				}
				
				final List<ClassificationReference> classifRefs = argument.getClassifcations().getClassificationReferences();
				if(classifRefs.size() != 1)
				{
					return false;
				}
				
				final ClassificationSystemKey classifSystemKey = classifRefs.get(0).getClassificationSystemKey();
				if(!classifSystemKey.getQname().equals(CLASSIF_SYSTEM_QNAME))
				{
					return false;
				}
				
				return classifRefs.get(0).getValue().equals(CLASSIFICATION_VALUE_CODE);
			}

			@Override
			public void describeTo(Description description)
			{
			}
		};
	}
}
