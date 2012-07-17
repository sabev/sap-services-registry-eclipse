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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.servicesregistry.wsdl.internal.wsdlimport.WsdlWtpImportToolImpl;
import org.junit.Test;

@SuppressWarnings("nls")
public class WsdlWtpImportToolImplTest
{
	@Test
	public void testDefineImportUrlReturnsImportUrlInCaseImportIsValidUrl() throws IOException 
	{
		final URL rootUrl = new URL("http://mytest/WsdlUrl");
		final TestWsdlWtpImportToolImpl importTool = new TestWsdlWtpImportToolImpl(rootUrl);
		final String imported = "http://myothertest/AnotherWsdlUrl";
		final URL resultUrl = importTool.defineImportUrl(rootUrl, imported);
		assertEquals("Invalud URL defined", imported, resultUrl.toExternalForm());
	}
	
	@Test
	public void testDefineImportReturnsComposedUrl() throws IOException {
		final String base = "http://mytest/";
		final URL rootUrl = new URL(base + "WsdlUrl");
		final TestWsdlWtpImportToolImpl importTool = new TestWsdlWtpImportToolImpl(rootUrl);
		importTool.redirected = rootUrl;
		
		final String imported = "AnotherWsdlUrl";
		final URL resultUrl = importTool.defineImportUrl(rootUrl, imported);
		assertEquals("Invalud URL defined", base + imported, resultUrl.toExternalForm());
	}
	
	@Test
	public void testDefineImportReturnsRedirectedUrl() throws IOException {
		final String base = "http://mytest/";
		final URL rootUrl = new URL(base + "WsdlUrl");
		final TestWsdlWtpImportToolImpl importTool = new TestWsdlWtpImportToolImpl(rootUrl);
		final String redirect = "redirected/";
		importTool.redirected = new URL(base + redirect);
		
		final String imported = "AnotherWsdlUrl";
		final URL resultUrl = importTool.defineImportUrl(rootUrl, imported);
		assertEquals("Invalud URL defined", base + redirect + imported, resultUrl.toExternalForm());
	}	
	
	private class TestWsdlWtpImportToolImpl extends WsdlWtpImportToolImpl 
	{
		public URL redirected;
		
		public TestWsdlWtpImportToolImpl(URL wsdlLocation) {
			super(wsdlLocation);
		}

		@Override
		protected URL defineImportUrl(final URL root, final String imported) throws IOException {
			return super.defineImportUrl(root, imported);
		}
		
		protected URL getRedirectedUrl(final HttpURLConnection httpConnection) throws IOException {	
			return redirected;
		}
	}
}
