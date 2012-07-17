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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.servicesregistry.testutils.TestProject;
import org.eclipse.servicesregistry.wsdl.wsdlimport.IWsdlWtpImportTool;
import org.eclipse.servicesregistry.wsdl.wsdlimport.WsdlWtpImportToolFactory;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WsdlImportToolTest2
{
	protected final static String WSDL_PATH = "wsdls";

	protected final static String ROOTWSDL_CASEONE_EMPTY_INVALID        = "caseone_rootwsdl_empty_invalid.wsdl";
	protected final static String ROOTWSDL_CASETWO_SEMIEMPTY_VALID      = "casetwo_rootwsdl_semiempty_valid.wsdl";
	protected final static String ROOTWSDL_CASETHREE_SEMIEMPTY_INVALID  = "casethree_rootwsdl_semiempty_invalid.wsdl";
	protected final static String ROOTWSDL_CASEFOUR_UNPARSABLE          = "casefour_rootwsdl_unparsable.wsdl";

	protected final static String ROOTWSDL_CASEFIVE_INCORRECT           = "casefive_rootwsdl_incorrect.wsdl";
		
	protected final static String ROOTWSDL_CASESIX_VALID                = "casesix_rootwsdl_valid.wsdl";
	protected final static String CHILDWSDL_CASESIX_UNPARSABLE          = "casesix_childwsdl_unparsable.wsdl";

	private final static String DIAGNOSTIC_MESSAGE 					    = "{0}, Location : {1}, Line : {2}, Column : {3} \n";

	
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
	
	@Test
	public void testDownloadFailedWithEmptyInvalidWsdl() throws Exception
	{		
		IWsdlWtpImportTool importer = WsdlWtpImportToolFactory
							.createWsdlWtpImportTool(getWsldUrl(ROOTWSDL_CASEONE_EMPTY_INVALID));
			
		try
		{
			importer.downloadWsdls(new File(testProject.getProject().getLocation().toOSString()));
		    fail("Import should fail due to empty invalid wsdl. "); 
		}
		catch (IOException exc)
		{
			final String message = exc.getMessage();
			assertNotNull(message);
			
			String expectedMessage = createExceptionMessage("DOM: Premature end of file.", getWsldUrl(ROOTWSDL_CASEONE_EMPTY_INVALID).toString(), "-1", "-1");
			if (!expectedMessage.equals(message)) { // in  JDK 1.5 exception message has line and column = -1
				// in  JDK 1.6 exception has line and column = 1
				expectedMessage = createExceptionMessage("DOM: Premature end of file.", getWsldUrl(ROOTWSDL_CASEONE_EMPTY_INVALID).toString(), "1", "1");
				assertEquals(expectedMessage, message);	
			}
		}
	}	

	@Test
	public void testDownloadWithSemiemptyValidWsdl() throws Exception
	{		
		IWsdlWtpImportTool importer = WsdlWtpImportToolFactory
							.createWsdlWtpImportTool(getWsldUrl(ROOTWSDL_CASETWO_SEMIEMPTY_VALID));

		IWsdlWtpDescriptorContainer container = null;
		File targetFolder = new File(testProject.getProject().getLocation().toOSString());
		try
		{
			container = importer.downloadWsdls(targetFolder);		   
		}
		catch (IOException exc)
		{
			fail("Imported file is perfectly valid. Download operation should succeed. ");
		}
				
		//JobUtils.waitForJobsSlow();
			
		assertNotNull("Wsdl is parsable and should be successfully imported. ", container.getRootWsdlDefinition());
		assertEquals(1, container.getAllAbsoluteLocations(targetFolder).size());
	}	
	
	@Test
	public void testDownloadFailedWithSemiemptyInvalidWsdl() throws Exception
	{		
		IWsdlWtpImportTool importer = WsdlWtpImportToolFactory
							.createWsdlWtpImportTool(getWsldUrl(ROOTWSDL_CASETHREE_SEMIEMPTY_INVALID));
	
		try
		{
			importer.downloadWsdls(new File(testProject.getProject().getLocation().toOSString()));	
			fail("Import should fail due to semiempty invalid wsdl. "); 
		}
		catch (IOException exc)
		{
			final String message = exc.getMessage();
			assertNotNull(message);
			
			final String expectedMessage = "The input source is not a valid WSDL document.";				
			assertEquals(message, expectedMessage);
		}
	}	
	
	@Test
	public void testDownloadFailedWithUnparsableWsdl() throws Exception
	{
		IWsdlWtpImportTool importer = WsdlWtpImportToolFactory
							.createWsdlWtpImportTool(getWsldUrl(ROOTWSDL_CASEFOUR_UNPARSABLE));
		
		try
		{
			importer.downloadWsdls(new File(testProject.getProject().getLocation().toOSString()));	
			fail("Import should fail due to unparsable wsdl. "); 
		}
		catch (IOException exc)
		{
			final String message = exc.getMessage();
			assertNotNull(message);			
			
			final String expectedMessage = createExceptionMessage("DOM: The element type \"binding\" must be terminated by the matching end-tag \"</binding>\".", getWsldUrl(ROOTWSDL_CASEFOUR_UNPARSABLE).toString(), "65", "3");	
			assertEquals(message, expectedMessage);						   							
		}
	}
		
	@Test
	public void testDownloadWithIncorrectWsdl() throws Exception
	{
		IWsdlWtpImportTool importer = WsdlWtpImportToolFactory
							.createWsdlWtpImportTool(getWsldUrl(ROOTWSDL_CASEFIVE_INCORRECT));

		IWsdlWtpDescriptorContainer container = null;		
		File targetFolder = new File(testProject.getProject().getLocation().toOSString());
		container = importer.downloadWsdls(targetFolder);	
		
		//JobUtils.waitForJobsSlow();
				
		assertNotNull("Wsdl is parsable and should be successfully imported. ", container.getRootWsdlDefinition());		
		assertEquals(2, container.getAllAbsoluteLocations(targetFolder).size());
	}
	
	@Test
	public void testDownloadFailedWithUnparsableChildWsdl() throws Exception
	{
		IWsdlWtpImportTool importer = WsdlWtpImportToolFactory
							.createWsdlWtpImportTool(getWsldUrl(ROOTWSDL_CASESIX_VALID));
				
		try
		{
			importer.downloadWsdls(new File(testProject.getProject().getLocation().toOSString()));		
			fail("Import should fail due to unparsable child wsdl. "); 
		}
		catch (IOException exc)
		{
			final String message = exc.getMessage();
			assertNotNull(message);
			
			final String expectedMessage = createExceptionMessage("DOM: The element type \"binding\" must be terminated by the matching end-tag \"</binding>\".", getWsldUrl(CHILDWSDL_CASESIX_UNPARSABLE).toString(), "65", "5");			
			assertEquals(message, expectedMessage);		
		}
	}	

	@Test
	public void testWsdlImportingWsdlInOtherFolder() throws Exception {
		IWsdlWtpImportTool importer = WsdlWtpImportToolFactory
							.createWsdlWtpImportTool(getWsldUrl("stockquote/stockquote.wsdl"));
		File targetFolder = new File(testProject.getProject().getLocation().toOSString());
				
		IWsdlWtpDescriptorContainer container = importer.downloadWsdls(targetFolder);
		assertEquals("Expected two wsdls and one schema to be downloaded", 3, container.getAllAbsoluteLocations(targetFolder).size());
	}
	
	private URL getWsldUrl(String wsdlName) throws MalformedURLException
	{		
		return this.getClass().getResource(WSDL_PATH + "/" + wsdlName);		
	}
	
	private String createExceptionMessage(String message, String location, String line, String column)
	{
		final String exceptionMessage = MessageFormat.format(DIAGNOSTIC_MESSAGE, new Object[] {
				message, location, line, column});
		
		return exceptionMessage;
	}	
}

