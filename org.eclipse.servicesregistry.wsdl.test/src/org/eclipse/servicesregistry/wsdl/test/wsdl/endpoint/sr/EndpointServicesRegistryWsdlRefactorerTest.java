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
package org.eclipse.servicesregistry.wsdl.test.wsdl.endpoint.sr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import javax.wsdl.Service;
import javax.xml.namespace.QName;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.servicesregistry.testutils.TestFileUtils;
import org.eclipse.servicesregistry.testutils.TestProject;
import org.eclipse.servicesregistry.wsdl.endpoint.BindingNotFoundException;
import org.eclipse.servicesregistry.wsdl.endpoint.EndpointWsdlRefactorer;
import org.eclipse.servicesregistry.wsdl.endpoint.IEndpointWsdlData;
import org.eclipse.servicesregistry.wsdl.endpoint.UnsupportedBindingException;
import org.eclipse.servicesregistry.wsdl.wsdlimport.IWsdlWtpImportTool;
import org.eclipse.servicesregistry.wsdl.wsdlimport.WsdlWtpImportToolFactory;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlStrategyException;
import org.eclipse.wst.wsdl.Port;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;


/**
 * A set of component tests for the endpoint wsdl refactorer.
 * @author Dimitar Georgiev
 */
public class EndpointServicesRegistryWsdlRefactorerTest {
	
	private static final QName BINDING_QNAME = new QName(
			"http://sap.com/xi/APPL/SE/Global_simplifiedservice/HTTPAuthOverHTTP_1BindingNS",
			"SupplierSimpleByNameAndAddressQueryResponse_In_simplified_SAP_DEFAULT_PROFILE_Service_HTTPAuthOverHTTP_1Binding");

	private static final QName PORTTYPE_QNAME = new QName("http://sap.com/xi/APPL/SE/Global_simplified", "SupplierSimpleByNameAndAddressQueryResponse_In_simplified");

	private static final QName SERVICE_QNAME = new QName("http://mynamespace" , "MyService");

	private IWsdlWtpDescriptorContainer wsdlDescriptor;
	
	//the testee
	private EndpointWsdlRefactorer refactorer;

	private URL sourceWsdlUrl;

	private IWsdlWtpImportTool importTool;
	private TestProject testProject;

	@Before
	public void setUp() throws Exception {
		
		final File tempDir = testFileUtils().createTempDirectory();
		
		final File sourceWsdlFile = testFileUtils().copyClassResourceToFileSystem(this.getClass(), "BindingWsdl.wsdl", tempDir);
		@SuppressWarnings("unused")
		final File importedWsdlFile = testFileUtils().copyClassResourceToFileSystem(this.getClass(), "rootwsdl_importedwsdl_2.wsdl", tempDir);

		sourceWsdlUrl = sourceWsdlFile.toURI().toURL();
		importTool = WsdlWtpImportToolFactory.createWsdlWtpImportTool(sourceWsdlUrl);
		wsdlDescriptor = importTool.loadWsdls();
		refactorer = new EndpointWsdlRefactorer();
		
		testProject = new TestProject();
	}
	
	@After
	public void tearDown() throws CoreException {
		testProject.dispose();
	}
	
	/**
	 * Regression Test.
	 * See comment on private method createServiceWithEndpoint inside EndpointWsdlRefactorer.<br>
	 * 
	 * SR sometimes returns binding WSDLs for which the targetNamespace of the document is not declared as a prefix in the attributes
	 * of the wsdl:definitions. Even though this looks unusual, it is technically a valid WSDL.
	 * Combined with a bug in wtp.wsdl api this caused an invalid endpoint WSDL to be downloaded - one whose port's binding attribute was not prefixed with a namespace alias.<br>
	 * 
	 * Here we refactor one such binding wsdl to an endpoint wsdl and assert the downloaded document is valid according to the WSDL 1.1 specification.
	 * @throws InterruptedException 
	 * @throws InvocationTargetException 
	 * 
	 */
	@Test
	public void testRefactoringBindingWsdlFromSRProducesValidWsdl() throws IOException, WsdlStrategyException, UnsupportedBindingException, BindingNotFoundException, CoreException, InvocationTargetException, InterruptedException {
		
		refactorer.refactorToEndpointWsdl(wsdlDescriptor, new RefactorerTestEpData(BINDING_QNAME, PORTTYPE_QNAME, SERVICE_QNAME, sourceWsdlUrl));
		
		final File targetFolder = testProject.getProject().getLocation().toFile();
		importTool.saveWsdls(targetFolder);
		
		assertBindingNamespaceIsDeclared();
		
	}

	private void assertBindingNamespaceIsDeclared() {
		@SuppressWarnings("rawtypes")
		Map services = wsdlDescriptor.getRootWsdlDefinition().getEObject().getServices();
		assertEquals(1, services.size());
		@SuppressWarnings("unchecked")
		Service service = ((Entry<QName, Service>)services.entrySet().iterator().next()).getValue();
		@SuppressWarnings("rawtypes")
		Map ports = service.getPorts();
		assertEquals(1, ports.size());
		@SuppressWarnings("unchecked")
		Port port = ((Entry<String, Port>)ports.entrySet().iterator().next()).getValue();
		
		Element portElement = port.getElement();
		String bindingText = portElement.getAttribute("binding");
		String[] segs = bindingText.split(":");
		assertEquals("binding is not prefixed", 2, segs.length);
		String bindingPrefix = "xmlns:"+segs[0];
		
		String bindingNamespace = wsdlDescriptor.getRootWsdlDefinition().getEObject().getElement().getAttribute(bindingPrefix);
		assertNotNull("binding namespace is not declared in wsdl definition", bindingNamespace);
		
	}

	private class RefactorerTestEpData implements IEndpointWsdlData {
		
		private static final String ENDPOINT_ADDRESS = "http://foo/moo";
		private static final String ENDPOINT_NAME = "DaEndpoint";
		private QName bQName;
		private QName serviceQName;
		private QName porttypeQname;
		private URL sourceUrl;

		RefactorerTestEpData(final QName bindingQName, final QName porttypeQName, final QName serviceQName, final URL sourceUrl) {
			this.bQName = bindingQName;
			this.porttypeQname = porttypeQName;
			this.serviceQName = serviceQName;
			this.sourceUrl = sourceUrl;
		}

		@Override
		public QName bindingQName() {
			return bQName;
		}

		@Override
		public URL bindingWsdlUrl() {
				return sourceUrl;
		}

		@Override
		public String endpointAddress() {
			return ENDPOINT_ADDRESS;
		}

		@Override
		public String endpointName() {
			return ENDPOINT_NAME;
		}

		@Override
		public QName porttypeQName() {
			return this.porttypeQname;
		}

		@Override
		public QName serviceQName() {
			return this.serviceQName;
		}
	}
	
	private TestFileUtils testFileUtils() {
		return new TestFileUtils();
	}
	
}
