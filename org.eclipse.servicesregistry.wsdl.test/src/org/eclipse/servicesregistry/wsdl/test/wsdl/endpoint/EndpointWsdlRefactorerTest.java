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
package org.eclipse.servicesregistry.wsdl.test.wsdl.endpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.servicesregistry.testutils.TestFileUtils;
import org.eclipse.servicesregistry.wsdl.endpoint.BindingNotFoundException;
import org.eclipse.servicesregistry.wsdl.endpoint.EndpointWsdlRefactorer;
import org.eclipse.servicesregistry.wsdl.endpoint.IEndpointWsdlData;
import org.eclipse.servicesregistry.wsdl.endpoint.UnsupportedBindingException;
import org.eclipse.servicesregistry.wsdl.walker.CancelWalking;
import org.eclipse.servicesregistry.wsdl.walker.WsdlVisitor;
import org.eclipse.servicesregistry.wsdl.walker.WsdlWalker;
import org.eclipse.servicesregistry.wsdl.wsdlimport.IWsdlWtpImportTool;
import org.eclipse.servicesregistry.wsdl.wsdlimport.WsdlWtpImportToolFactory;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlDefinition;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlStrategyException;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.binding.http.HTTPAddress;
import org.eclipse.wst.wsdl.binding.soap.SOAPAddress;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EndpointWsdlRefactorerTest
{
	private enum BINDING_TYPE_EXPECTATION
	{
		SOAP, HTTP;
	};

	private EndpointWsdlRefactorer refactorer;
	private File sourceWsdlFile;
	private File importedWsdlFile;
	private IWsdlWtpImportTool importTool;
	private IWsdlWtpDescriptorContainer wsdlDescriptor;

	private static final QName SOAP_BINDING_QNAME = new QName("http://www.regom.de", "TMorseSoapBinding");
	private static final QName SOAP_PORTTYPE_QNAME = new QName("http://www.regom.de", "TMorseSoap");
	private static final QName SOAP_SERVICE_QNAME = new QName("http://www.regom.de", "MySoapService");

	private static final QName HTTP_BINDING_QNAME = new QName("http://www.regom.de", "HTTPBinding");
	private static final QName HTTP_PORTTYPE_QNAME = new QName("http://www.regom.de", "TMorseHTTP");
	private static final QName HTTP_SERVICE_QNAME = new QName("http://www.regom.de", "MyHTTPService");

	private static final QName IMPORTED_BINDING_QNAME = new QName("http://www.regom.de.imported", "ImportedBinding");
	private static final QName IMPORTED_PORTTYPE_QNAME = new QName("http://www.regom.de.imported", "ImportedPorttype");
	private static final QName IMPORTED_SERVICE_QNAME = new QName("http://www.regom.de.imported", "MyImportedService");

	private static final String ENDPOINT_ADDRESS = "http://test.sap.com/endpoint";
	private static final String ENDPOINT_NAME = "MyEndpoint";

	@Before
	public void setUp() throws Exception
	{
		sourceWsdlFile = testFileUtils().copyToTempLocation(this.getClass(), "morse_multbindings.wsdl", "morse_multbindings.wsdl");
		importedWsdlFile = testFileUtils().copyToTempLocation(this.getClass(), "morse_imported_wsdl.wsdl", "morse_imported_wsdl.wsdl");
		importTool = WsdlWtpImportToolFactory.createWsdlWtpImportTool(sourceWsdlFile.toURI().toURL());
		wsdlDescriptor = importTool.loadWsdls();
		refactorer = new EndpointWsdlRefactorer();
	}

	@After
	public void tearDown() throws Exception
	{
		sourceWsdlFile.delete();
		importedWsdlFile.delete();
	}

	@Test
	public void testRefactorWithSoapBinding() throws WsdlStrategyException, IOException, BindingNotFoundException, UnsupportedBindingException
	{
		final IEndpointWsdlData epWsdlData = new RefactorerTestEpData(SOAP_BINDING_QNAME, SOAP_PORTTYPE_QNAME, SOAP_SERVICE_QNAME);
		refactorer.refactorToEndpointWsdl(wsdlDescriptor, epWsdlData);
		verifyRefactoring(epWsdlData, BINDING_TYPE_EXPECTATION.SOAP);
	}

	@Test
	public void testRefactorWithHTTPBinding() throws WsdlStrategyException, IOException, BindingNotFoundException, UnsupportedBindingException
	{
		final IEndpointWsdlData epWsdlData = new RefactorerTestEpData(HTTP_BINDING_QNAME, HTTP_PORTTYPE_QNAME, HTTP_SERVICE_QNAME);
		refactorer.refactorToEndpointWsdl(wsdlDescriptor, epWsdlData);
		verifyRefactoring(epWsdlData, BINDING_TYPE_EXPECTATION.HTTP);
	}

	@Test
	public void testRefactorWithImportedPorttype() throws WsdlStrategyException, IOException, BindingNotFoundException, UnsupportedBindingException
	{
		final IEndpointWsdlData epWsdlData = new RefactorerTestEpData(IMPORTED_BINDING_QNAME, IMPORTED_PORTTYPE_QNAME, IMPORTED_SERVICE_QNAME);
		refactorer.refactorToEndpointWsdl(wsdlDescriptor, epWsdlData);
		verifyRefactoring(epWsdlData, BINDING_TYPE_EXPECTATION.SOAP);
	}

	@Test(expected=BindingNotFoundException.class)
	public void testRefactorWithUnexistingBinding() throws WsdlStrategyException, IOException, UnsupportedBindingException, BindingNotFoundException
	{
		final QName unexistingBindingQName = new QName("http://pesho", "gosho");
		final IEndpointWsdlData epWsdlData = new RefactorerTestEpData(unexistingBindingQName, IMPORTED_PORTTYPE_QNAME, IMPORTED_SERVICE_QNAME);
		refactorer.refactorToEndpointWsdl(wsdlDescriptor, epWsdlData);
	}
	
	private void verifyRefactoring(final IEndpointWsdlData epWsdlData, BINDING_TYPE_EXPECTATION bindingExpectation)
	{
		final List<Binding> bindings = new ArrayList<Binding>();
		final List<PortType> porttypes = new ArrayList<PortType>();
		final List<Service> services = new ArrayList<Service>();

		final WsdlVisitor<RuntimeException> visitor = new WsdlVisitor<RuntimeException>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void visit(IWsdlArtifact<?> currentArtifact) throws RuntimeException, CancelWalking
			{
				final IWsdlDefinition def = (IWsdlDefinition) currentArtifact;
				services.addAll(def.getEObject().getEServices());
				bindings.addAll(def.getEObject().getEBindings());
				porttypes.addAll(def.getEObject().getEPortTypes());
			}
		};
		(new WsdlWalker<RuntimeException>(visitor)).walk(wsdlDescriptor.getRootWsdlDefinition());

		assertEquals("One porttype expected", 1, porttypes.size());
		assertEquals("One binding expected", 1, bindings.size());
		assertEquals("One service expected", 1, services.size());
		assertEquals("One port expected", 1, services.iterator().next().getEPorts().size());

		final PortType thePorttype = porttypes.iterator().next();
		assertEquals("Unexpected porttype QName", epWsdlData.porttypeQName(), thePorttype.getQName());
		final Binding theBinding = bindings.iterator().next();
		assertEquals("Unexpected binding QName", epWsdlData.bindingQName(), theBinding.getQName());
		assertEquals("Binding not associated with expected porttype", thePorttype.getQName(), theBinding.getPortType().getQName());
		final Service theService = services.iterator().next();
		assertEquals("Unexpected service QName", epWsdlData.serviceQName(), theService.getQName());
		final Port thePort = (Port) theService.getEPorts().iterator().next();
		assertEquals("Unexpected port name", epWsdlData.endpointName(), thePort.getName());
		assertEquals("Port not associated with expected binding", thePort.getBinding().getQName(), theBinding.getQName());

		final ExtensibilityElement extElement = (ExtensibilityElement) thePort.getEExtensibilityElements().iterator().next();
		switch (bindingExpectation)
		{
		case SOAP:
			assertTrue("Unexpected extensibility element type", extElement instanceof SOAPAddress);
			assertEquals("Unexpected endpoint address", epWsdlData.endpointAddress(), ((SOAPAddress) extElement).getLocationURI());
			break;
		case HTTP:
			assertTrue("Unexpected extensibility element type", extElement instanceof HTTPAddress);
			assertEquals("Unexpected endpoint address", epWsdlData.endpointAddress(), ((HTTPAddress) extElement).getLocationURI());
			break;
		}
	}

	private class RefactorerTestEpData implements IEndpointWsdlData
	{
		private QName bQName;
		private QName serviceQName;
		private QName porttypeQname;

		RefactorerTestEpData(final QName bindingQName, final QName porttypeQName, final QName serviceQName)
		{
			this.bQName = bindingQName;
			this.porttypeQname = porttypeQName;
			this.serviceQName = serviceQName;
		}

		@Override
		public QName bindingQName()
		{
			return bQName;
		}

		@Override
		public URL bindingWsdlUrl()
		{
			try
			{
				return sourceWsdlFile.toURI().toURL();
			} catch (MalformedURLException e)
			{
				throw new RuntimeException(e);
			}
		}

		@Override
		public String endpointAddress()
		{
			return ENDPOINT_ADDRESS;
		}

		@Override
		public String endpointName()
		{
			return ENDPOINT_NAME;
		}

		@Override
		public QName porttypeQName()
		{
			return this.porttypeQname;
		}

		@Override
		public QName serviceQName()
		{
			return this.serviceQName;
		}
	}
	
	private TestFileUtils testFileUtils()
	{
		return new TestFileUtils();
	}
}
