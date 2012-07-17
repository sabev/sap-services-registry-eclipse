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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.eclipse.servicesregistry.testutils.TestFileUtils;
import org.eclipse.servicesregistry.wsdl.internal.wsdlimport.headless.ExistingFilesHandlingException;
import org.eclipse.servicesregistry.wsdl.internal.wsdlimport.headless.IExistingFilesHandler;
import org.eclipse.servicesregistry.wsdl.wsdlimport.WsdlDownloadException;
import org.eclipse.servicesregistry.wsdl.wsdlimport.headless.HeadlessWsdlImporterFactory;
import org.eclipse.servicesregistry.wsdl.wsdlimport.headless.IWsdlImporter;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.junit.Before;
import org.junit.Test;

public class HeadlessWsdlImporterFactoryTest
{

	private WSDLImporterTestFixture fixture;
	private IExistingFilesHandler existingFilesHandler;
	
	private WSDLImporterTestFixture fixture()
	{
		return fixture;
	}
	
	private File tempFolder()
	{
		return new TestFileUtils().createTempDirectory();
	}
	
	@Before
	public void setUp() throws Exception
	{
		fixture = new WSDLImporterTestFixture();
		existingFilesHandler = new IExistingFilesHandler(){
			public void handleExistingFiles(Set<File> existingFiles) throws ExistingFilesHandlingException
			{
			}};
	}
	
	@Test
	public void testWSDLIsImportedInFolder() throws WsdlDownloadException
	{
		final IWsdlImporter importer = headlessImporterFactory().createImporter(fixture.globWhetherWsdlURL(), tempFolder(), existingFilesHandler);
		final IWsdlWtpDescriptorContainer res = importer.download();
		assertEquals(fixture().globWhetherWsdlURL(), res.getRootWsdlDefinition().getOriginalLocation());
	}
	
	@Test
	public void testFolderCreatedIfNotExisted() throws WsdlDownloadException, IOException 
	{
		final File tempFolder = File.createTempFile(Long.valueOf(System.currentTimeMillis()).toString(), "txt").getParentFile();
		final File notExistingDir = new File(tempFolder, "Folder"+Long.valueOf(System.currentTimeMillis()).toString());
		assertFalse(notExistingDir.exists());
		final IWsdlImporter importer = headlessImporterFactory().createImporter(fixture.globWhetherWsdlURL(), notExistingDir, existingFilesHandler);
		try {
			importer.download();
			assertTrue(notExistingDir.exists());
			assertEquals(1, notExistingDir.list().length); 
		} finally
		{
			new TestFileUtils().deleteDirectory(notExistingDir);
		}
	}
	
	@Test(expected=WsdlDownloadException.class)
	public void testExceptionIsThrownIfUrlUnreachable() throws MalformedURLException, WsdlDownloadException
	{
		final IWsdlImporter importer = headlessImporterFactory().createImporter(new URL("http://I don't exist.not existing domain"), tempFolder(), existingFilesHandler);
		importer.download();
	}

	@Test
	public void testWSDLIsImportedWithAlreadyExistingFiles() throws WsdlDownloadException 
	{
		final File targetDir = tempFolder();
		final IWsdlImporter importer = headlessImporterFactory().createImporter(fixture.globWhetherWsdlURL(), targetDir, existingFilesHandler);
		final IWsdlWtpDescriptorContainer res = importer.download();
		assertTrue("WSDL file does not exist", res.getRootWsdlDefinition().getFile().exists());

		final boolean[] handlerCalled = new boolean[] { false };
		existingFilesHandler = new IExistingFilesHandler()
		{
			public void handleExistingFiles(Set<File> existingFiles) throws ExistingFilesHandlingException
			{
				handlerCalled[0] = true;
				assertEquals("One file expected", 1, existingFiles.size());
				assertTrue("Unexpected existing file", existingFiles.iterator().next().equals(res.getRootWsdlDefinition().getFile()));
				assertTrue("Wsdl file was not deleted", res.getRootWsdlDefinition().getFile().delete());
			}
		};
		final IWsdlImporter secondImporter = headlessImporterFactory().createImporter(fixture.globWhetherWsdlURL(), targetDir, existingFilesHandler);
		final IWsdlWtpDescriptorContainer secondContainer = secondImporter.download();
		assertTrue("WSDL file does not exist", secondContainer.getRootWsdlDefinition().getFile().exists());
		assertTrue("Existing resources handler was not invoked", handlerCalled[0]);
	}
	
	private HeadlessWsdlImporterFactory headlessImporterFactory()
	{
		return new HeadlessWsdlImporterFactory();
	}
	
}
