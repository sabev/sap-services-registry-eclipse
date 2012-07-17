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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.servicesregistry.testutils.TestProject;
import org.eclipse.servicesregistry.wsdl.internal.util.URLUtils;
import org.eclipse.servicesregistry.wsdl.wsdlimport.IWsdlDownloadManager;
import org.eclipse.servicesregistry.wsdl.wsdlimport.WsdlDownloadManagerFactory;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlDefinition;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WsdlDownloadManagerTest {
	
	protected final static String WSDL_PATH = "wsdls";

	protected final static String WSDL_NAME = "conversions.wsdl";

	private IFolder wsdlFolder;
	private TestProject testProject;

	@Before
	public void setUp() throws Exception
	{
		testProject = new TestProject();
		wsdlFolder = testProject.getProject().getFolder(WSDL_PATH);
		wsdlFolder.create(true, true, null);

	}
	
	@After
	public void tearDown() throws CoreException
	{
		testProject.dispose();
	}

	private void loadFiles(String[] files) throws Exception
	{
		for (String fileName : files)
		{
			IFile file = wsdlFolder.getFile(fileName);
			InputStream is = this.getClass().getResourceAsStream(WSDL_PATH + "/" + fileName);
			file.create(is, true, null);
			is.close();
		}
	}

	private URL getWsldUrl(String wsdlName) throws MalformedURLException
	{
		String path = testProject.getProject().getLocation().toOSString() + "/" + WSDL_PATH + "/" + wsdlName;

		URL url;
		
		try {
			url = new URLUtils().resolveStringToUriCompliantURL("file:/"+path);	
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		
		return url;
	}
	
	@Test
	public void testDownload() throws Exception
	{
		loadFiles(new String[] { WSDL_NAME });

		IWsdlDownloadManager mgr = WsdlDownloadManagerFactory.create();
		
		IFolder srcFolder = testProject.getProject().getFolder("src");
		IFile file = srcFolder.getFile("target.wsdl");
		String path = file.getRawLocation().toString();
		
		try
		{
			mgr.download(null, new File(path));
			Assert.fail("Expected NullpointerException");
		}
		catch (NullPointerException ex)
		{
			//ok
		}
		
		try
		{
			mgr.download(getWsldUrl(WSDL_NAME), null);
			Assert.fail("Expected NullpointerException");
		}
		catch (NullPointerException ex)
		{
			//ok
		}
		
		IWsdlWtpDescriptorContainer container = mgr.download(getWsldUrl(WSDL_NAME), new File(path));
		IWsdlDefinition root = container.getRootWsdlDefinition();
		Assert.assertNotNull(root);
		Assert.assertEquals("conversions.wsdl", root.getFile().getName());
		
	}

}
