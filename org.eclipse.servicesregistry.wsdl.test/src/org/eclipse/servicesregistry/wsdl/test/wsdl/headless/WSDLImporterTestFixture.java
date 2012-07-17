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
package org.eclipse.servicesregistry.wsdl.test.wsdl.headless;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.servicesregistry.testutils.TestProject;

public class WSDLImporterTestFixture
{
	final private TestProject testProject;
	final private IFile globWhetherWsdl;
	final private IFile bankValWsdl;
	final private IFolder wsdlFolder;
	final private IFile wrongSyntxWsdl;
	private IFile withoutBindingsWSDL = null;
	
	public WSDLImporterTestFixture() throws CoreException
	{
		testProject = new TestProject("BrowserUtilTestProject");
		testProject.createSourceFolder("src");

		wsdlFolder = testProject.getProject().getFolder("wsdl");
		wsdlFolder.create(false, true, null);

		globWhetherWsdl = wsdlFolder.getFile("globalweather.wsdl");
		globWhetherWsdl.create(this.getClass().getResourceAsStream("wsdl/globalweather.wsdl"), false, null);

		bankValWsdl = wsdlFolder.getFile("BankVal.wsdl");
		bankValWsdl.create(this.getClass().getResourceAsStream("wsdl/BankVal.wsdl"), false, null);
		
		wrongSyntxWsdl = wsdlFolder.getFile("WrongSyntax.wsdl");
		wrongSyntxWsdl.create(this.getClass().getResourceAsStream("wsdl/WrongSyntax.wsdl"), false, null);
		
	}
	
	public TestProject project()
	{
		return testProject;
	}

	public URL globWhetherWsdlURL() {
		return createUrl(globWhetherWsdl);
	}

	public URL bankValWsdlUrl() {
		return createUrl(bankValWsdl);
	}

	public IFolder wsdlFolder() {
		return wsdlFolder;
	}

	public URL wrongSyntxWsdlUrl() {
		return createUrl(wrongSyntxWsdl);
	}
	
	public URL withoutBindingsWSDLUrl() throws CoreException {
		return createUrl(getWithoutBindingsWSDL());
	}

	private URL createUrl(IFile file)
	{
		try
		{
			return file.getLocationURI().toURL();	
		} catch (MalformedURLException mue)
		{
			throw new RuntimeException(mue);
		}
	}
	
	protected IFile getWithoutBindingsWSDL() throws CoreException {
		if(withoutBindingsWSDL==null) {
			withoutBindingsWSDL = wsdlFolder.getFile("withoutBinndings.wsdl");
			withoutBindingsWSDL.create(this.getClass().getResourceAsStream("wsdl/withoutBinndings.wsdl"), false, null);
		}
		return withoutBindingsWSDL;

	}
}
