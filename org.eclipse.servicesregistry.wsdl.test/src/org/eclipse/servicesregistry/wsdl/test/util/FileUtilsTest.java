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
package org.eclipse.servicesregistry.wsdl.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.servicesregistry.wsdl.internal.util.FileUtils;
import org.eclipse.servicesregistry.wsdl.internal.util.IFileUtils;
import org.junit.Before;
import org.junit.Test;

public class FileUtilsTest
{
	private static final String FILENAME = "MyTestFile";
	private IFileUtils fileUtils;

	@Before
	public void setUp()
	{
		fileUtils = FileUtils.getInstance();
	}

	@Test
	public void testGetFileNameForFileWithoutExtension()
	{
		assertEquals("Unexpected filename", FILENAME, fileUtils.getFileNameWithoutExtension(new File(FILENAME)));
	}

	@Test
	public void testGetFileNameForFileWithExtension()
	{
		assertEquals("Unexpected filename", FILENAME, fileUtils.getFileNameWithoutExtension(new File(FILENAME + ".test")));
	}

	@Test
	public void testGetFileNameForFileEndingWithDot()
	{
		assertEquals("Unexpected filename", FILENAME, fileUtils.getFileNameWithoutExtension(new File(FILENAME + ".")));
	}

	@Test
	public void testGetExtensionForFileWithoutExtension()
	{
		assertEquals("Unexpected file extension", "", fileUtils.getFileExtension(new File(FILENAME)));
	}

	@Test
	public void testGetExtensionForFileEndingWithDot()
	{
		assertEquals("Unexpected file extension", "", fileUtils.getFileExtension(new File(FILENAME + ".")));
	}

	@Test
	public void testGetExtensionForFileWithExtension()
	{
		assertEquals("Unexpected file extension", "test", fileUtils.getFileExtension(new File(FILENAME + ".test")));
	}

	@Test
	public void testIsFileForRemoteURL() throws MalformedURLException, URISyntaxException, FileNotFoundException
	{
		assertFalse(fileUtils.isFile(new URL("http://my.domain/something")));
	}

	@Test
	public void testIsFileForLocalUnexistingURL() throws URISyntaxException, IOException
	{
		final File f = File.createTempFile("test", null);
		assertTrue("Temp file could not be deleted", f.delete());
		assertTrue("url is a file url but isFile returned false", fileUtils.isFile(f.toURI().toURL()));
	}

	@Test
	public void testIsFileForLocalExistingURL() throws URISyntaxException, IOException
	{
		final File f = File.createTempFile("test", null);
		assertTrue(fileUtils.isFile(f.toURI().toURL()));
	}

	@Test
	public void testGetSystemDir()
	{
		assertEquals("Unexpected temp dir", new File(System.getProperty("java.io.tmpdir")), fileUtils.systemTempDir());
	}
	
}
