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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.servicesregistry.wsdl.internal.util.IURLUtils;
import org.eclipse.servicesregistry.wsdl.internal.util.URLUtils;
import org.junit.Before;
import org.junit.Test;

public class URLUtilsTest
{
	private IURLUtils urlUtils;
	
	@Before
	public void setUp()
	{
		urlUtils = new URLUtils();
	}

	@Test
	public void testResolveToUrlWithURICompliantFileStrings() throws MalformedURLException, URISyntaxException
	{
		final String[] compliantStrings = new String[] { "file:/C:/dir1/dir2/myfile.txt", "file:/C:\\dir1\\dir2\\myfile.txt",
						"file:///C:/dir1/dir2/myfile.txt", "file:///C:\\dir1\\dir2\\myfile.txt" };

		final String expectedURLString = "file:/C:/dir1/dir2/myfile.txt";
		for (String compliantString : compliantStrings)
		{
			assertEquals(expectedURLString, urlUtils.resolveStringToUriCompliantURL(compliantString).toString());
		}
	}

	@Test
	public void testResolveToUrlWithURICompliantHttpString() throws MalformedURLException, URISyntaxException
	{
		final String compliantString = "http://myhost.mydomain:12345/myresource.xml";
		assertEquals(compliantString, urlUtils.resolveStringToUriCompliantURL(compliantString).toString());
	}

	@Test
	public void testResolveToUrlWithFileLocationString() throws IOException, URISyntaxException
	{
		final File testFile = File.createTempFile("URLUtilsETest_" + System.currentTimeMillis(), null);
		final String filePath = testFile.getAbsolutePath();
		final URL resolvedURL = urlUtils.resolveStringToUriCompliantURL(filePath);
		assertEquals("URL incorrectly resolved", testFile.toURI().toURL().toString().toLowerCase(), resolvedURL.toString().toLowerCase());
	}

	@Test
	public void testResolveToUrlWithSpaces() throws MalformedURLException, URISyntaxException
	{
		final String urlString = "file:/C:/Space dir/space file.txt";
		final String expectedURLString = "file:/C:/Space%20dir/space%20file.txt";

		assertEquals(expectedURLString, urlUtils.resolveStringToUriCompliantURL(urlString).toString());
	}

	@Test
	public void testResolveToUrlWithPercent20() throws MalformedURLException, URISyntaxException
	{
		final String urlString = "file:/C:/Space%20dir/space%20file.txt";
		final String expectedURLString = "file:/C:/Space%20dir/space%20file.txt";

		assertEquals(expectedURLString, urlUtils.resolveStringToUriCompliantURL(urlString).toString());
	}

	@Test
	public void testResolveToUrlWithNetbiosLocations() throws MalformedURLException, URISyntaxException
	{
		final String urlString = "\\\\localhost\\dir1\\dir2\\myfile.txt";
		final String expectedUrlString = new File(urlString).toURI().toURL().toExternalForm();
		assertEquals(expectedUrlString, urlUtils.resolveStringToUriCompliantURL(urlString).toString());
	}

	@Test
	public void testResolveToUrlWithNetbiosLocationsWithFile() throws MalformedURLException, URISyntaxException
	{
		final String urlString = "file:///\\\\10.10.10.10\\dir1\\dir2\\myfile.txt";
		final String expectedUrlString = new URL(urlString).toExternalForm();
		assertEquals(expectedUrlString, urlUtils.resolveStringToUriCompliantURL(urlString).toString());
	}

	@Test
	public void testResolveToUrlWithNetbiosLocationsWithSpaces() throws MalformedURLException, URISyntaxException
	{
		final String urlString = "\\\\localhost\\Spaced dir\\Spaced file";
		final String expectedUrlString = new File(urlString).toURI().toURL().toExternalForm();
		assertEquals(expectedUrlString, urlUtils.resolveStringToUriCompliantURL(urlString).toString());
	}

	@Test
	public void testResolveHttpUrlWithSpaces() throws MalformedURLException, URISyntaxException
	{
		final String urlWithSpaces = "http://test.host:1234/my dir/myresource";
		final String expectedString = "http://test.host:1234/my%20dir/myresource";
		assertEquals("URL with spaces not properly resolved", expectedString, urlUtils.resolveStringToUriCompliantURL(urlWithSpaces).toString());
	}

