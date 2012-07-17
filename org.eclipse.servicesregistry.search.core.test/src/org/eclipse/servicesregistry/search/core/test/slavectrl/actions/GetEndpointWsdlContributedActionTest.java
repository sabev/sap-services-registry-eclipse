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
package org.eclipse.servicesregistry.search.core.test.slavectrl.actions;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.platform.discovery.util.api.env.IErrorHandler;
import org.eclipse.platform.discovery.util.api.longop.ILongOperationRunner;
import org.eclipse.platform.discovery.util.internal.longop.CurrentThreadOperationRunner;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.GetEndpointWsdlContributedAction;
import org.eclipse.servicesregistry.search.core.internal.slavectrl.IEndpointActionConfiguration;
import org.eclipse.servicesregistry.wsdl.endpoint.IEndpointWsdlData;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlDefinition;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IExistingFilesProcessor;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlStrategyException;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GetEndpointWsdlContributedActionTest
{
	private final QName SERVICE_QNAME = new QName("http://www.regom.de", "TestService");
	private final QName PORTTYPE_QNAME = new QName("http://www.regom.de", "TMorseSoap");
	private final QName BINDING_QNAME = new QName("http://www.regom.de", "TMorseSoapBinding");
	private final String ENDPOINT_ADDR = "http://test.endpoint";
	private final String ENDPOINT_NAME = "TestEndpoin";
	private File sourceWsdlFile;
	private File targetDir;

	private GetEndpointWsdlContributedAction action;

	@Mock
	private IExistingFilesProcessor existingFilesProcessor;
	@Mock
	private IEndpointActionConfiguration actionConfig;
	@Mock
	private IErrorHandler errorHandler;
	@Mock
	private IEndpointWsdlData epWsdlData;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		sourceWsdlFile = testFileUtils().copyToTempLocation(this.getClass(), "morse.wsdl", "morse" + System.currentTimeMillis() + ".wsdl");
		targetDir = new File(new org.eclipse.servicesregistry.wsdl.internal.util.FileUtils().systemTempDir(), "test" + System.currentTimeMillis());
		assertTrue("Target dis was not created", targetDir.mkdirs());

		stub(existingFilesProcessor.process(any(Set.class))).toReturn(Status.OK_STATUS);

		stub(actionConfig.existingFilesProcessor()).toReturn(existingFilesProcessor);
		stub(actionConfig.saveDestination()).toReturn(targetDir);
		stub(actionConfig.errorHandler()).toReturn(errorHandler);
		stub(actionConfig.endpointData()).toReturn(epWsdlData);
		stub(actionConfig.endpointData()).toReturn(epWsdlData);
		stub(actionConfig.wsdlUrl()).toReturn(sourceWsdlFile.toURI().toURL().toString());
		stub(actionConfig.rootWsdlFileName()).toReturn(PORTTYPE_QNAME.getLocalPart());

		stub(epWsdlData.bindingQName()).toReturn(BINDING_QNAME);
		stub(epWsdlData.bindingWsdlUrl()).toReturn(sourceWsdlFile.toURI().toURL());
		stub(epWsdlData.endpointAddress()).toReturn(ENDPOINT_ADDR);
		stub(epWsdlData.endpointName()).toReturn(ENDPOINT_NAME);
		stub(epWsdlData.serviceQName()).toReturn(SERVICE_QNAME);
		stub(epWsdlData.porttypeQName()).toReturn(PORTTYPE_QNAME);

		action = new GetEndpointWsdlContributedAction();
	}

	private org.eclipse.servicesregistry.testutils.TestFileUtils testFileUtils()
	{
		return new org.eclipse.servicesregistry.testutils.TestFileUtils();
	}

	private void runAction()
	{
		final Set<Object> configs = new HashSet<Object>();
		configs.add(actionConfig);
		action.perform(new CurrentThreadOperationRunner(new NullProgressMonitor()), configs);
	}

	@After
	public void tearDown() throws Exception
	{
		sourceWsdlFile.delete();
		testFileUtils().deleteDirectory(targetDir);
	}

	@Test
	public void testGetWsdl() throws WsdlStrategyException, IOException
	{
		final List<IWsdlWtpDescriptorContainer> containerHolder = new ArrayList<IWsdlWtpDescriptorContainer>();
		runAction();
		verify(actionConfig, times(1)).wsdlDownloaded(argThat(wsdlContainerMatcher(containerHolder)), any(ILongOperationRunner.class));

		final IWsdlWtpDescriptorContainer wsdlContainer = containerHolder.iterator().next();
		final IWsdlDefinition rootWsdl = wsdlContainer.getRootWsdlDefinition();
		assertEquals("Unexpected wsdl file", PORTTYPE_QNAME.getLocalPart() + ".wsdl", wsdlContainer.getRootWsdlDefinition().getFile().getName());

		final Service service = (Service) rootWsdl.getEObject().getEServices().iterator().next();
		assertEquals("Unexpected service", SERVICE_QNAME, service.getQName());

		final PortType portType = (PortType) rootWsdl.getEObject().getEPortTypes().iterator().next();
		assertEquals("Unexpected porttype", PORTTYPE_QNAME, portType.getQName());
	}

	private Matcher<IWsdlWtpDescriptorContainer> wsdlContainerMatcher(final List<IWsdlWtpDescriptorContainer> container)
	{
		return new BaseMatcher<IWsdlWtpDescriptorContainer>()
		{

			@Override
			public boolean matches(Object item)
			{
				container.add((IWsdlWtpDescriptorContainer) item);
				return true;
			}

			@Override
			public void describeTo(Description description)
			{
			}
		};
	}

}
