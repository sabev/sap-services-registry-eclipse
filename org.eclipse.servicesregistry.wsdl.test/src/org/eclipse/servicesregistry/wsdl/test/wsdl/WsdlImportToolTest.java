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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Set;
import java.util.zip.ZipInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.servicesregistry.testutils.TestFileUtils;
import org.eclipse.servicesregistry.testutils.TestProject;
import org.eclipse.servicesregistry.wsdl.internal.wsdlimport.WsdlWtpStrategyDefault;
import org.eclipse.servicesregistry.wsdl.walker.CancelWalking;
import org.eclipse.servicesregistry.wsdl.walker.WsdlVisitor;
import org.eclipse.servicesregistry.wsdl.walker.WsdlWalker;
import org.eclipse.servicesregistry.wsdl.wsdlimport.IWsdlWtpImportTool;
import org.eclipse.servicesregistry.wsdl.wsdlimport.WsdlWtpImportToolFactory;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifact.ARTIFACT_TYPE;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlArtifactReference;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlDefinition;
import org.eclipse.servicesregistry.wsdl.wsdlimport.model.IWsdlWtpDescriptorContainer;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.IWsdlArtifactFileNameCalculator;
import org.eclipse.servicesregistry.wsdl.wsdlimport.strategy.WsdlStrategyException;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.xsd.XSDSchema;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("nls")
public class WsdlImportToolTest
{
	protected final static String WSDL_PATH = "wsdls";

	protected final static String WSDL_NAME = "conversions.wsdl";

	protected final static String WSDL_NAME_BROKEN = "conversionsBroken.wsdl";

	protected final static String WSDL_ROOT_NAME = "wsdlroot_ClassificationService.wsdl";
	protected final static String WSDL_IMPORT1_NAME = "wsdlfile_ClassificationService1.wsdl";
	protected final static String WSDL_IMPORT2_NAME = "wsdlfile_ClassificationService2.wsdl";

	protected final static String WSDL_CONFIGURATION_NAME = "Configuration.wsdl";
	protected final static String XSD_CONFIGURATION_NAME = "Configuration.xsd";
	protected final static String WSDL_ENVELOPE_NAME = "envelope.xml";
	protected final static String WSDL_MANUFACTURER_NAME = "Manufacturer.wsdl";
	
	protected final static String WSDL_COMPOUNDTESTDEFS_NAME = "CompoundTestDefs.wsdl";
	protected final static String SCHEMA_COMPOUNDTESTDEFS_NAME = "CompoundTestDefs.xsd";
	protected final static String SCHEMA_INCLUDEDDEFS_NAME = "IncludedDefs.xsd";
	protected final static String WSDL_MARSHALLTESTSERVICE_NAME = "MarshallTestService.wsdl";
	protected final static String WSDL_MARSHALLTESTSERVICEDEFS_NAME = "MarshallTestServiceDefs.wsdl";
	protected final static String SCHEMA_MARSHALLTESTSERVICEDEFS_NAME = "MarshallTestServiceDefs.xsd";
	protected final static String WSDL_NEWSCHEMADEFS_NAME = "NewSchemaDefs.wsdl";
	protected final static String SCHEMA_NEWSCHEMADEFS_NAME = "NewSchemaDefs.xsd";
	protected final static String WSDL_ONEWAYTESTDEFS_NAME = "OneWayTestDefs.wsdl";
	protected final static String SCHEMA_ONEWAYTESTDEFS_NAME = "OneWayTestDefs.xsd";
	protected final static String [] WSDL_WITH_SPECIAL_CHARS = {"Header%7dTestService.wsdl", "Header%20TestDefs.wsdl", "Header%20TestDefs.xsd", "HeaderImport%7eschema.xsd", "HeaderInclude%7b%7dschema.xsd"};
	protected final static String [] SCHEMAIMPORT_NO_SCHEMATAG = {"stockquote.wsdl", "stockquote_imported.wsdl", "stockquote.xsd"};

	private static final String NAMED_WSDL_ROOT_FILE_NAME = "stockquote.wsdl";
	private static final String NAMED_WSDL_IMPORTED_WSDL_NAME = "stockquote_imported.wsdl";
	private static final String NAMED_WSDL_IMPORTED_SCHEMA_NAME = "stockquote.xsd";
	