	@Test
	public void testResolveHttpUrlWithAnchors() throws MalformedURLException, URISyntaxException
	{
		final String urlWithSpaces = "http://test.host:1234/my dir/myresource#sec1";
		final String expectedString = "http://test.host:1234/my%20dir/myresource#sec1";
		assertEquals("URL with anchors not properly resolved", expectedString, urlUtils.resolveStringToUriCompliantURL(urlWithSpaces).toString());
	}

	@Test
	public void testResolveHttpUrlSquareBrackets() throws MalformedURLException, URISyntaxException
	{
		final String urlWithSpaces = "http://test.host:1234/my dir/myresource[sec1]";
		final String expectedString = "http://test.host:1234/my%20dir/myresource%5Bsec1%5D";
		assertEquals("URL with anchors not properly resolved", expectedString, urlUtils.resolveStringToUriCompliantURL(urlWithSpaces).toString());
	}

	@Test
	public void testResolveHttpUrlWithUserInfo() throws MalformedURLException, URISyntaxException
	{
		final String urlWithSpaces = "http://myuser:mypass@test.host:1234/my dir/myresource[sec1]";
		final String expectedString = "http://myuser:mypass@test.host:1234/my%20dir/myresource%5Bsec1%5D";
		assertEquals("URL with user info not properly resolved", expectedString, urlUtils.resolveStringToUriCompliantURL(urlWithSpaces).toString());
	}

	@Test
	public void testResolveFileUrlWithFileLocation() throws MalformedURLException, URISyntaxException
	{
		final String urlWithSpaces = "C:\\mydir\\myfile.txt";
		final String expectedString = "file:/C:/mydir/myfile.txt";
		assertEquals("URL with file location", expectedString, urlUtils.resolveStringToUriCompliantURL(urlWithSpaces).toString());
	}

	@Test
	public void testResolveFileUrlWithSpaceInHttp() throws MalformedURLException, URISyntaxException
	{
		final String urlWithSpaces = "http://10.55.72.66:8088/ws/New Folder/W2JDL Annotations.wsdl";
		final String expectedString = "http://10.55.72.66:8088/ws/New%20Folder/W2JDL%20Annotations.wsdl";
		assertEquals("URL with file location", expectedString, urlUtils.resolveStringToUriCompliantURL(urlWithSpaces).toString());
	}

	@Test
	public void testResolveFileUrlWithEscapedSpaceInHttp() throws MalformedURLException, URISyntaxException
	{
		final String urlWithSpaces = "http://10.55.72.66:8088/ws/New Folder/W2JDL%20Annotations.wsdl";
		final String expectedString = "http://10.55.72.66:8088/ws/New%20Folder/W2JDL%2520Annotations.wsdl";
		assertEquals("URL with file location", expectedString, urlUtils.resolveStringToUriCompliantURL(urlWithSpaces).toString());
	}

	@Test
	public void testEscapedSpaceInFileLocation() throws MalformedURLException, URISyntaxException
	{
		final String urlWithSpaces = "C:\\my dir\\my%20file.txt";
		final String expectedString = "file:/C:/my%20dir/my%2520file.txt";
		assertEquals("URL with file location", expectedString, urlUtils.resolveStringToUriCompliantURL(urlWithSpaces).toString());
	}

	@Test
	public void testEscapedSpaceInFileURL() throws MalformedURLException, URISyntaxException
	{
		final String urlWithSpaces = "file:/C:\\my dir\\my%20file.txt";
		final String expectedString = "file:/C:/my%20dir/my%2520file.txt";
		assertEquals("URL with file location", expectedString, urlUtils.resolveStringToUriCompliantURL(urlWithSpaces).toString());
	}

	@Test
	public void testIsUrlRfc2396Compatible() throws MalformedURLException
	{
		final URL compatibleURL = new URL("file:/C/dir1/dir2/file1.txt");
		final URL incompatibleURL = new URL("file:/C/dir 1/dir 2/file 1.txt");
		
		assertTrue("Compatible URL was not accepted", urlUtils.isRFC2396CompliantURL(compatibleURL));
		assertFalse("Incompatible URL was accepted", urlUtils.isRFC2396CompliantURL(incompatibleURL));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testResolveRelativePathSpecToURI() throws MalformedURLException, URISyntaxException {
		urlUtils.resolveStringToUriCompliantURL("pesho");
	}
}
