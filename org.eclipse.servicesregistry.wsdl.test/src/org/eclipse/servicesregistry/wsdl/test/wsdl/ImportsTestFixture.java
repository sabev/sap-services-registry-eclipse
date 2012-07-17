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
package org.eclipse.servicesregistry.wsdl.test.wsdl;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.servicesregistry.testutils.TestProject;
import org.eclipse.servicesregistry.wsdl.wsdlimport.IWsdlWtpImportTool;
import org.junit.After;
import org.junit.Before;


/**
 * This fixture gives access to six wsdls:
 * <ul>
 * <li>Three WSDLs importing, including, and redefining a valid schema which is not used anywhere throughout the wsdl document</li>
 * <li>Three WSDLs importing, including, and redefining a schema with an invalid schemaLocation attribute. Again, the schema is not used anywhere throughout the wsdl document.</li>
 * </ul>
 * The fixture is project based - it creates a java project on setup and deletes it on teardown. A convenience method is declared returning the java.io.File for the target download folder,
 * which is the source folder of the java project.
 * @author Dimitar Georgiev
 *
 */
public abstract class ImportsTestFixture{
	
	private static final String WSDL_NAME = "NewWSDLFile.wsdl";
	private static final String IMP = "imp/";
	private static final String INCL = "incl/";
	private static final String REDEF = "redef/";
	
	private static final String UNUSED_SUBFOLDER = "unusedimp/";
	private static final String INACCESSIBLE_SUBFOLDER = "inaccessible/";
	
	protected IWsdlWtpImportTool importTool;
	
	private TestProject testProject;

	@Before
	public void setUp() throws Exception {
		testProject = new TestProject();
	}
	
	@After
	public void tearDown() throws Exception {
		testProject.dispose();
	}
	
	protected URL getWsdlWithUnusedImport() {
		return getWsdlUrl(UNUSED_SUBFOLDER+IMP);
	}
	
	protected URL getWsdlWithUnusedInclude() {
		return getWsdlUrl(UNUSED_SUBFOLDER+INCL);
	}
	protected URL getWsdlWithUnusedRedefine() {
		return getWsdlUrl(UNUSED_SUBFOLDER+REDEF);
	}
	
	protected URL getWsdlWithUnusedAndInaccessibleImport() {
		return getWsdlUrl(INACCESSIBLE_SUBFOLDER+IMP);
	}
	
	protected URL getWsdlWithUnusedAndInaccessibleInclude() {
		return getWsdlUrl(INACCESSIBLE_SUBFOLDER+INCL);
	}
	
	protected URL getWsdlWithUnusedAndInaccessibleRedefine() {
		return getWsdlUrl(INACCESSIBLE_SUBFOLDER+REDEF);
	}
	
	private URL getWsdlUrl(String subfolder) {
		try{
			URL bundleUrl = this.getClass().getResource("wsdls/"+subfolder+WSDL_NAME);
			return FileLocator.toFileURL(bundleUrl);
		}catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected File getDownloadFolder() {
		return testProject.getProject().getLocation().toFile();
	}
	
}