	/*
	 * The test WSDL imports an external schema through redefine directive
	 */
	private static final String WSDL_WITH_SCHEMA_REDEFINE ="ECC_DOCUMENTCRTRC1.wsdl";
	
	/*
	 * The test WSDL imports itself, and imports a schema A which includes schema B which includes schema A
	 */
	private static final String WSDL_WITH_RECURSIVE_REFERENCES = "EchoWsdl.wsdl";
	
	private IFolder wsdlFolder;
	private TestVisitor testVisitor;
	private WsdlWalker<RuntimeException> wsdlWalker;
	
	private TestProject testProject;
	
	@Before
	public void setUp() throws Exception
	{
		testProject = new TestProject();
		wsdlFolder = testProject.getProject().getFolder(WSDL_PATH);
		wsdlFolder.create(true, true, null);
		testVisitor = new TestVisitor();
		wsdlWalker = new WsdlWalker<RuntimeException>(testVisitor);
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
			InputStream is = getInputStream(fileName);
			file.create(is, true, null);
			is.close();
		}
	}

	private InputStream getInputStream(String fileName) {
		return this.getClass().getResourceAsStream(WSDL_PATH + "/" + fileName);
	}

	private URL getWsdlUrlCopiedInProject(String wsdlName) throws MalformedURLException
	{
		String path = testProject.getProject().getLocation().toOSString() + "/" + WSDL_PATH + "/" + wsdlName;

		return new File(path).toURI().toURL();
	}

	@Test
	public void testCreateWithWrongValue() throws Exception
	{
		try
		{
			WsdlWtpImportToolFactory.createWsdlWtpImportTool(null);
			fail("Wrong input parameters");
		} catch (NullPointerException e)
		{
			// expected
		}

		try
		{
			WsdlWtpImportToolFactory.createWsdlWtpImportTool((URL)null, null);
			fail("Wrong input parameters");
		} catch (NullPointerException e)
		{
			// expected
		}
		
		try
		{
			WsdlWtpImportToolFactory.createWsdlWtpImportTool(null, new WsdlWtpStrategyDefault());
			fail("Wrong input parameters");
		} catch (NullPointerException e)
		{
			//expected
		}
	}

	@Test
	public void testCreateWithCorrectValue() throws Exception
	{
		WsdlWtpImportToolFactory.createWsdlWtpImportTool(getWsdlUrlCopiedInProject(WSDL_NAME));
	}

	@Test
	public void testDownload() throws Exception
	{
		loadFiles(new String[] { WSDL_NAME });
		IWsdlWtpImportTool importer = WsdlWtpImportToolFactory
							.createWsdlWtpImportTool(getWsdlUrlCopiedInProject(WSDL_NAME));
		File targetFolder = new File(testProject.getProject().getLocation().toOSString());
	
		IWsdlWtpDescriptorContainer container = importer.downloadWsdls(targetFolder);
		IWsdlDefinition root = container.getRootWsdlDefinition();
		assertTrue(root != null);
		Set<File> wsdls = container.getAllAbsoluteLocations(targetFolder);
		assertTrue(wsdls.size() == 1);
	}

	@Test
	public void testDownloadImports() throws Exception
	{
		loadFiles(new String[] { WSDL_ROOT_NAME, WSDL_IMPORT1_NAME, WSDL_IMPORT2_NAME });
		IWsdlWtpImportTool importer = WsdlWtpImportToolFactory
							.createWsdlWtpImportTool(getWsdlUrlCopiedInProject(WSDL_ROOT_NAME));
		File targetFolder = new File(testProject.getProject().getLocation().toOSString());

		IWsdlWtpDescriptorContainer container = importer.downloadWsdls(targetFolder);
		IWsdlDefinition root = container.getRootWsdlDefinition();
		assertTrue(root != null);
		Set<File> wsdls = container.getAllAbsoluteLocations(targetFolder);
		assertEquals("3 artifacts expected", 3, wsdls.size());
	}
	
	@Test
	public void testDownloadImportsIncludesSchemas() throws Exception
	{
		loadFiles(new String[] {WSDL_MARSHALLTESTSERVICE_NAME, WSDL_COMPOUNDTESTDEFS_NAME, SCHEMA_COMPOUNDTESTDEFS_NAME, 
				SCHEMA_INCLUDEDDEFS_NAME, WSDL_MARSHALLTESTSERVICEDEFS_NAME, SCHEMA_MARSHALLTESTSERVICEDEFS_NAME, 
				WSDL_NEWSCHEMADEFS_NAME, SCHEMA_NEWSCHEMADEFS_NAME, WSDL_ONEWAYTESTDEFS_NAME, SCHEMA_ONEWAYTESTDEFS_NAME});
		IWsdlWtpImportTool importer = WsdlWtpImportToolFactory
							.createWsdlWtpImportTool(getWsdlUrlCopiedInProject(WSDL_MARSHALLTESTSERVICE_NAME));
		File targetFolder = new File(testProject.getProject().getLocation().toOSString());
		IWsdlWtpDescriptorContainer container = importer.downloadWsdls(targetFolder);
		IWsdlDefinition root = container.getRootWsdlDefinition();
		assertTrue(root != null);
		Set<File> wsdls = container.getAllAbsoluteLocations(targetFolder);
		assertEquals("10 artifacts expected", 10, wsdls.size());
	}
	
	@Test
	public void testDowloadRedefine() throws Exception{
		URL wsdlUrl = this.getClass().getResource(WSDL_PATH+"/"+WSDL_WITH_SCHEMA_REDEFINE);
		IWsdlWtpImportTool tool = WsdlWtpImportToolFactory.createWsdlWtpImportTool(wsdlUrl);
		File targetFolder = wsdlFolder.getLocation().toFile();
		IWsdlWtpDescriptorContainer container = tool.downloadWsdls(targetFolder);
		assertEquals("Six artifacts expected, two wsdls and four schemas", 6, container.getAllAbsoluteLocations(targetFolder).size());
		
		wsdlWalker.walk(container.getRootWsdlDefinition());
		assertEquals("one referenced wsdl expected", 1, testVisitor.wsdls);
		assertEquals("four referenced schemas expected", 4, testVisitor.schemas);

	}
	
	@Test
	public void testDownloadWithRecursiveReferences() throws Exception{
		URL wsdlUrl = this.getClass().getResource(WSDL_PATH+"/"+WSDL_WITH_RECURSIVE_REFERENCES);
		IWsdlWtpImportTool tool = WsdlWtpImportToolFactory.createWsdlWtpImportTool(wsdlUrl);
		File targetFolder = wsdlFolder.getLocation().toFile();
		IWsdlWtpDescriptorContainer container = tool.downloadWsdls(targetFolder);
		assertEquals("Three artifacts expected, one wsdl and two schemas", 3, container.getAllAbsoluteLocations(targetFolder).size());
		wsdlWalker.walk(container.getRootWsdlDefinition());
		assertEquals("no referenced wsdl expected", 0, testVisitor.wsdls);
		assertEquals("two referenced schemas expected", 2, testVisitor.schemas);
	}
	

	@Test
	public void testDownloadImportsSchemas() throws Exception
	{
		loadFiles(new String[] { WSDL_MANUFACTURER_NAME, WSDL_CONFIGURATION_NAME, WSDL_ENVELOPE_NAME, XSD_CONFIGURATION_NAME });
		IWsdlWtpImportTool importer = WsdlWtpImportToolFactory
							.createWsdlWtpImportTool(getWsdlUrlCopiedInProject(WSDL_MANUFACTURER_NAME));
		File targetFolder = new File(testProject.getProject().getLocation().toOSString());
		IWsdlWtpDescriptorContainer container = importer.downloadWsdls(targetFolder);
		IWsdlDefinition root = container.getRootWsdlDefinition();
		assertTrue(root != null);
		Set<File> wsdls = container.getAllAbsoluteLocations(targetFolder);
		assertEquals("4 artifacts expected", 4, wsdls.size());
	}

	@Test
	public void testDownloadImportsSchemasAdditionalStrategy() throws Exception
	{
		loadFiles(new String[] { WSDL_MANUFACTURER_NAME, WSDL_CONFIGURATION_NAME, WSDL_ENVELOPE_NAME, XSD_CONFIGURATION_NAME });
		IWsdlWtpImportTool importer = WsdlWtpImportToolFactory
							.createWsdlWtpImportTool(getWsdlUrlCopiedInProject(WSDL_MANUFACTURER_NAME), new WsdlWtpStrategyDefault());
		File targetFolder = new File(testProject.getProject().getLocation().toOSString());

		IWsdlWtpDescriptorContainer container = importer.downloadWsdls( targetFolder);
		IWsdlDefinition root = container.getRootWsdlDefinition();
		assertTrue(root != null);
		Set<File> wsdls = container.getAllAbsoluteLocations(targetFolder);
		assertEquals("4 artifacts expected", 4, wsdls.size());
		
		try
		{
			container.renameWsdlDefinition(null, null);
			fail("NullPointerException was not thrown.");
		}
		catch (NullPointerException e)
		{
			assertTrue(true);
		}

		try
		{
			container.renameWsdlDefinition(wsdls.iterator().next(), null);
			fail("NullPointerException was not thrown.");
		}
		catch (NullPointerException e)
		{
			assertTrue(true);
		}

		try
		{
			container.renameWsdlDefinition(wsdls.iterator().next(), "");
			fail("IllegalArgumentException was not thrown.");
		}
		catch (IllegalArgumentException e)
		{
			assertTrue(true);
		}

		try
		{
			container.renameSchemaDefinition(null, null);
			fail("NullPointerException was not thrown.");
		}
		catch (NullPointerException e)
		{
			assertTrue(true);
		}

		try
		{
			container.renameSchemaDefinition(wsdls.iterator().next(), null);
			fail("NullPointerException was not thrown.");
		}
		catch (NullPointerException e)
		{
			assertTrue(true);
		}

		try
		{
			container.renameSchemaDefinition(wsdls.iterator().next(), "");
			fail("IllegalArgumentException was not thrown.");
		}
		catch (IllegalArgumentException e)
		{
			assertTrue(true);
		}

		try
		{
			
			File incorrectSchemaFile = container.getRootWsdlDefinition().getFile();
			container.renameSchemaDefinition(incorrectSchemaFile, "NewSchema.xsd");
			fail("FileNotFoundException was not thrown.");
		}
		catch (FileNotFoundException e)
		{
			assertTrue(true);
		}
	}
	
	@Test
	public void testDownloadWsdlAndSchemasWithSpecialChars() throws Exception
	{
		copyZippedFiles("specchars.zi_");
		
		IWsdlWtpImportTool importer = WsdlWtpImportToolFactory
							.createWsdlWtpImportTool(getWsdlUrlCopiedInProject(WSDL_WITH_SPECIAL_CHARS[0]));
		File targetFolder = new File(testProject.getProject().getLocation().toOSString());
		IWsdlWtpDescriptorContainer container = importer.downloadWsdls(targetFolder);
		IWsdlDefinition root = container.getRootWsdlDefinition();
		assertTrue(root != null);
		Set<File> wsdls = container.getAllAbsoluteLocations(targetFolder);
		assertEquals("5 artifacts expected", 5, wsdls.size());
		for (String fileName : WSDL_WITH_SPECIAL_CHARS) {
			assertFileExistsInList(wsdls, fileName);
		}
	}
	
	@Test
	public void testDownloadWsdlWithImportNamespaceToSchemaTag() throws Exception
	{
		loadFiles(SCHEMAIMPORT_NO_SCHEMATAG);
		IWsdlWtpImportTool importer = WsdlWtpImportToolFactory
							.createWsdlWtpImportTool(getWsdlUrlCopiedInProject(SCHEMAIMPORT_NO_SCHEMATAG[0]));
		File targetFolder = new File(testProject.getProject().getLocation().toOSString());

		IWsdlWtpDescriptorContainer container = importer.downloadWsdls(targetFolder);
		IWsdlDefinition root = container.getRootWsdlDefinition();
		assertTrue(root != null);
		final Set<File> wsdls = container.getAllAbsoluteLocations(targetFolder);
		assertEquals("Three files expected", 3, wsdls.size());
	}
	
	@Test
	public void testWsdlArtifactReferences() throws Exception
	{
		loadFiles(new String[]{NAMED_WSDL_ROOT_FILE_NAME, NAMED_WSDL_IMPORTED_WSDL_NAME, NAMED_WSDL_IMPORTED_SCHEMA_NAME});
		IWsdlWtpImportTool importer = WsdlWtpImportToolFactory
							.createWsdlWtpImportTool(getWsdlUrlCopiedInProject(NAMED_WSDL_ROOT_FILE_NAME));
		final IWsdlWtpDescriptorContainer container = importer.downloadWsdls(new File(testProject.getProject().getLocation().toOSString()));
		
		final IWsdlDefinition root = container.getRootWsdlDefinition();
		assertEquals("Unexpected root wsdl references", 2, root.references().size());
		for(IWsdlArtifactReference ref : root.references())
		{
			final IWsdlArtifact<?> refArtifact = ref.referencedArtifact();
			final File refFile = refArtifact.getFile();
			assertTrue("File reference " + refFile.getAbsolutePath() + " does not exist", refFile.exists());
			assertTrue("Un expected reference: " + refFile.getName(), refFile.getName().equals(NAMED_WSDL_IMPORTED_WSDL_NAME) || refFile.getName().equals(NAMED_WSDL_IMPORTED_SCHEMA_NAME));
			
			if(refFile.getName().equals(NAMED_WSDL_IMPORTED_WSDL_NAME)) 
			{
				final File f = refArtifact.references().iterator().next().referencedArtifact().getFile();
				assertEquals("Imported wsdl should have 1 reference", 1, refArtifact.references().size());
				assertEquals("Unexpected reference: " + f.getAbsolutePath(), f.getName(), NAMED_WSDL_IMPORTED_SCHEMA_NAME);
			}
			else if(refFile.getName().equals(NAMED_WSDL_IMPORTED_SCHEMA_NAME))
			{
				assertEquals("XSD schema is expected to have no references", 0, refArtifact.references().size());
			}
			else
			{
				fail("Unexpected file: " + refFile.getName());
			}
		}
	}
	
	/** as of this changelist, the artifacts in the container are relative after load and absolute after save. It is likely this behaviour will change in the very near future,
	 * the concept of absolute locations being removed from {@link IWsdlArtifact}. Then this test will fail and will need to be rewritten so it always asserts relative paths,
	 * before and after download.
	 */
	@Test
	public void testContainerRelativeLocations() throws Exception{
		loadFiles(new String[]{NAMED_WSDL_ROOT_FILE_NAME, NAMED_WSDL_IMPORTED_WSDL_NAME, NAMED_WSDL_IMPORTED_SCHEMA_NAME});
		final File rootWsdlSrcFile = new File(wsdlFolder.getLocation().toFile(), NAMED_WSDL_ROOT_FILE_NAME);
		assertTrue("Root WSDL file does not exist", rootWsdlSrcFile.exists());
		File target = testProject.getProject().getLocation().toFile();
		final IWsdlWtpImportTool importer = WsdlWtpImportToolFactory.createWsdlWtpImportTool(rootWsdlSrcFile.toURI().toURL());
		
		final IWsdlWtpDescriptorContainer container = importer.loadWsdls();
		WsdlWalker<RuntimeException> walker = new WsdlWalker<RuntimeException>(new ArtifactPathTester(true));
		walker.walk(container.getRootWsdlDefinition());
		
		importer.saveWsdls(target);
		walker = new WsdlWalker<RuntimeException>(new ArtifactPathTester(false));
		walker.walk(container.getRootWsdlDefinition());
		
	}
	
	@Test
	public void testDownloadFromFileURLWithWhitespaces() throws IOException, WsdlStrategyException {
		File sourceDir = createDirWithBlanksInPathname();
		
		//and see that we are able to handle this
		File file = testFileUtils().copyClassResourceToFileSystem(getClass(), "wsdls/"+WSDL_NAME, sourceDir);
		URL unescapedFileUrl = new URL("file:///" + file.getAbsolutePath());
		
		final IWsdlWtpImportTool importer = WsdlWtpImportToolFactory.createWsdlWtpImportTool(unescapedFileUrl);

		IWsdlWtpDescriptorContainer container = importer.loadWsdls();
		File javaFolder = new File(testProject.getProject().getLocationURI());
		importer.saveWsdls(javaFolder);
		//basic sanity assert that save was successful
		assertEquals("unexpected number of artifacts", 1, container.getAllAbsoluteLocations(javaFolder).size());
	}
	
	@Test
	public void testDownloadWsdlWithVeryLongFileName() throws Exception {
		loadFiles(new String[] { WSDL_NAME });
		IWsdlWtpImportTool importer = WsdlWtpImportToolFactory
							.createWsdlWtpImportTool(getWsdlUrlCopiedInProject(WSDL_NAME), new LongFileNameStrategy());
		File targetFolder = new File(testProject.getProject().getLocation().toOSString());
		importer.downloadWsdls(targetFolder);
	}

	private File createDirWithBlanksInPathname() {
		String currentTime = Long.valueOf(System.currentTimeMillis()).toString();
		File sourceDir = new File(getTempDir(), currentTime + "  " + "Pesho");
		assertTrue("Could not create folder:"+sourceDir.getAbsolutePath(), sourceDir.mkdir());
		return sourceDir;
	}
	
	private File getTempDir() {
		File result = new File(System.getProperty("java.io.tmpdir"));
		assertTrue("The system temporary folder does not exist", result.exists());
		return result;
	}
	
	private void copyZippedFiles(final String zipFileName) throws IOException, MalformedURLException 
	{
		final File outFile = new File(wsdlFolder.getLocation().toOSString());
		testFileUtils().unpackToDir(new ZipInputStream(getInputStream(zipFileName)), outFile);
	}
	
	private TestFileUtils testFileUtils() 
	{
		return new TestFileUtils();
	}
	
	private void assertFileExistsInList(final Set<File> wsdls, final String fileName)
	{
		boolean found = false;
		for (File file : wsdls) {
			if (file.getName().equals(fileName)) {
				found = true;
				break;
			}
		}
		
		if (!found) {
			fail("File was not found: " + fileName);
		}
	}

	private class TestVisitor implements WsdlVisitor<RuntimeException> {
		int wsdls = 0;
		int schemas = 0;

		@Override
		public void visit(IWsdlArtifact<?> currentArtifact)
				throws RuntimeException, CancelWalking {
			if(currentArtifact.getType().equals(ARTIFACT_TYPE.REFERENCED_SCHEMA)) {
				schemas++;
			}else if(currentArtifact.getType().equals(ARTIFACT_TYPE.REFERENCED_WSDL)) {
				wsdls++;
			}
		}
		
	}
	
	private class ArtifactPathTester implements WsdlVisitor<RuntimeException> {
		private boolean shouldBeRelative;
		public ArtifactPathTester(boolean shouldBeRelative) {
			this.shouldBeRelative = shouldBeRelative;
		}
		@Override
		public void visit(IWsdlArtifact<?> currentArtifact)
				throws RuntimeException, CancelWalking {
			if(shouldBeRelative) {
				assertFalse("Relative path expected", currentArtifact.getFile().isAbsolute());
			}else{
				assertTrue("Absolute path expected", currentArtifact.getFile().isAbsolute());
			}
			
		}
	}
	
	private class LongFileNameStrategy extends WsdlWtpStrategyDefault {
		@Override
		public IWsdlArtifactFileNameCalculator createWsdlFileNameCalculator() {
			final IWsdlArtifactFileNameCalculator parentCalculator = super.createWsdlFileNameCalculator();
			return new IWsdlArtifactFileNameCalculator() {
				
				@Override
				public String proposeRootWsdlFileName(URL artifactUrl, Definition wsdlDefinition) {
					char[] manyA = new char[300];
					Arrays.fill(manyA, 'a');
					return parentCalculator.proposeRootWsdlFileName(artifactUrl, wsdlDefinition) + new String(manyA);
				}
				
				@Override
				public String proposeReferencedWsdlFileName(String rootWsdlFileName, URL artifactUrl, Definition wsdlDef) {
					return parentCalculator.proposeReferencedWsdlFileName(rootWsdlFileName, artifactUrl, wsdlDef);
				}
				

				@Override
				public String proposeReferencedSchemaFileName(String rootWsdlFileName, URL artifactUrl,XSDSchema schema) {
					return parentCalculator.proposeReferencedSchemaFileName(rootWsdlFileName, artifactUrl, schema);

				}
			};
		}
	}
}
